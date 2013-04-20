package lilypad.server.proxy.net;

public enum ProxySessionState {

	DISCONNECTED,
	ENCRYPT_REQUEST,
	VERIFYING,
	DIRECTING,
	CONNECTED;
	
}
