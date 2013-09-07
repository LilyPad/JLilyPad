package lilypad.server.proxy.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;

import lilypad.server.common.IPlayerCallback;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.proxy.packet.CraftPacketConstants;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.impl.EncryptRequestPacket;
import lilypad.server.proxy.packet.impl.EncryptResponsePacket;
import lilypad.server.proxy.packet.impl.HandshakePacket;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;

@Sharable
public class ProxyInboundHandler extends SimpleChannelInboundHandler<Packet> {

	private static final AttributeKey<ProxySession> proxySessionKey = new AttributeKey<ProxySession>("proxySession");
	private Map<String, Long> throttle = new ConcurrentHashMap<String, Long>();

	private ProxyConfig config;
	private ProxySessionMapper sessionMapper;

	public ProxyInboundHandler(ProxyConfig config, ProxySessionMapper sessionMapper) {
		this.config = config;
		this.sessionMapper = sessionMapper;
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
		String address = this.getAddress(context.channel());
		if(this.throttle.containsKey(address) && System.currentTimeMillis() - this.throttle.get(address) < this.config.proxy_getPlayerThrottle()) {
			context.close();
			this.throttle.put(address, System.currentTimeMillis());
			return;
		} else {
			this.throttle.put(address, System.currentTimeMillis());
		}
		context.attr(proxySessionKey).set(new ProxySession(this.config, this.sessionMapper, context.channel()));
	}

	@Override
	public void channelInactive(ChannelHandlerContext context) throws Exception {
		ProxySession proxySession = context.attr(proxySessionKey).get();
		if(proxySession == null) {
			return;
		}
		context.attr(proxySessionKey).set(null);
		proxySession.inboundDisconnected();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext context, Packet packet) throws Exception {
		final ProxySession proxySession = context.attr(proxySessionKey).get();
		if(proxySession == null) {
			context.close();
			return;
		}
		switch(proxySession.getState()) {
		case DISCONNECTED:
			if(packet.getOpcode() == HandshakePacket.opcode) {
				HandshakePacket handshakePacket = (HandshakePacket) packet;
				if(handshakePacket.getProtocolVersion() > CraftPacketConstants.protocolVersion) {
					proxySession.kick("Error: Server Outdated");
					return;
				} else if(handshakePacket.getProtocolVersion() < CraftPacketConstants.protocolVersion) {
					proxySession.kick("Error: Client Outdated");
					return;
				}
				proxySession.setUsername(handshakePacket.getUsername());
				proxySession.setServerHost(handshakePacket.getServerHost());
				proxySession.setState(LoginState.ENCRYPT_REQUEST);
				proxySession.getInboundChannel().writeAndFlush(new EncryptRequestPacket(proxySession.genServerKey(), this.config.proxy_getKeyPair().getPublic(), proxySession.genServerVerification()));
				this.throttle.remove(this.getAddress(context.channel()));
			} else if(packet.getOpcode() == 0xFE) {
				IPlayerCallback playerCallback = this.config.proxy_getPlayerCallback();
				int playerCount;
				int playerMaximum;
				if(playerCallback != null) {
					playerCount = playerCallback.getPlayerCount();
					playerMaximum = playerCallback.getPlayerMaximum();
				} else {
					playerCount = this.sessionMapper.getAuthenticatedSize();
					playerMaximum = this.config.proxy_getPlayerMaximum();
				}
				proxySession.kick(CraftPacketConstants.magic + "1\0"
						+ CraftPacketConstants.protocolVersion + '\0'
						+ CraftPacketConstants.minecraftVersion + '\0'
						+ CraftPacketConstants.colorize(this.config.proxy_getPlayerMotd()) + '\0'					
						+ playerCount + '\0'
						+ playerMaximum);
				this.throttle.remove(this.getAddress(context.channel()));
			} else {
				proxySession.kick("Error: Protocol Mismatch (0x01))");
			}
			break;
		case ENCRYPT_REQUEST:
			if(packet.getOpcode() == EncryptResponsePacket.opcode) {
				EncryptResponsePacket encryptResponsePacket = (EncryptResponsePacket) packet;
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, this.config.proxy_getKeyPair().getPrivate());
				byte[] serverVerification = cipher.doFinal(encryptResponsePacket.getServerVerification());
				byte[] sharedSecret = cipher.doFinal(encryptResponsePacket.getSharedSecret());
				if(!Arrays.equals(serverVerification, proxySession.getServerVerification())) {
					proxySession.kick("Error: Protocol Mismatch (0x02)");
					return;
				}
				proxySession.setSharedSecret(sharedSecret);
				proxySession.setState(LoginState.AUTHENTICATE);
				proxySession.getInboundChannel().pipeline().addFirst(new AESDecoder(proxySession.getSharedSecret()));
				proxySession.inboundAuthenticate();
			} else {
				proxySession.kick("Error: Protocol Mismatch (0x03)");
			}
			break;
		case CONNECTED:
			proxySession.inboundReceived(packet);
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

	public String getAddress(Channel channel) {
		return ((InetSocketAddress) channel.remoteAddress()).getAddress().getHostAddress();
	}

}
