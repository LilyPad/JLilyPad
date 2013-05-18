package lilypad.server.connect.node;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lilypad.packet.common.Packet;
import lilypad.packet.connect.impl.KeepalivePacket;
import lilypad.packet.connect.impl.RequestPacket;
import lilypad.packet.connect.impl.ServerAddPacket;
import lilypad.packet.connect.impl.ServerPacket;
import lilypad.server.connect.ConnectService;
import lilypad.server.connect.query.NodeQueryLookupService;
import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;
import lilypad.server.common.IServer;
import lilypad.server.common.util.SecurityUtils;
import io.netty.channel.Channel;

public class NodeSession implements IServer {

	private ConnectService connectService;
	private IAuthenticator authenticator;
	private IPlayable playable;

	private Channel channel;
	private int ponged;

	private String username;
	private String authenticationKey;
	private NodeSessionRole role;
	
	private InetSocketAddress inboundAddress;
	private String securityKey;
	
	private String motd;
	private String version;
	private Set<String> players = new HashSet<String>();
	private int maximumPlayers;

	public NodeSession(Channel channel, ConnectService connectService, IAuthenticator authenticator, IPlayable playable) {
		this.channel = channel;
		this.connectService = connectService;
		this.authenticator = authenticator;
		this.playable = playable;
		this.role = NodeSessionRole.UNAUTHENTICATED;
	}

	public void markAuthenticated(String username) {
		this.username = username;
		this.role = NodeSessionRole.AUTHENTICATED;
		this.connectService.getSessionMapper().markAuthenticated(this);
	}

	public void markServer(InetSocketAddress inboundAddress) {
		NodeSessionMapper sessionMapper = this.connectService.getSessionMapper();
		NodeSession lastSession = sessionMapper.getServerByUsername(this.username);
		if(lastSession != null) {
			lastSession.getChannel().close();
		}
		sessionMapper.remove(this);
		this.role = NodeSessionRole.SERVER;
		this.inboundAddress = inboundAddress;
		this.securityKey = SecurityUtils.randomHash();
		sessionMapper.markAuthenticated(this);
		sessionMapper.markServer(this);
		for(NodeSession proxy : sessionMapper.getProxies()) {
			proxy.write(new ServerAddPacket(this.username, this.securityKey, this.inboundAddress.getAddress().getHostAddress(), this.inboundAddress.getPort()));
		}
	}

	public void markProxy(InetSocketAddress inboundAddress, String motd, String version, int maximumPlayers) {
		NodeSessionMapper sessionMapper = this.connectService.getSessionMapper();
		NodeSession lastSession = sessionMapper.getProxyByUsername(this.username);
		if(lastSession != null) {
			lastSession.getChannel().close();
		}
		sessionMapper.remove(this);
		this.role = NodeSessionRole.PROXY;
		this.inboundAddress = inboundAddress;
		this.motd = motd;
		this.version = version;
		this.maximumPlayers = maximumPlayers;
		sessionMapper.markAuthenticated(this);
		sessionMapper.markProxy(this);
		this.connectService.getProxyCache().registerProxy(this);
		for(NodeSession server : sessionMapper.getServers()) {
			this.write(new ServerAddPacket(server.getIdentification(), server.getSecurityKey(), server.getInboundAddress().getAddress().getHostAddress(), server.getInboundAddress().getPort()));
		}
	}

	public void handleRequest(RequestPacket requestPacket) {
		this.write(NodeQueryLookupService.instance.getById(requestPacket.getOperation()).execute(this, requestPacket.getId(), requestPacket.getPayload()));
	}

	public void write(Packet packet) {
		if(this.channel == null) {
			return;
		}
		this.channel.write(packet);
	}

	public void ping(int random) {
		if(this.ponged != 0) {
			return;
		}
		this.ponged = random;
		this.write(new KeepalivePacket(this.ponged));
	}

	public void pong(int random) {
		if(this.ponged != random) {
			return;
		}
		this.ponged = 0;
	}

	public void cleanup() {
		try {
			if(this.connectService != null && this.isAuthenticated()) {
				this.connectService.getSessionMapper().remove(this);
				switch(this.role) {
				case PROXY:
					this.connectService.getProxyCache().unregisterProxy(this);
					break;
				case SERVER:
					for(NodeSession proxy : this.connectService.getSessionMapper().getProxies()) {
						proxy.write(new ServerPacket(this.username));
					}
					break;
				default:
					break;
				}
			}
			if(this.players != null) {
				this.players.clear();
			}
		} finally {
			this.connectService = null;
			this.authenticator = null;
			this.playable = null;
			this.channel = null;
			this.ponged = 0;
			this.role = NodeSessionRole.UNAUTHENTICATED;
			this.username = null;
			this.authenticationKey = null;
			this.players = null;
		}
	}

	public ConnectService getConnectService() {
		return this.connectService;
	}

	public IAuthenticator getAuthenticator() {
		return this.authenticator;
	}

	public IPlayable getPlayable() {
		return this.playable;
	}

	public Channel getChannel() {
		return this.channel;
	}

	public InetSocketAddress getAddress() {
		return (InetSocketAddress) this.channel.remoteAddress();
	}

	public String getIdentification() {
		switch(this.role) {
		case AUTHENTICATED:
			return this.username + "." + this.channel.id();
		case SERVER:
		case PROXY:
			return this.username;
		default:
			return "";
		}
	}

	public boolean isAuthenticated() {
		return this.role != NodeSessionRole.UNAUTHENTICATED;
	}

	public String getAuthenticationKey() {
		return this.authenticationKey;
	}

	public boolean hasAuthenticationKey() {
		return this.authenticationKey != null;
	}

	public String generateAuthenticationKey() {
		return this.authenticationKey = SecurityUtils.randomHash();
	} 

	public NodeSessionRole getRole() {
		return this.role;
	}

	public InetSocketAddress getInboundAddress() {
		return this.inboundAddress;
	}

	public String getSecurityKey() {
		return this.securityKey;
	}
	
	public String getMotd() {
		return this.motd;
	}

	public String getVersion() {
		return this.version;
	}

	public Set<String> getPlayers() {
		return Collections.unmodifiableSet(this.players);
	}

	public boolean addPlayer(String player) {
		if(!this.connectService.getProxyCache().addPlayer(player, this)) {
			return false;
		}
		this.players.add(player);
		return true;
	}

	public void removePlayer(String player) {
		if(this.players.remove(player)) {
			this.connectService.getProxyCache().removePlayer(player);
		}
	}

	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}

}
