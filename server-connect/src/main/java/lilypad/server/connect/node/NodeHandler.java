package lilypad.server.connect.node;

import java.io.IOException;

import lilypad.packet.common.Packet;
import lilypad.packet.connect.impl.KeepalivePacket;
import lilypad.packet.connect.impl.RequestPacket;
import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;
import lilypad.server.connect.ConnectService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;

@Sharable
public class NodeHandler extends SimpleChannelInboundHandler<Packet> {

	private static final AttributeKey<NodeSession> nodeSession = new AttributeKey<NodeSession>("nodeSession");

	private ConnectService connectService;
	private IAuthenticator authenticator;
	private IPlayable playable;

	public NodeHandler(ConnectService connectService, IAuthenticator authenticator, IPlayable playable) {
		this.connectService = connectService;
		this.authenticator = authenticator;
		this.playable = playable;
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
		context.attr(nodeSession).setIfAbsent(new NodeSession(context.channel(), this.connectService, this.authenticator, this.playable));
	}

	@Override
	public void channelInactive(ChannelHandlerContext context) throws Exception {
		context.attr(nodeSession).getAndSet(null).cleanup();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
		switch(packet.getOpcode()) {
		case 0x00:
			KeepalivePacket keepalivePacket = (KeepalivePacket) packet;
			context.attr(nodeSession).get().pong(keepalivePacket.getRandom());
			break;
		case 0x01:
			context.attr(nodeSession).get().handleRequest((RequestPacket) packet);
			break;
		default:
			context.close(); // invalid packet
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		if(cause instanceof IOException) {
			if(!cause.getMessage().equals("Connection reset by peer")) {
				cause.printStackTrace();
			}
		} else if (!(cause instanceof ReadTimeoutException)) {
			cause.printStackTrace();
		}
		if(channel.isOpen()) {
			channel.close();
		}
	}

}
