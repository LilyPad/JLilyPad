package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetKeyResult;

public class GetKeyRequest implements Request<GetKeyResult> {

	public Class<GetKeyResult> getResult() {
		return GetKeyResult.class;
	}

}
