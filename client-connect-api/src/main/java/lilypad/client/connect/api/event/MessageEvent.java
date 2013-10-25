package lilypad.client.connect.api.event;

import java.io.UnsupportedEncodingException;

/**
 * Called when a message has been received by the session.
 */
public class MessageEvent extends Event {

	private String sender;
	private String channel;
	private byte[] message;
	
	/**
	 * 
	 * @param sender of the event
	 * @param channel of the event
	 * @param message of the event
	 */
	public MessageEvent(String sender, String channel, byte[] message) {
		this.sender = sender;
		this.channel = channel;
		this.message = message;
	}
	
	/**
	 * 
	 * @return sender of the message
	 */
	public String getSender() {
		return this.sender;
	}
	
	/**
	 * 
	 * @return channel to identify the message
	 */
	public String getChannel() {
		return this.channel;
	}
	
	/**
	 * 
	 * @return message
	 */
	public byte[] getMessage() {
		return this.message;
	}
	
	/**
	 * 
	 * @return message represented as a UTF-8 string
	 * @throws UnsupportedEncodingException if the message can not be represented as a UTF-8 string
	 */
	public String getMessageAsString() throws UnsupportedEncodingException {
		return new String(this.message, "UTF-8");
	}
	
}
