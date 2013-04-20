package lilypad.client.connect.api;

import java.net.InetSocketAddress;

public class ServerAddEvent {

	private String server;
	private String securityKey;
	private InetSocketAddress address;
	
	public ServerAddEvent(String server, String securityKey, InetSocketAddress address) {
		this.server = server;
		this.securityKey = securityKey;
		this.address = address;
	}
	
	public String getServer() {
		return this.server;
	}
	
	public String getSecurityKey() {
		return this.securityKey;
	}
	
	public InetSocketAddress getAddress() {
		return this.address;
	}
	
}
