package lilypad.server.proxy.net;

public enum ProxyState {

	DISCONNECTED,
	STATUS,
	STATUS_PING,
	LOGIN,
	LOGIN_ENCRYPT,
	VERIFY,
	INIT,
	CONNECTED;
	
}
