package lilypad.server.proxy.net;

public enum ProxyOutboundHandlerState {

	DISCONNECTED,
	ENCRYPT_REQUEST,
	BUFFERING,
	CONNECTED;
	
}
