package lilypad.client.connect.api;

public interface ServerEventListener {

	public void onServerAdd(Connect connect, ServerAddEvent event);
	
	public void onServerRemove(Connect connect, String server);
	
}
