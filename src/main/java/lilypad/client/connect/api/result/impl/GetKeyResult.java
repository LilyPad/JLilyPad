package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetKeyResult extends Result {

	private String key;
	
	/**
	 * Called only when the result is unsuccessful.
	 * 
	 * @param statusCode of the result
	 */
	public GetKeyResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	/**
	 * Showing the result was successful, passing the data
	 * to accompany the result.
	 * 
	 * @param key
	 */
	public GetKeyResult(String key) {
		this(StatusCode.SUCCESS);
		this.key = key;
	}
	
	/**
	 * Shared salt used within the authentication process for the password.
	 * 
	 * @return salt
	 */
	public String getKey() {
		return this.key;
	}
	
}
