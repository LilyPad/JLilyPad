package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetWhoamiResult;

/**
 * Request to get the current identification the network
 * recognizes your session as.
 */
public class GetWhoamiRequest implements Request<GetWhoamiResult> {

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<GetWhoamiResult> getResult() {
		return GetWhoamiResult.class;
	}

}
