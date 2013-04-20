package lilypad.server.proxy.net;

import java.net.InetSocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import lilypad.server.proxy.packet.CraftPacketConstants;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.impl.EncryptRequestPacket;
import lilypad.server.proxy.packet.impl.HandshakePacket;
import lilypad.server.proxy.packet.impl.StatusPacket;
import lilypad.server.common.IServer;

public class ProxyOutboundHandler extends ChannelInboundMessageHandlerAdapter<Packet> {

	private IServer server;
	private ProxySession proxySession;
	private ProxyOutboundHandlerState state = ProxyOutboundHandlerState.DISCONNECTED;

	public ProxyOutboundHandler(IServer server, ProxySession proxySession) {
		this.server = server;
		this.proxySession = proxySession;
	}

	public void channelActive(ChannelHandlerContext context) throws Exception {
		Channel channel = context.channel();
		if(!this.proxySession.isInboundConnected()) {
			channel.close();
			return;
		}
		InetSocketAddress inboundAddress = this.proxySession.getInboundAddress();
		InetSocketAddress outboundAddress = (InetSocketAddress) channel.remoteAddress();
		this.state = ProxyOutboundHandlerState.ENCRYPT_REQUEST;
		channel.write(new HandshakePacket(CraftPacketConstants.protocolVersion, this.proxySession.getUsername(), server.getSecurityKey() + ";" + inboundAddress.getAddress().getHostAddress() + ";" + inboundAddress.getPort(), outboundAddress.getPort()));
	}

	public void channelInactive(ChannelHandlerContext context) throws Exception {
		try {
			if(this.state != ProxyOutboundHandlerState.DISCONNECTED) {
				this.proxySession.outboundDisconnected(context.channel());
			}
		} finally {
			this.server = null;
			this.proxySession = null;
			this.state = ProxyOutboundHandlerState.DISCONNECTED;
		}
	}

	public void messageReceived(ChannelHandlerContext context, Packet packet) throws Exception {
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
					this.proxySession.kickIfDirecting("Error: Protocol Mismatch (0x04)");
					channel.close();
					return;
				}
				this.state = ProxyOutboundHandlerState.BUFFERING;
				this.proxySession.setReadable(false);
				channel.write(new StatusPacket(0));
			} else {
				this.proxySession.kickIfDirecting("Error: Protocol Mismatch (0x05)");
				channel.close();
			}
			break;
		case BUFFERING:
			if(packet.getOpcode() == 0x0D) {
				this.state = ProxyOutboundHandlerState.CONNECTED;
				this.proxySession.setOutboundChannel(this.server, channel);
				this.proxySession.setReadable(true);
			}
		case CONNECTED:
			this.proxySession.outboundReceived(packet);
			if(packet.getOpcode() == 0xFF) {
				this.state = ProxyOutboundHandlerState.DISCONNECTED;
			}
			break;
		default:
			break;
		}
	}

	public void exceptionCaught(ChannelHandlerContext context, Throwable cause) throws Exception {
		Channel channel = context.channel();
		//cause.printStackTrace(); ignore
		if(channel.isOpen()) {
			channel.close();
		}
	}

}
