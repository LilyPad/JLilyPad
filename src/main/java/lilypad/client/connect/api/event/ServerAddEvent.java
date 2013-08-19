package lilypad.client.connect.api.event;

import java.net.InetSocketAddress;

/**
 * Called when a server has been added to the network
 * when the session is the role of a proxy.
 */
public class ServerAddEvent extends Event {

	private String server;
	private String securityKey;
	private InetSocketAddress address;
	
	/**
	 * 
	 * @param server the server that has been added
	 * @param securityKey the security key of the server
	 * @param address the connection address of the server
	 */
	public ServerAddEvent(String server, String securityKey, InetSocketAddress address) {
		this.server = server;
		this.securityKey = securityKey;
		this.address = address;
	}
	
	/**
	 * 
	 * @return the server that has been added
	 */
	public String getServer() {
		return this.server;
	}
	
	/**
	 * 
	 * @return the security key of the server
	 */
	public String getSecurityKey() {
		return this.securityKey;
	}
	
	/**
	 * 
	 * @return the connection address of the server
	 */
	public InetSocketAddress getAddress() {
		return this.address;
	}
	
}
