package lilypad.server.proxy.net;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import lilypad.packet.common.Packet;
import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.packet.common.VarIntFrameCodec;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.proxy.net.http.HttpGetClient;
import lilypad.server.proxy.net.http.HttpGetClientListener;
import lilypad.server.proxy.net.http.impl.AsyncHttpGetClient;
import lilypad.server.proxy.packet.MinecraftPacketConstants;
import lilypad.server.proxy.packet.GenericPacket;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair.StateCodecProvider;
import lilypad.server.proxy.packet.impl.LoginDisconnectPacket;
import lilypad.server.proxy.packet.impl.LoginSuccessPacket;
import lilypad.server.proxy.packet.impl.PlayDisconnectPacket;
import lilypad.server.proxy.packet.impl.PlayJoinGamePacket;
import lilypad.server.proxy.packet.impl.PlayPlayerListPacket;
import lilypad.server.proxy.packet.impl.PlayRespawnPacket;
import lilypad.server.proxy.packet.impl.PlayScoreObjectivePacket;
import lilypad.server.proxy.packet.impl.PlayTeamPacket;
import lilypad.server.proxy.packet.state.HandshakeStateCodecProvider;
import lilypad.server.proxy.packet.state.LoginStateCodecProvider;
import lilypad.server.proxy.packet.state.PlayStateCodecProvider;
import lilypad.server.proxy.util.MinecraftUtils;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.IServer;
import lilypad.server.common.util.GsonUtils;
import lilypad.server.common.util.SecurityUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ProxySession {
	
	private ProxyConfig config;
	private ProxySessionMapper sessionMapper;

	private Channel inboundChannel;
	private Channel outboundChannel;
	private HttpGetClient authHttpGetClient;
	private ProxyState state = ProxyState.DISCONNECTED;

	private String username;
	private String uuid;
	private String serverAddress;
	private String serverKey;
	private byte[] verifyToken;
	private byte[] sharedSecret;

	private IServer server;
	private boolean redirecting;

	private int clientEntityId;
	private int serverEntityId;
	private Set<String> playersTabbed = new HashSet<String>();
	private Set<String> scoreboards = new HashSet<String>();
	private Set<String> teams = new HashSet<String>();

	public ProxySession(ProxyConfig config, ProxySessionMapper sessionMapper, Channel channel) {
		this.config = config;
		this.sessionMapper = sessionMapper;
		this.inboundChannel = channel;
	}

	public void inboundAuthenticate() {
		URI uri;
		try {
			uri = MinecraftUtils.getSessionURI(this.username, SecurityUtils.shaHex(this.getServerKey().getBytes("ISO_8859_1"), this.sharedSecret, this.config.proxy_getKeyPair().getPublic().getEncoded()), true);
		} catch(UnsupportedEncodingException exception) {
			exception.printStackTrace();
			return;
		}
		this.authHttpGetClient = new AsyncHttpGetClient(uri, this.inboundChannel.eventLoop());
		this.authHttpGetClient.registerListener(new HttpGetClientListener() {
			@SuppressWarnings("unchecked")
			public void httpResponse(HttpGetClient httpClient, String response) {
				Map<String, String> jsonResponse;
				jsonResponse = GsonUtils.gson().fromJson(response, HashMap.class);
				if(jsonResponse == null) {
					inboundAuthenticate(false);
					return;
				}
				if(!jsonResponse.containsKey("id")) {
					inboundAuthenticate(false);
					return;
				}
				uuid = jsonResponse.get("id");
				inboundAuthenticate(true);
			}
			public void exceptionCaught(HttpGetClient httpClient, Throwable throwable) {
				System.out.println("[LilyPad] error: Authentication to Minecraft.net Failed");
				throwable.printStackTrace();
				inboundAuthenticate(false);
			}
		});
		this.authHttpGetClient.run();
	}

	public void inboundAuthenticate(boolean success) {
		if(!this.isInboundConnected()) {
			return;
		}
		if(!success) {
			this.disconnect("Error: Authentication to Minecraft.net Failed");
			return;
		}
		if(this.sessionMapper.hasAuthenticatedByUsername(this.username)) {
			this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleLoggedIn()));
			return;
		}
		if(this.config.proxy_getPlayerMaximum() > 1 && this.sessionMapper.getAuthenticatedSize() >= this.config.proxy_getPlayerMaximum()) {
			this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleFull()));
			return;
		}
		String serverName = this.config.proxy_getDomains().get(this.serverAddress.toLowerCase());
		if(serverName == null && (serverName = this.config.proxy_getDomains().get("*")) == null) {
			this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleOffline()));
			return;
		}
		IServer server = this.config.proxy_getServerSource().getServerByName(serverName);
		if(server == null) {
			this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleOffline()));
			return;
		}
		IPlayerCallback playerCallback = this.config.proxy_getPlayerCallback();
		if(playerCallback != null) {
			int result = playerCallback.notifyPlayerJoin(this.username);
			if(result == 0) {
				this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleLoggedIn()));
				return;
			} else if(result == -1) {
				this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleOffline()));
				return;
			}
		}
		this.state = ProxyState.INIT;
		this.inboundChannel.writeAndFlush(new LoginSuccessPacket(UUID.randomUUID().toString(), this.username));
		this.inboundChannel.attr(StatefulPacketCodecProviderPair.attributeKey).get().setState(PlayStateCodecProvider.instance);
		this.sessionMapper.markAuthenticated(this);
		this.redirect(server);
	}

	public void inboundDisconnected() {
		try {
			if(this.username != null && this.isAuthenticated()) {
				if(this.config != null) {
					IPlayerCallback playerCallback = this.config.proxy_getPlayerCallback();
					if(playerCallback != null) {
						playerCallback.notifyPlayerLeave(this.username);
					}
				}
				if(this.sessionMapper != null) {
					this.sessionMapper.remove(this);
				}
			}
			if(this.playersTabbed != null) {
				this.playersTabbed.clear();
			}
			if(this.scoreboards != null) {
				this.scoreboards.clear();
			}
			if(this.teams != null) {
				this.teams.clear();
			}
			if(this.inboundChannel != null && this.inboundChannel.isOpen()) {
				this.inboundChannel.close();
			}
			if(this.outboundChannel != null && this.outboundChannel.isOpen()) {
				this.outboundChannel.close();
			}
			if(this.authHttpGetClient != null && this.authHttpGetClient.isRunning()) {
				this.authHttpGetClient.close();
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
			this.config = null;
			this.sessionMapper = null;
			this.inboundChannel = null;
			this.outboundChannel = null;
			this.authHttpGetClient = null;
			this.state = ProxyState.DISCONNECTED;
			this.username = null;
			this.serverAddress = null;
			this.serverKey = null;
			this.verifyToken = null;
			this.sharedSecret = null;
			this.server = null;
			this.redirecting = false;
			this.playersTabbed = null;
			this.scoreboards = null;
			this.teams = null;
		}
	}

	public void inboundReceived(Packet packet) {
		if(this.redirecting) {
			return;
		}
		if(packet instanceof GenericPacket) {
			((GenericPacket) packet).swapEntityId(this.clientEntityId, this.serverEntityId);
		}
		this.outboundChannel.writeAndFlush(packet);
	}

	public void outboundDisconnected(Channel channel) {
		if(this.outboundChannel != channel) {
			return;
		}
		this.disconnect(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleLostConn()));
	}

	public void outboundReceived(Channel channel, Packet packet) {
		if(this.outboundChannel == channel && this.redirecting) {
			return;
		}
		switch(packet.getOpcode()) {
		case PlayJoinGamePacket.opcode:
			PlayJoinGamePacket playJoinGamePacket = (PlayJoinGamePacket) packet;
			if(this.config.proxy_isPlayerTab()) {
				playJoinGamePacket.setMaxPlayers(60);
			} else {
				playJoinGamePacket.setMaxPlayers(0);
			}
			this.serverEntityId = playJoinGamePacket.getEntityId();
			if(this.state == ProxyState.INIT) {
				this.clientEntityId = playJoinGamePacket.getEntityId();
			} else {
				this.inboundChannel.write(new PlayRespawnPacket(playJoinGamePacket.getDimension() == 0 ? 1 : 0, 2, 0, "DEFAULT"));
				this.inboundChannel.write(new PlayRespawnPacket(playJoinGamePacket.getDimension(), playJoinGamePacket.getDifficulty(), playJoinGamePacket.getGamemode(), playJoinGamePacket.getLevelType()));
				Iterator<String> playersTabbed = this.playersTabbed.iterator();
				while(playersTabbed.hasNext()) {
					this.inboundChannel.write(new PlayPlayerListPacket(playersTabbed.next(), false, 0));
					playersTabbed.remove();
				}
				Iterator<String> scoreboards = this.scoreboards.iterator();
				while(scoreboards.hasNext()) {
					this.inboundChannel.write(new PlayScoreObjectivePacket(scoreboards.next(), "", 1));
					scoreboards.remove();
				}
				Iterator<String> teams = this.teams.iterator();
				while(teams.hasNext()) {
					this.inboundChannel.write(new PlayTeamPacket(teams.next(), 1, null, null, null, 0, null));
					teams.remove();
				}
				this.inboundChannel.flush();
				return;
			}
			break;
		case PlayPlayerListPacket.opcode:
			PlayPlayerListPacket playPlayerListPacket = (PlayPlayerListPacket) packet;
			if(playPlayerListPacket.isOnline()) {
				this.playersTabbed.add(playPlayerListPacket.getName());
			} else {
				this.playersTabbed.remove(playPlayerListPacket.getName());
			}
			break;
		case PlayScoreObjectivePacket.opcode:
			PlayScoreObjectivePacket playScoreObjectivePacket = (PlayScoreObjectivePacket) packet;
			if(playScoreObjectivePacket.isCreating()) {
				this.scoreboards.add(playScoreObjectivePacket.getName());
			} else if(playScoreObjectivePacket.isRemoving()) {
				this.scoreboards.remove(playScoreObjectivePacket.getName());
			}
			break;
		case PlayTeamPacket.opcode:
			PlayTeamPacket teamPacket = (PlayTeamPacket) packet;
			if(teamPacket.isCreating()) {
				this.teams.add(teamPacket.getName());
			} else if(teamPacket.isRemoving()) {
				this.teams.remove(teamPacket.getName());
			}
			break;
		case PlayDisconnectPacket.opcode:
			this.disconnect(((PlayDisconnectPacket) packet).getReason());
			return;
		default:
			if(packet instanceof GenericPacket) {
				((GenericPacket) packet).swapEntityId(this.clientEntityId, this.serverEntityId);
			}
			break;
		}
		this.inboundChannel.writeAndFlush(packet);
	}

	public void redirect(final IServer server) { 
		new Bootstrap().group(this.inboundChannel.eventLoop())
		.channel(NioSocketChannel.class)
		.localAddress(this.config.proxy_getOutboundAddress())
		.remoteAddress(server.getInboundAddress())
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
		.handler(new ChannelInitializer<SocketChannel>() {
			public void initChannel(SocketChannel channel) throws Exception {
				StatefulPacketCodecProviderPair packetCodecProvider = new StatefulPacketCodecProviderPair(HandshakeStateCodecProvider.instance);
				channel.attr(StatefulPacketCodecProviderPair.attributeKey).set(packetCodecProvider);
				channel.pipeline().addLast(new ReadTimeoutHandler(30));
				channel.pipeline().addLast(new VarIntFrameCodec());
				channel.pipeline().addLast(new PacketEncoder(packetCodecProvider.getServerBound()));
				channel.pipeline().addLast(new PacketDecoder(packetCodecProvider.getClientBound()));
				channel.pipeline().addLast(new ProxyOutboundHandler(server, ProxySession.this));
			}
		})
		.connect().addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					return;
				}
				disconnectIfInitializing("Error: Internal Mismatch (0x02)");
			}
		});
	}

	public void disconnect(String reason) {
		if(!this.isInboundConnected()) {
			return;
		}
		StateCodecProvider stateCodecProvider = this.inboundChannel.attr(StatefulPacketCodecProviderPair.attributeKey).get().getState();
		if(stateCodecProvider == PlayStateCodecProvider.instance) {
			this.inboundChannel.writeAndFlush(new PlayDisconnectPacket(GsonUtils.gson().toJson(reason)));
		} else if(stateCodecProvider == LoginStateCodecProvider.instance) {
			this.inboundChannel.writeAndFlush(new LoginDisconnectPacket(GsonUtils.gson().toJson(reason)));
		}
		this.inboundChannel.close();
	}

	public void disconnectIfInitializing(String message) {
		if(this.state != ProxyState.INIT) {
			return;
		}
		this.disconnect(message);
	}

	public Channel getInboundChannel() {
		return this.inboundChannel;
	}

	public InetSocketAddress getInboundAddress() {
		return (InetSocketAddress) this.inboundChannel.remoteAddress();
	}

	public boolean isInboundConnected() {
		return this.inboundChannel != null && this.inboundChannel.isOpen();
	}

	public Channel getOutboundChannel() {
		return this.outboundChannel;
	}

	public void setOutboundChannel(IServer server, Channel channel) {
		Channel oldOutboundChannel = this.outboundChannel;
		this.state = ProxyState.CONNECTED;
		this.server = server;
		this.outboundChannel = channel;
		this.redirecting = false;
		if(oldOutboundChannel != null && oldOutboundChannel.isOpen()) {
			oldOutboundChannel.close();
		}
	}

	public ProxyState getState() {
		return this.state;
	}

	public boolean isAuthenticated() {
		return this.state == ProxyState.CONNECTED || this.state == ProxyState.INIT;
	}

	public void setState(ProxyState state) {
		this.state = state;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getServerAddress() {
		return this.serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public String getServerKey() {
		return this.serverKey;
	}

	public String genServerKey() {
		return this.serverKey = SecurityUtils.randomHash();
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}

	public byte[] genVerifyToken() {
		return this.verifyToken = SecurityUtils.randomBytes(4);
	}

	public byte[] getSharedSecret() {
		return this.sharedSecret;
	}

	public void setSharedSecret(byte[] sharedSecret) {
		this.sharedSecret = sharedSecret;
	}

	public IServer getServer() {
		return this.server;
	}

	public boolean isRedirecting() {
		return this.redirecting;
	}

	public void setRedirecting(boolean redirecting) {
		this.redirecting = redirecting;
	}

}
