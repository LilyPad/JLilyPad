package lilypad.server.proxy.net;

public enum LoginState {

	DISCONNECTED,
	ENCRYPT_REQUEST,
	AUTHENTICATE,
	INITIALIZE,
	CONNECTED;
	
}
