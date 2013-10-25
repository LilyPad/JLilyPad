package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetDetailsResult;

/**
 * Request to get the uniform connection details of the network,
 * not guaranteed to be an accurate representation.
 */
public class GetDetailsRequest implements Request<GetDetailsResult> {

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<GetDetailsResult> getResult() {
		return GetDetailsResult.class;
	}

}
