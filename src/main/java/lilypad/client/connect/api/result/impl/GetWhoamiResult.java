package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetWhoamiResult extends Result {

	private String identification;
	
	public GetWhoamiResult(String identification) {
		super(StatusCode.SUCCESS);
		this.identification = identification;
	}
	
	public GetWhoamiResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	public String getIdentification() {
		return this.identification;
	}

}
