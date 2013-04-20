package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetDetailsResult;

public class GetDetailsRequest implements Request<GetDetailsResult> {

	public Class<GetDetailsResult> getResult() {
		return GetDetailsResult.class;
	}

}
