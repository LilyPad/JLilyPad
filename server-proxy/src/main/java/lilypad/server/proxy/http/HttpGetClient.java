package lilypad.server.proxy.http;

public interface HttpGetClient {

	public void run();
	
	public boolean isRunning();
	
	public void close();
	
	public void registerListener(HttpGetClientListener listener);
	
	public void unregisterListener(HttpGetClientListener listener);
	
}
