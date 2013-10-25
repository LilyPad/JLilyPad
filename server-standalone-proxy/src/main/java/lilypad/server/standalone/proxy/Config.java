package lilypad.server.standalone.proxy;

import java.security.KeyPair;

import lilypad.client.connect.api.ConnectSettings;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.IServerSource;

public abstract class Config implements ProxyConfig, ConnectSettings {

	private transient IServerSource serverSource;
	private transient IPlayerCallback playerCallback;
	private transient KeyPair keyPair;
	
	public void init(IServerSource serverSource, IPlayerCallback playerCallback, KeyPair keyPair) {
		this.serverSource = serverSource;
		this.playerCallback = playerCallback;
		this.keyPair = keyPair;
	}
	
	public KeyPair proxy_getKeyPair() {
		return this.keyPair;
	}
	
	public IPlayerCallback proxy_getPlayerCallback() {
		return this.playerCallback;
	}
	
	public IServerSource proxy_getServerSource() {
		return this.serverSource;
	}

}
