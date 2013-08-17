package lilypad.server.proxy.http;

public interface HttpGetClientListener {

	public void httpResponse(HttpGetClient httpClient, String response);
	
	public void exceptionCaught(HttpGetClient httpClient, Throwable throwable);
	
}
