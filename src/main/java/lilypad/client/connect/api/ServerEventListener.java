package lilypad.client.connect.api;

public interface ServerEventListener {

	/**
	 * Called when a server has been added to the network
	 * when the session is the role of a proxy.
	 * 
	 * @param connect session that the event belongs to
	 * @param event
	 */
	public void onServerAdd(Connect connect, ServerAddEvent event);
	
	/**
	 * Called when a server has been removed from the network
	 * when the session is the role of a proxy.
	 * 
	 * @param connect session that the event belongs to
	 * @param server that has been removed
	 */
	public void onServerRemove(Connect connect, String server);
	
}
