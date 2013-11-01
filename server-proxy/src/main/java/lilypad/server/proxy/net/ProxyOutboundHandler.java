package lilypad.server.proxy.net;

import java.io.IOException;
import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.timeout.ReadTimeoutException;
import lilypad.server.proxy.packet.MinecraftPacketConstants;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.impl.HandshakePacket;
import lilypad.server.proxy.packet.impl.LoginDisconnectPacket;
import lilypad.server.proxy.packet.impl.LoginStartPacket;
import lilypad.server.proxy.packet.impl.LoginSuccessPacket;
import lilypad.server.proxy.packet.impl.PlayDisconnectPacket;
import lilypad.server.proxy.packet.state.LoginStateCodecProvider;
import lilypad.server.proxy.packet.state.PlayStateCodecProvider;
import lilypad.server.common.IServer;

public class ProxyOutboundHandler extends SimpleChannelInboundHandler<Packet> {

	private IServer server;
	private ProxySession proxySession;
	private ProxyState state = ProxyState.DISCONNECTED;

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
		channel.writeAndFlush(new HandshakePacket(MinecraftPacketConstants.protocolVersion, server.getSecurityKey() + ";" + inboundAddress.getAddress().getHostAddress() + ";" + inboundAddress.getPort() + ";" + proxySession.getUuid(), outboundAddress.getPort(), 2));
		context.channel().attr(StatefulPacketCodecProviderPair.attributeKey).get().setState(LoginStateCodecProvider.instance);
		channel.writeAndFlush(new LoginStartPacket(this.proxySession.getUsername()));
		this.state = ProxyState.LOGIN;
	}

	@Override
	public void channelInactive(ChannelHandlerContext context) throws Exception {
		try {
			if(this.state == ProxyState.INIT) {
				this.proxySession.setRedirecting(false);
			}
			if(this.state != ProxyState.DISCONNECTED) {
				this.proxySession.outboundDisconnected(context.channel());
			}
		} finally {
			this.server = null;
			this.proxySession = null;
			this.state = ProxyState.DISCONNECTED;
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
		case LOGIN:
			if(packet.getOpcode() == LoginSuccessPacket.opcode) {
				this.state = ProxyState.INIT;
				this.proxySession.setRedirecting(true);
				context.channel().attr(StatefulPacketCodecProviderPair.attributeKey).get().setState(PlayStateCodecProvider.instance);
			} else if(packet.getOpcode() == LoginDisconnectPacket.opcode) {
				this.proxySession.disconnect(new PlayDisconnectPacket((((LoginDisconnectPacket) packet)).getJson()));
				channel.close();
			} else {
				this.proxySession.disconnectIfInitializing("Error: Protocol Mismatch");
				channel.close();
			}
			break;
		case INIT:
			if(packet.getOpcode() == 0x08) {
				this.state = ProxyState.CONNECTED;
				this.proxySession.setOutboundChannel(this.server, channel);
			}
		case CONNECTED:
			this.proxySession.outboundReceived(channel, packet);
			if(packet.getOpcode() == PlayDisconnectPacket.opcode) {
				this.state = ProxyState.DISCONNECTED;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		if(cause instanceof IOException && cause.getMessage().equals("Connection reset by peer")) {
			// ignore
		} else if(cause instanceof ReadTimeoutException) {
			// ignore
		} else if(cause instanceof DecoderException) {
			// ignore
		} else {
			cause.printStackTrace();
		}
		if(channel.isOpen()) {
			channel.close();
		}
	}

}
