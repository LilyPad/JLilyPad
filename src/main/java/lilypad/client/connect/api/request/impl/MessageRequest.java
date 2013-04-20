package lilypad.client.connect.api.request.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.MessageResult;

public class MessageRequest implements Request<MessageResult> {

	private List<String> usernames;
	private String channel;
	private byte[] message;
	
	public MessageRequest(String username, String channel, String message) throws UnsupportedEncodingException {
		this(username, channel, message.getBytes("UTF-8"));
	}
	
	public MessageRequest(List<String> usernames, String channel, String message) throws UnsupportedEncodingException {
		this(usernames, channel, message.getBytes("UTF-8"));
	}
	
	public MessageRequest(String username, String channel, byte[] message) {
		this(username == null || username.length() == 0 ? new ArrayList<String>() : Arrays.asList(username), channel, message);
	}
	
	public MessageRequest(List<String> usernames, String channel, byte[] message) {
		this.usernames = usernames;
		this.channel = channel;
		this.message = message;
	}

	public Class<MessageResult> getResult() {
		return MessageResult.class;
	}
	
	public List<String> getUsernames() {
		return this.usernames;
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
