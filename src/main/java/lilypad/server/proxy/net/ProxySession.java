package lilypad.server.proxy.net;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.proxy.packet.CraftPacketCodecRegistry;
import lilypad.server.proxy.packet.CraftPacketConstants;
import lilypad.server.proxy.packet.GenericPacket;
import lilypad.packet.common.Packet;
import lilypad.server.proxy.packet.impl.KickPacket;
import lilypad.server.proxy.packet.impl.LoginPacket;
import lilypad.server.proxy.packet.impl.PlayerListPacket;
import lilypad.server.proxy.packet.impl.RespawnPacket;
import lilypad.server.proxy.packet.impl.ScoreboardObjectivePacket;
import lilypad.server.proxy.packet.impl.TeamPacket;
import lilypad.server.proxy.util.MinecraftUtils;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.IServer;
import lilypad.server.common.net.DummyChannel;
import lilypad.server.common.util.SecurityUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.DefaultChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class ProxySession {

	private ProxyConfig config;
	private ProxySessionMapper sessionMapper;

	private Channel inboundChannel;
	private Channel outboundChannel;
	private ProxySessionState state = ProxySessionState.DISCONNECTED;

	private String username;
	private String serverHost;
	private String serverKey;
	private byte[] serverVerification;
	private byte[] sharedSecret;

	private IServer server;
	private boolean readable;

	private int clientEntityId;
	private int serverEntityId;
	private Set<String> playersTabbed = new HashSet<String>();
	private Set<String> scoreboards = new HashSet<String>();
	private Set<String> teams = new HashSet<String>();

	public ProxySession(ProxyConfig config, ProxySessionMapper sessionMapper, Channel channel) {
		this.config = config;
		this.sessionMapper = sessionMapper;
		this.inboundChannel = new DummyChannel(channel);
	}

	public void inboundAuthenticate() {
		try {
			if(!this.serverKey.equals("-")) {
				String serverKey = SecurityUtils.shaHex(this.getServerKey().getBytes("ISO_8859_1"), this.sharedSecret, this.config.proxy_getKeyPair().getPublic().getEncoded());
				if(!MinecraftUtils.authenticate(this.username, serverKey)) {
					this.kick("Error: Authentication to Minecraft.net Failed");
					return;
				}
				if(!this.isInboundConnected()) {
					return;
				}
			}
			if(this.sessionMapper.hasAuthenticatedByUsername(this.username)) {
				this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleLoggedIn()));
				return;
			}
			if(this.config.proxy_getPlayerMaximum() > 1 && this.sessionMapper.getAuthenticatedSize() >= this.config.proxy_getPlayerMaximum()) {
				this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleFull()));
				return;
			}
			IPlayerCallback playerCallback = this.config.proxy_getPlayerCallback();
			if(playerCallback != null) {
				int notified = playerCallback.notifyPlayerJoin(this.username);
				if(notified == 0) {
					this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleLoggedIn()));
					return;
				} else if(notified == -1) {
					this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleOffline()));
					return;
				}
			}
			String serverName = this.config.proxy_getDomains().get(this.serverHost.toLowerCase());
			if(serverName == null) {
				serverName = this.config.proxy_getDomains().get("*");
			}
			IServer server = this.config.proxy_getServerSource().getServerByName(serverName);
			if(server == null) {
				this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleOffline()));
				return;
			}
			this.state = ProxySessionState.DIRECTING;
			this.sessionMapper.markAuthenticated(this);
			this.redirect(server);
		} catch(Exception exception) {
			exception.printStackTrace();
			this.kick("Error: Internal Mismatch (0x01)");
		}
	}

	public void inboundDisconnected() {
		try {
			if(this.username != null) {
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
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
			this.config = null;
			this.sessionMapper = null;
			this.inboundChannel = null;
			this.outboundChannel = null;
			this.state = ProxySessionState.DISCONNECTED;
			this.username = null;
			this.serverHost = null;
			this.serverKey = null;
			this.serverVerification = null;
			this.sharedSecret = null;
			this.server = null;
			this.readable = false;
			this.playersTabbed = null;
			this.scoreboards = null;
			this.teams = null;
		}
	}

	public void inboundReceived(Packet packet) {
		if(!this.readable) {
			return;
		}
		if(packet instanceof GenericPacket) {
			((GenericPacket) packet).swapEntityId(this.clientEntityId, this.serverEntityId);
		}
		this.outboundChannel.write(packet);
	}

	public void outboundDisconnected(Channel channel) {
		if(this.outboundChannel != channel) {
			return;
		}
		this.kick(CraftPacketConstants.colorize(this.config.proxy_getLocaleLostConn()));
	}

	public void outboundReceived(Packet packet) {
		switch(packet.getOpcode()) {
		case LoginPacket.opcode:
			LoginPacket loginPacket = (LoginPacket) packet;
			if(this.config.proxy_isPlayerTab()) {
				loginPacket.setMaxPlayers(60);
			} else {
				loginPacket.setMaxPlayers(0);
			}
			this.serverEntityId = loginPacket.getEntityId();
			if(this.state == ProxySessionState.DIRECTING) {
				this.clientEntityId = loginPacket.getEntityId();
			} else {
				this.inboundChannel.write(new RespawnPacket(loginPacket.getDimension() == 0 ? 1 : 0, 2, 0, loginPacket.getHeight(), "DEFAULT"));
				this.inboundChannel.write(new RespawnPacket(loginPacket.getDimension(), loginPacket.getDifficulty(), loginPacket.getGamemode(), loginPacket.getHeight(), loginPacket.getLevelType()));
				Iterator<String> playersTabbed = this.playersTabbed.iterator();
				while(playersTabbed.hasNext()) {
					this.inboundChannel.write(new PlayerListPacket(playersTabbed.next(), false, 0));
					playersTabbed.remove();
				}
				Iterator<String> scoreboards = this.scoreboards.iterator();
				while(scoreboards.hasNext()) {
					this.inboundChannel.write(new ScoreboardObjectivePacket(scoreboards.next(), "", (byte) 1));
					scoreboards.remove();
				}
				Iterator<String> teams = this.teams.iterator();
				while(teams.hasNext()) {
					this.inboundChannel.write(new TeamPacket(teams.next(), (byte) 1, null));
					teams.remove();
				}
				return;
			}
			break;
		case PlayerListPacket.opcode:
			PlayerListPacket playerListPacket = (PlayerListPacket) packet;
			if(playerListPacket.isOnline()) {
				this.playersTabbed.add(playerListPacket.getPlayer());
			} else {
				this.playersTabbed.remove(playerListPacket.getPlayer());
			}
			break;
		case ScoreboardObjectivePacket.opcode:
			ScoreboardObjectivePacket scoreboardObjectivePacket = (ScoreboardObjectivePacket) packet;
			if(scoreboardObjectivePacket.isCreating()) {
				this.scoreboards.add(scoreboardObjectivePacket.getName());
			} else if(scoreboardObjectivePacket.isRemoving()) {
				this.scoreboards.remove(scoreboardObjectivePacket.getName());
			}
			break;
		case TeamPacket.opcode:
			TeamPacket teamPacket = (TeamPacket) packet;
			if(teamPacket.isCreating()) {
				this.teams.add(teamPacket.getName());
			} else if(teamPacket.isRemoving()) {
				this.teams.remove(teamPacket.getName());
			}
			break;
		case KickPacket.opcode:
			KickPacket kickPacket = (KickPacket) packet;
			this.kick(kickPacket.getMessage());
			return;
		default:
			if(packet instanceof GenericPacket) {
				((GenericPacket) packet).swapEntityId(this.clientEntityId, this.serverEntityId);
			}
			break;
		}
		this.inboundChannel.write(packet);
	}

	public void redirect(final IServer server) { 
		Bootstrap bootstrap = new Bootstrap().group(this.inboundChannel.eventLoop())
				.channel(NioSocketChannel.class)
				.localAddress(this.config.proxy_getOutboundAddress())
				.remoteAddress(server.getInboundAddress())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
				.handler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new ReadTimeoutHandler(30));
						channel.pipeline().addLast(new PacketEncoder(CraftPacketCodecRegistry.instance));
						channel.pipeline().addLast(new PacketDecoder(CraftPacketCodecRegistry.instance));
						channel.pipeline().addLast(new ProxyOutboundHandler(server, ProxySession.this));
					}
				});
		bootstrap.connect().addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					return;
				}
				kickIfDirecting("Error: Internal Mismatch (0x02)");
			}
		});
	}

	public void kick(String message) {
		if(!this.isInboundConnected()) {
			return;
		}
		this.inboundChannel.write(new KickPacket(message), new DefaultChannelPromise(this.inboundChannel)).addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if(inboundChannel == null || !inboundChannel.isOpen()) {
					return;
				}
				inboundChannel.close();
			}
		});
	}

	public void kickIfDirecting(String message) {
		if(this.state != ProxySessionState.DIRECTING) {
			return;
		}
		this.kick(message);
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
		this.state = ProxySessionState.CONNECTED;
		this.server = server;
		this.outboundChannel = new DummyChannel(channel);
		if(oldOutboundChannel != null && oldOutboundChannel.isOpen()) {
			oldOutboundChannel.close();
		}
	}

	public ProxySessionState getState() {
		return this.state;
	}

	public boolean isAuthenticated() {
		return this.state == ProxySessionState.CONNECTED;
	}

	public void setState(ProxySessionState state) {
		this.state = state;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServerIp() {
		return this.serverHost;
	}

	public void setServerHost(String serverIp) {
		this.serverHost = serverIp;
	}

	public String getServerKey() {
		return this.serverKey;
	}

	public String genServerKey() {
		if(this.config.proxy_isPlayerAuthenticate()) {
			return this.serverKey = SecurityUtils.randomHash();
		} else {
			return this.serverKey = "-";
		}
	}

	public byte[] getServerVerification() {
		return this.serverVerification;
	}

	public byte[] genServerVerification() {
		return this.serverVerification = SecurityUtils.randomBytes(4);
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

	public boolean isReadable() {
		return this.readable;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}

}
