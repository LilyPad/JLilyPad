package lilypad.client.connect.api;

@Deprecated
public class RedirectEvent extends lilypad.client.connect.api.event.RedirectEvent {

	/**
	 * Create a legacy redirect event from a new redirect event.
	 * 
	 * @param event
	 */
	public RedirectEvent(lilypad.client.connect.api.event.RedirectEvent event) {
		super(event.getServer(), event.getPlayer());
	}
	
	/**
	 * 
	 * @param server the server to redirect to
	 * @param player the player to be redirected
	 */
	public RedirectEvent(String server, String player) {
		super(server, player);
	}

}
