package lilypad.server.proxy;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.util.Map;

import lilypad.server.common.config.IConfig;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.IServerSource;

public interface ProxyConfig extends IConfig {
	
	public InetSocketAddress proxy_getBindAddress();
	
	public InetSocketAddress proxy_getOutboundAddress();
	
	public IPlayerCallback proxy_getPlayerCallback();

	public KeyPair proxy_getKeyPair();
	
	public String proxy_getPlayerMotd();
	
	public int proxy_getPlayerMaximum();
	
	public long proxy_getPlayerThrottle();
	
	public boolean proxy_isPlayerTab();
	
	public boolean proxy_isPlayerAuthenticate();
	
	public Map<String, String> proxy_getDomains();
	
	public IServerSource proxy_getServerSource();
	
	public String proxy_getLocaleFull();
	
	public String proxy_getLocaleOffline();
	
	public String proxy_getLocaleLoggedIn();
	
	public String proxy_getLocaleLostConn();
	
	public String proxy_getLocaleShutdown();
	
}
