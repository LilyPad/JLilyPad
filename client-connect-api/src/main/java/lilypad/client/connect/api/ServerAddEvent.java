package lilypad.client.connect.api;

import java.net.InetSocketAddress;

@Deprecated
public class ServerAddEvent extends lilypad.client.connect.api.event.ServerAddEvent {

	/**
	 * Create a legacy server add event from a new server add event.
	 * 
	 * @param event
	 */
	public ServerAddEvent(lilypad.client.connect.api.event.ServerAddEvent event) {
		super(event.getServer(), event.getSecurityKey(), event.getAddress());
	}
	
	/**
	 * 
	 * @param server the server that has been added
	 * @param securityKey the security key of the server
	 * @param address the connection address of the server
	 */
	public ServerAddEvent(String server, String securityKey, InetSocketAddress address) {
		super(server, securityKey, address);
	}

}
