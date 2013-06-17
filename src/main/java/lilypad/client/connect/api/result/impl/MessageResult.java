package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class MessageResult extends Result {

	/**
	 * 
	 * @param statusCode of the result
	 */
	public MessageResult(StatusCode statusCode) {
		super(statusCode);
	}
	
}
