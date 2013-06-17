package lilypad.client.connect.api.request;

public class RequestException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Showing a request failed
	 * 
	 * @param reason of the exception
	 */
	public RequestException(String reason) {
		super(reason);
	}
	
}
