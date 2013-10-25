package lilypad.client.connect.api.request;

import lilypad.client.connect.api.result.Result;

public interface Request<T extends Result> {

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<T> getResult();
	
}
