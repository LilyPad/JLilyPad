package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetKeyResult extends Result {

	private String key;
	
	public GetKeyResult(String key) {
		this(StatusCode.SUCCESS);
		this.key = key;
	}
	
	public GetKeyResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	public String getKey() {
		return this.key;
	}
	
}
