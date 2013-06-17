package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class NotifyPlayerResult extends Result {
	
	/**
	 * 
	 * @param statusCode of the result
	 */
	public NotifyPlayerResult(StatusCode statusCode) {
		super(statusCode);
	}

}
