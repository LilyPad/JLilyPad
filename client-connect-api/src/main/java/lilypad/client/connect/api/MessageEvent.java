package lilypad.client.connect.api;

@Deprecated
public class MessageEvent extends lilypad.client.connect.api.event.MessageEvent {

	/**
	 * Create a legacy message event from a new message event.
	 * 
	 * @param event
	 */
	public MessageEvent(lilypad.client.connect.api.event.MessageEvent event) {
		super(event.getSender(), event.getChannel(), event.getMessage());
	}
	
	/**
	 * 
	 * @param sender of the event
	 * @param channel of the event
	 * @param message of the event
	 */
	public MessageEvent(String sender, String channel, byte[] message) {
		super(sender, channel, message);
	}

}
