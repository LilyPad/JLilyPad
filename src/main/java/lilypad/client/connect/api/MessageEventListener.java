package lilypad.client.connect.api;

public interface MessageEventListener {

	/**
	 * Called when a message has been received by the session.
	 * 
	 * @param connect session that the event belongs to
	 * @param messageEvent
	 */
	public void onMessage(Connect connect, MessageEvent messageEvent);
	
}
