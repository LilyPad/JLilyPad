package lilypad.server.standalone.proxy;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.ServerAddEvent;
import lilypad.client.connect.api.event.ServerRemoveEvent;
import lilypad.server.common.IServer;
import lilypad.server.common.IServerSource;

public class ConnectServerSource implements IServerSource {

	private Map<String, ConnectServer> servers = new ConcurrentHashMap<String, ConnectServer>();
	private Connect connect;
	
	public ConnectServerSource(Connect connect) {
		this.connect = connect;
	}
	
	public IServer getServerByName(String username) {
		return this.servers.get(username);
	}

	@EventListener
	public void onServerAdd(ServerAddEvent event) {
		String address = event.getAddress().getAddress().getHostAddress();
		if(address.equals("127.0.0.1") || address.equals("localhost")) {
			this.servers.put(event.getServer(), new ConnectServer(event.getServer(), new InetSocketAddress(this.connect.getSettings().getOutboundAddress().getHostName(), event.getAddress().getPort()), event.getSecurityKey()));
		} else {
			this.servers.put(event.getServer(), new ConnectServer(event.getServer(), event.getAddress(), event.getSecurityKey()));
		}
	}

	@EventListener
	public void onServerRemove(ServerRemoveEvent event) {
		this.servers.remove(event.getServer());
	}

	public void clearServers() {
		this.servers.clear();
	}

}
