package lilypad.client.connect.api.result;

public interface FutureResultListener<T extends Result> {

	/**
	 * Called when a result has been received for a registered
	 * FutureResult.
	 * 
	 * @param result of the request
	 * @see FutureResult
	 */
	public void onResult(T result);
	
}
