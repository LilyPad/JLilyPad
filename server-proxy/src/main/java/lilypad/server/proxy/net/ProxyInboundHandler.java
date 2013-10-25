package lilypad.server.proxy.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.util.GsonUtils;
import lilypad.server.proxy.ProxyConfig;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.MinecraftPacketConstants;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair;
import lilypad.server.proxy.packet.impl.HandshakePacket;
import lilypad.server.proxy.packet.impl.LoginEncryptRequestPacket;
import lilypad.server.proxy.packet.impl.LoginEncryptResponsePacket;
import lilypad.server.proxy.packet.impl.LoginStartPacket;
import lilypad.server.proxy.packet.impl.StatusPingPacket;
import lilypad.server.proxy.packet.impl.StatusRequestPacket;
import lilypad.server.proxy.packet.impl.StatusResponsePacket;
import lilypad.server.proxy.packet.state.LoginStateCodecProvider;
import lilypad.server.proxy.packet.state.StatusStateCodecProvider;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import io.netty.util.AttributeKey;

@Sharable
public class ProxyInboundHandler extends SimpleChannelInboundHandler<Packet> {

	private static final AttributeKey<ProxySession> proxySessionKey = new AttributeKey<ProxySession>("proxySession");
	
	private ProxyConfig config;
	private ProxySessionMapper sessionMapper;

	public ProxyInboundHandler(ProxyConfig config, ProxySessionMapper sessionMapper) {
		this.config = config;
		this.sessionMapper = sessionMapper;
	}

	@Override
	public void channelActive(ChannelHandlerContext context) throws Exception {
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
				if(handshakePacket.getProtocolVersion() != MinecraftPacketConstants.protocolVersion) {
					context.close();
					return;
				}
				proxySession.setServerAddress(handshakePacket.getServerAddress());
				switch(handshakePacket.getRequestedState()) {
				case 1:
					proxySession.setState(ProxyState.STATUS);
					context.channel().attr(StatefulPacketCodecProviderPair.attributeKey).get().setState(StatusStateCodecProvider.instance);
					break;
				case 2:
					proxySession.setState(ProxyState.LOGIN);
					context.channel().attr(StatefulPacketCodecProviderPair.attributeKey).get().setState(LoginStateCodecProvider.instance);
					break;
				default:
					context.close();
					return;
				}
			} else {
				context.close();
			}
			break;
		case LOGIN:
			if(packet.getOpcode() == LoginStartPacket.opcode) {
				LoginStartPacket loginStartPacket = (LoginStartPacket) packet;
				proxySession.setUsername(loginStartPacket.getName());
				if(this.config.proxy_isPlayerAuthenticate()) {
					proxySession.getInboundChannel().writeAndFlush(new LoginEncryptRequestPacket(proxySession.genServerKey(), this.config.proxy_getKeyPair().getPublic(), proxySession.genVerifyToken()));
					proxySession.setState(ProxyState.LOGIN_ENCRYPT);
				} else {
					proxySession.inboundAuthenticate(true);
				}
			} else {
				context.close();
			}
			break;
		case LOGIN_ENCRYPT:
			if(packet.getOpcode() == LoginEncryptResponsePacket.opcode) {
				LoginEncryptResponsePacket loginEncryptResponsePacket = (LoginEncryptResponsePacket) packet;
				Cipher cipher = Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, this.config.proxy_getKeyPair().getPrivate());
				byte[] verifyToken = cipher.doFinal(loginEncryptResponsePacket.getVerifyToken());
				if(!Arrays.equals(verifyToken, proxySession.getVerifyToken())) {
					context.close();
					return;
				}
				proxySession.setSharedSecret(cipher.doFinal(loginEncryptResponsePacket.getSharedSecret()));
				proxySession.setState(ProxyState.VERIFY);
				proxySession.getInboundChannel().pipeline().addFirst(new AesCodec(proxySession.getSharedSecret()));
				proxySession.inboundAuthenticate();
			} else {
				context.close();
			}
			break;
		case STATUS:
			if(packet.getOpcode() == StatusRequestPacket.opcode) {
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
				Map<String, Object> version = new HashMap<String, Object>();
				version.put("name", MinecraftPacketConstants.minecraftVersion);
				version.put("protocol", MinecraftPacketConstants.protocolVersion);
				Map<String, Object> players = new HashMap<String, Object>();
				players.put("max", playerMaximum);
				players.put("online", playerCount);
				Map<String, Object> description = new HashMap<String, Object>();
				description.put("text", MinecraftPacketConstants.colorize(this.config.proxy_getPlayerMotd()));
				Map<String, Object> response = new HashMap<String, Object>();
				response.put("version", version);
				response.put("players", players);
				response.put("description", description);
				String favicon = this.config.proxy_getPlayerFavicon();
				if(favicon != null && favicon.length() != 0) {
					response.put("favicon", favicon);
				}
				proxySession.getInboundChannel().writeAndFlush(new StatusResponsePacket(GsonUtils.gson().toJson(response)));
				proxySession.setState(ProxyState.STATUS_PING);
			} else {
				context.close();
			}
			break;
		case STATUS_PING:
			if(packet.getOpcode() == StatusPingPacket.opcode) {
				proxySession.getInboundChannel().writeAndFlush(packet);
				context.close();
			} else {
				context.close();
			}
			break;
		case CONNECTED:
			proxySession.inboundReceived(packet);;
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
