package lilypad.server.connect;

import java.net.InetSocketAddress;

import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;
import lilypad.server.common.config.IConfig;

public interface ConnectConfig extends IConfig {

	public InetSocketAddress connect_getBindAddress();
	
	public IAuthenticator connect_getAuthenticator();
	
	public IPlayable connect_getPlayable();
	
}
