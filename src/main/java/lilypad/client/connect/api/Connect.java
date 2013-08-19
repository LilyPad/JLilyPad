package lilypad.client.connect.api;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.Result;

public interface Connect {

	/**
	 * Connect the server to it's network.
	 * 
	 * @throws Throwable showing the connection failed
	 */
	public void connect() throws Throwable;
	
	/**
	 * Disconnect the server from it's network.
	 */
	public void disconnect();
	
	/**
	 * Close to not allow for anymore connections nor disconnections.
	 */
	public void close();
	
	/**
	 * Pass a request to be completed and given a result by the
	 * network.
	 * 
	 * @param request
	 * @return the FutureResult to show that the request will be completed in the future
	 * @throws RequestException if the request failed
	 */
	public <T extends Result> FutureResult<T> request(Request<T> request) throws RequestException;

	/**
	 * Register event listener methods dictated by the EventListener
	 * annotation.
	 * 
	 * @param object
	 */
	public void registerEvents(Object object);
	
	/**
	 * Unregister event listener methods dictated by the EventListener
	 * annotation.
	 * 
	 * @param object
	 */
	public void unregisterEvents(Object object);
	
	/**
	 * Register a MessageEventListener to receive an events when
	 * a message has been received by this specific session.
	 * 
	 * @param messageEventListener
	 */
	@Deprecated
	public void registerMessageEventListener(MessageEventListener messageEventListener);
	
	/**
	 * Unregister a MessageEventListener to exclude from receiving
	 * events when a message has been received by this specific session.
	 * 
	 * @param messageEventListener
	 */
	@Deprecated
	public void unregisterMessageEventListener(MessageEventListener messageEventListener);
	
	/**
	 * Register a RedirectEventListener to receive an events when
	 * a redirect is to be handled by this specific session, only
	 * used when the session's role is that of a proxy.
	 * 
	 * @param redirectEventListener
	 */
	@Deprecated
	public void registerRedirectEventListener(RedirectEventListener redirectEventListener);
	
	/**
	 * Unregister a RedirectEventListener to exclude from receiving 
	 * events when a redirect is to be handled by this specific session.
	 * 
	 * @param redirectEventListener
	 */
	@Deprecated
	public void unregisterRedirectEventListener(RedirectEventListener redirectEventListener);
	
	/**
	 * Register a ServerEventListener to receive an events when
	 * a server has been added or removed from the network, only
	 * used when the session's role is that of a proxy.
	 * 
	 * @param serverEventListener
	 */
	@Deprecated
	public void registerServerEventListener(ServerEventListener serverEventListener);
	
	/**
	 * Unregister a ServerEventListener to exclude from receiving
	 * events when a server has been added or removed from the network.
	 * 
	 * @param serverEventListener
	 */
	@Deprecated
	public void unregisterServerEventListener(ServerEventListener serverEventListener);
	
	/**
	 * 
	 * @return if this session is connected
	 */
	public boolean isConnected();
	
	/**
	 * 
	 * @return if this session is closed
	 */
	public boolean isClosed();
	
	/**
	 * 
	 * @return the settings, not guaranteed to be an accurate representation
	 */
	public ConnectSettings getSettings();
	
}
