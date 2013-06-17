package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class RedirectResult extends Result {

	/**
	 * 
	 * @param statusCode of the result
	 */
	public RedirectResult(StatusCode statusCode) {
		super(statusCode);
	}

}
