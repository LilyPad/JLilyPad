package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetKeyResult;

/**
 * Request to receive a shared salt to be used within the
 * authentication process.
 */
public class GetKeyRequest implements Request<GetKeyResult> {

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<GetKeyResult> getResult() {
		return GetKeyResult.class;
	}

}
