package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class AsServerResult extends Result {

	private String securityKey;
	
	public AsServerResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	public AsServerResult(String securityKey) {
		super(StatusCode.SUCCESS);
		this.securityKey = securityKey;
	}
	
	public String getSecurityKey() {
		return this.securityKey;
	}
	
}
