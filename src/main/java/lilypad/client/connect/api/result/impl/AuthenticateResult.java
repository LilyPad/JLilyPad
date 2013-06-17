package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class AuthenticateResult extends Result {

	/**
	 * 
	 * @param statusCode of the result
	 */
	public AuthenticateResult(StatusCode statusCode) {
		super(statusCode);
	}

}
