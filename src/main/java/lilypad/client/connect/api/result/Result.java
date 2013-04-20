package lilypad.client.connect.api.result;

public abstract class Result {

	private StatusCode statusCode;
	
	public Result(StatusCode statusCode) {
		this.statusCode = statusCode;
	}
	
	public StatusCode getStatusCode() {
		return this.statusCode;
	}
	
		
}
