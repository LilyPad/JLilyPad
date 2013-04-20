package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetPlayersResult;

public class GetPlayersRequest implements Request<GetPlayersResult> {

	private boolean asList;
	
	public GetPlayersRequest() {
		this(false);
	}
	
	public GetPlayersRequest(boolean asList) {
		this.asList = asList;
	}
	
	public Class<GetPlayersResult> getResult() {
		return GetPlayersResult.class;
	}
	
	public boolean getAsList() {
		return this.asList;
	}

}
