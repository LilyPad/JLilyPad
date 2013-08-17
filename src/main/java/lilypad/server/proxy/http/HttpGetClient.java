package lilypad.server.proxy.http;

public interface HttpGetClient extends Runnable {

	public void registerListener(HttpGetClientListener listener);
	
	public void unregisterListener(HttpGetClientListener listener);
	
}
