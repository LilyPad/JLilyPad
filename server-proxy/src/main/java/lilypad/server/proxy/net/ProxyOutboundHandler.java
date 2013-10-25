package lilypad.server.proxy.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import lilypad.server.proxy.packet.CraftPacketConstants;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.impl.EncryptRequestPacket;
import lilypad.server.proxy.packet.impl.HandshakePacket;
import lilypad.server.proxy.packet.impl.StatusPacket;
import lilypad.server.common.IServer;

public class ProxyOutboundHandler extends SimpleChannelInboundHandler<Packet> {

	private IServer server;
	private ProxySession proxySession;
	private LoginState state = LoginState.DISCONNECTED;

	public ProxyOutboundHandler(IServer server, ProxySession proxySession) {
		this.server = server;
		this.proxySession = proxySession;
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
		Channel channel = context.channel();
		if(!this.proxySession.isInboundConnected()) {
			channel.close();
			return;
		}
		InetSocketAddress inboundAddress = this.proxySession.getInboundAddress();
		InetSocketAddress outboundAddress = (InetSocketAddress) channel.remoteAddress();
		this.state = LoginState.ENCRYPT_REQUEST;
		channel.writeAndFlush(new HandshakePacket(CraftPacketConstants.protocolVersion, this.proxySession.getUsername(), server.getSecurityKey() + ";" + inboundAddress.getAddress().getHostAddress() + ";" + inboundAddress.getPort(), outboundAddress.getPort()));
	}

	@Override
	public void channelInactive(ChannelHandlerContext context) throws Exception {
		try {
			if(this.state == LoginState.INITIALIZE) {
				this.proxySession.setRedirecting(false);
			}
			if(this.state != LoginState.DISCONNECTED) {
				this.proxySession.outboundDisconnected(context.channel());
			}
		} finally {
			this.server = null;
			this.proxySession = null;
			this.state = LoginState.DISCONNECTED;
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
		Channel channel = context.channel();
		if(this.proxySession == null || !this.proxySession.isInboundConnected()) {
			channel.close();
			return;
		}
		switch(this.state) {
		case ENCRYPT_REQUEST:
			if(packet.getOpcode() == EncryptRequestPacket.opcode) {
				EncryptRequestPacket encryptRequestPacket = (EncryptRequestPacket) packet;
				if(!encryptRequestPacket.getServerKey().equals("-")) {
					this.proxySession.kickIfInitializing("Error: Protocol Mismatch (0x04)");
					channel.close();
					return;
				}
				this.state = LoginState.INITIALIZE;
				this.proxySession.setRedirecting(true);
				channel.writeAndFlush(new StatusPacket(0));
			} else {
				this.proxySession.kickIfInitializing("Error: Protocol Mismatch (0x05)");
				channel.close();
			}
			break;
		case INITIALIZE:
			if(packet.getOpcode() == 0x0D) {
				this.state = LoginState.CONNECTED;
				this.proxySession.setOutboundChannel(this.server, channel);
			}
		case CONNECTED:
			this.proxySession.outboundReceived(channel, packet);
			if(packet.getOpcode() == 0xFF) {
				this.state = LoginState.DISCONNECTED;
			}
			break;
		default:
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
