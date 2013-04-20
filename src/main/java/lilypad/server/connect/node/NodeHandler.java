package lilypad.server.connect.node;

import lilypad.packet.common.Packet;
import lilypad.packet.connect.impl.KeepalivePacket;
import lilypad.packet.connect.impl.RequestPacket;
import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;
import lilypad.server.connect.ConnectService;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.util.AttributeKey;

@Sharable
public class NodeHandler extends ChannelInboundMessageHandlerAdapter<Packet> {

	private static final AttributeKey<NodeSession> nodeSession = new AttributeKey<NodeSession>("nodeSession");
	
	private ConnectService connectService;
	private IAuthenticator authenticator;
	private IPlayable playable;
	
	public NodeHandler(ConnectService connectService, IAuthenticator authenticator, IPlayable playable) {
		this.connectService = connectService;
		this.authenticator = authenticator;
		this.playable = playable;
	}
	
	public void channelActive(ChannelHandlerContext context) throws Exception {
		context.attr(nodeSession).setIfAbsent(new NodeSession(context.channel(), this.connectService, this.authenticator, this.playable));
	}

	public void channelInactive(ChannelHandlerContext context) throws Exception {
		context.attr(nodeSession).getAndSet(null).cleanup();
	}
	
	public void messageReceived(ChannelHandlerContext context, Packet packet) throws Exception {
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
	
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		cause.printStackTrace();
		if(channel.isOpen()) {
			channel.close();
		}
	}

}
