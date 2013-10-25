package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class AsServerResult extends Result {

	private String securityKey;
	
	/**
	 * Called only when the result is unsuccessful.
	 * 
	 * @param statusCode of the result
	 */
	public AsServerResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	/**
	 * Showing the result was successful, passing the data
	 * to accompany the result.
	 * 
	 * @param securityKey of the result
	 */
	public AsServerResult(String securityKey) {
		super(StatusCode.SUCCESS);
		this.securityKey = securityKey;
	}
	
	/**
	 * Secret used within the game login process for authorization.
	 * 
	 * @return security key
	 */
	public String getSecurityKey() {
		return this.securityKey;
	}
	
}
