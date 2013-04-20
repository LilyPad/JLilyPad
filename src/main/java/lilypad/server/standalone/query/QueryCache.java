package lilypad.server.standalone.query;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lilypad.server.common.IPlayable;
import lilypad.server.common.IServer;

public class QueryCache implements IPlayable {

	public static final InetSocketAddress blankAddress = new InetSocketAddress("0.0.0.0", 0);
	
	private String motd;
	private InetSocketAddress serverAddress;
	private Set<String> players = new HashSet<String>();
	private int playerMaximum;
	private String version;
	
	public QueryCache() {
		this.invalidate();
	}
	
	public void invalidate() {
		this.motd = "Unknown";
		this.serverAddress = blankAddress;
		this.players.clear();
		this.playerMaximum = 0;
		this.version = "Unknown";
	}
	
	public String getMotd() {
		return this.motd;
	}
	
	public void setMotd(String motd) {
		this.motd = motd;
	}

	public InetSocketAddress getBindAddress() {
		return this.serverAddress;
	}
	
	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	public Set<String> getPlayers() {
		return Collections.unmodifiableSet(this.players);
	}
	
	public void replacePlayers(Set<String> players) {
		this.players = players;
	}

	public int getPlayerMaximum() {
		return this.playerMaximum;
	}
	
	public void setPlayerMaximum(int playerMaximum) {
		this.playerMaximum = playerMaximum;
	}

	public String getVersion() {
		return this.version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}

	public boolean redirect(String name, IServer server) {
		throw new UnsupportedOperationException();
	}

}
