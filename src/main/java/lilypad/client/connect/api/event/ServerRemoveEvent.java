package lilypad.client.connect.api.event;

/**
 * Called when a server has been removed from the network
 * when the session is the role of a proxy.
 */
public class ServerRemoveEvent extends Event {

	private String server;
	
	/**
	 * 
	 * @param server the server removed from the network
	 */
	public ServerRemoveEvent(String server) {
		this.server = server;
	}

	/**
	 * 
	 * @return the server removed from the network
	 */
	public String getServer() {
		return this.server;
	}
	
}
