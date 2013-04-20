package lilypad.client.connect.api;

import java.io.UnsupportedEncodingException;

public class MessageEvent {

	private String sender;
	private String channel;
	private byte[] message;
	
	public MessageEvent(String sender, String channel, byte[] message) {
		this.sender = sender;
		this.channel = channel;
		this.message = message;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public byte[] getMessage() {
		return this.message;
	}
	
	public String getMessageAsString() throws UnsupportedEncodingException {
		return new String(this.message, "UTF-8");
	}
	
}
