package lilypad.client.connect.api;

@Deprecated
public interface RedirectEventListener {

	/**
	 * Called when a player is to be redirected to a specified
	 * server by the session when the session is the role of a proxy.
 	 * 
	 * @param connect session that the event belongs to
	 * @param redirectEvent
	 */
	public void onRedirect(Connect connect, RedirectEvent redirectEvent);
	
}
