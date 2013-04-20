package lilypad.client.connect.api.request;

import lilypad.client.connect.api.result.Result;

public interface Request<T extends Result> {

	public Class<T> getResult();
	
}
