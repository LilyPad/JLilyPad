package lilypad.client.connect.api.result;

public abstract class Result {

	private StatusCode statusCode;
	
	/**
	 * 
	 * @param statusCode of the result
	 */
	public Result(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
	/**
	 * Showing how the request was handled by the network, namely
	 * if it succeeded or failed.
	 * 
	 * @return status code
	 */
	public StatusCode getStatusCode() {
		return this.statusCode;
	}
	
		
}
