package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetWhoamiResult;

public class GetWhoamiRequest implements Request<GetWhoamiResult> {

	public Class<GetWhoamiResult> getResult() {
		return GetWhoamiResult.class;
	}

}
