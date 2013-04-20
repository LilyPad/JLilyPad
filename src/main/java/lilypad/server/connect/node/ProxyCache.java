package lilypad.server.connect.node;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyCache {

	public static final InetSocketAddress blankAddress = new InetSocketAddress("0.0.0.0", 0);

	private Map<String, NodeSession> playersToProxy = new ConcurrentHashMap<String, NodeSession>();

	private List<InetSocketAddress> address = new ArrayList<InetSocketAddress>();
	private InetSocketAddress addressShown;
	private List<String> motd = new ArrayList<String>();
	private String motdShown;
	private List<String> version = new ArrayList<String>();
	private String versionShown;
	private List<Integer> maximumPlayers = new ArrayList<Integer>();
	private int maximumPlayersShown;

	public ProxyCache() {
		this.rebuild();
	}

	public void registerProxy(NodeSession nodeSession) {
		this.address.add(nodeSession.getInboundAddress());
		this.motd.add(nodeSession.getMotd());
		this.version.add(nodeSession.getVersion());
		this.maximumPlayers.add(nodeSession.getMaximumPlayers());
		this.rebuild();
	}

	public void unregisterProxy(NodeSession nodeSession) {
		this.removePlayersByProxy(nodeSession);
		this.address.remove(nodeSession.getInboundAddress());
		this.motd.remove(nodeSession.getMotd());
		this.version.remove(nodeSession.getVersion());
		this.maximumPlayers.remove((Integer) nodeSession.getMaximumPlayers());
		this.rebuild();
	}

	public void rebuild() {
		if(this.address.isEmpty()) {
			this.addressShown = blankAddress;
		} else {
			this.addressShown = this.address.get(0);
		}
		if(this.motd.isEmpty()) {
			this.motdShown = "Unknown";
		} else {
			this.motdShown = this.motd.get(0);
		}
		if(this.version.isEmpty()) {
			this.versionShown = "Unknown";
		} else {
			this.versionShown = this.version.get(0);
		}
		this.maximumPlayersShown = 0;
		for(int maximumPlayers : this.maximumPlayers) {
			if(maximumPlayers == 1) {
				this.maximumPlayersShown = 1;
				break;
			}
			this.maximumPlayersShown += maximumPlayers;
		}
	}

	public Set<String> getPlayers() {
		return Collections.unmodifiableSet(this.playersToProxy.keySet());
	}

	public NodeSession getProxyByPlayer(String player) {
		return this.playersToProxy.get(player);
	}

	public boolean addPlayer(String player, NodeSession server) {
		if(this.playersToProxy.containsKey(player)) {
			return false;
		}
		this.playersToProxy.put(player, server);
		return true;
	}

	public void removePlayer(String player) {
		this.playersToProxy.remove(player);
	}

	public void removePlayersByProxy(NodeSession server) {
		for(String player : server.getPlayers()) {
			this.removePlayer(player);
		}
	}

	public InetSocketAddress getAddress() {
		return this.addressShown;
	}

	public String getMotd() {
		return this.motdShown;
	}

	public String getVersion() {
		return this.versionShown;
	}

	public int getMaximumPlayers() {
		return this.maximumPlayersShown;
	}

}
