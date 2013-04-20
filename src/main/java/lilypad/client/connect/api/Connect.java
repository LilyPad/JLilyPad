package lilypad.client.connect.api;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.Result;

public interface Connect {

	public void connect() throws Throwable;
	
	public void disconnect();
	
	public void close();
	
	public <T extends Result> FutureResult<T> request(Request<T> request) throws RequestException;

	public void registerMessageEventListener(MessageEventListener messageEventListener);
	
	public void unregisterMessageEventListener(MessageEventListener messageEventListener);
	
	public void registerRedirectEventListener(RedirectEventListener redirectEventListener);
	
	public void unregisterRedirectEventListener(RedirectEventListener redirectEventListener);
	
	public void registerServerEventListener(ServerEventListener redirectEventListener);
	
	public void unregisterServerEventListener(ServerEventListener redirectEventListener);
	
	public boolean isConnected();
	
	public boolean isClosed();
	
	public ConnectSettings getSettings();
	
}
