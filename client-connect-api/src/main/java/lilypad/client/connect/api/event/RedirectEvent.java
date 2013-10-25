package lilypad.client.connect.api.event;

/**
 * Called when a player is to be redirected to a specified
 * server by the session when the session is the role of a proxy.
 */
public class RedirectEvent extends Event {

	private String server;
	private String player;
	
	/**
	 * 
	 * @param server the server to redirect to
	 * @param player the player to be redirected
	 */
	public RedirectEvent(String server, String player) {
		this.server = server;
		this.player = player;
	}

	/**
	 * 
	 * @return the server to redirect to
	 */
	public String getServer() {
		return this.server;
	}
	
	/**
	 * 
	 * @return the player to be redirected
	 */
	public String getPlayer() {
		return this.player;
	}
	
}
