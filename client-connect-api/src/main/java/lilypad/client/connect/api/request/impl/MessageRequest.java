package lilypad.client.connect.api.request.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.MessageResult;

/**
 * Request to have a message sent to a single or multiple other
 * identifications on the network.
 */
public class MessageRequest implements Request<MessageResult> {

	private List<String> recipients;
	private String channel;
	private byte[] message;
	
	/**
	 * 
	 * @param recipient single recipient, if null or blank will be
	 * a broadcast message
	 * @param channel to identify the message
	 * @param message encoded as a UTF-8 string
	 */
	public MessageRequest(String recipient, String channel, String message) throws UnsupportedEncodingException {
		this(recipient, channel, message.getBytes("UTF-8"));
	}
	
	/**
	 * 
	 * @param recipients list of all recipients, if blank will be
	 * a broadcast message
	 * @param channel to identify the message
	 * @param message encoded as a UTF-8 string
	 */
	public MessageRequest(List<String> recipients, String channel, String message) throws UnsupportedEncodingException {
		this(recipients, channel, message.getBytes("UTF-8"));
	}
	
	/**
	 * 
	 * @param recipient single recipient, if null or blank will be
	 * a broadcast message
	 * @param channel to identify the message
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public MessageRequest(String recipient, String channel, byte[] message) {
		this(recipient == null || recipient.length() == 0 ? Collections.EMPTY_LIST : Arrays.asList(recipient), channel, message);
	}
	
	/**
	 * 
	 * @param recipients list of all recipients, if blank will be
	 * a broadcast message
	 * @param channel to identify the message
	 * @param message
	 */
	public MessageRequest(List<String> recipients, String channel, byte[] message) {
		this.recipients = recipients;
		this.channel = channel;
		this.message = message;
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<MessageResult> getResult() {
		return MessageResult.class;
	}
	
	/**
	 * 
	 * @return list of all recipients
	 */
	public List<String> getRecipients() {
		return this.recipients;
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
