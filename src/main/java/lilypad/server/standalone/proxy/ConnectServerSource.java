package lilypad.server.standalone.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.ServerAddEvent;
import lilypad.client.connect.api.ServerEventListener;
import lilypad.server.common.IServer;
import lilypad.server.common.IServerSource;

public class ConnectServerSource implements IServerSource, ServerEventListener {

	private Map<String, ConnectServer> servers = new ConcurrentHashMap<String, ConnectServer>();

	public IServer getServerByName(String username) {
		return this.servers.get(username);
	}

	public void onServerAdd(Connect connect, ServerAddEvent event) {
		this.servers.put(event.getServer(), new ConnectServer(event.getServer(), event.getAddress(), event.getSecurityKey()));
	}

	public void onServerRemove(Connect connect, String server) {
		this.servers.remove(server);
	}

	public void clearServers() {
		this.servers.clear();
	}

}
