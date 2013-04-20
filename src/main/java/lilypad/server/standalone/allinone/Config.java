package lilypad.server.standalone.allinone;

import java.security.KeyPair;
import java.util.Map;

import lilypad.server.common.util.SecurityUtils;
import lilypad.server.connect.ConnectConfig;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.query.tcp.QueryTcpConfig;
import lilypad.server.query.udp.QueryUdpConfig;
import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.common.IServerSource;

public abstract class Config implements QueryTcpConfig, QueryUdpConfig, ConnectConfig, ProxyConfig, IAuthenticator {

	private transient IPlayable playable;
	private transient IServerSource serverSource;
	private transient KeyPair keyPair;
	
	public void init(IPlayable playable, IServerSource serverSource, KeyPair keyPair) {
		this.playable = playable;
		this.serverSource = serverSource;
		this.keyPair = keyPair;
	}

	public IPlayable querytcp_getPlayable() {
		return this.playable;
	}
	
	public IPlayable queryudp_getPlayable() {
		return this.playable;
	}
	
	public IAuthenticator connect_getAuthenticator() {
		return this;
	}
	
	public IPlayable connect_getPlayable() {
		return this.playable;
	}
	
	public abstract Map<String, String> connect_getLogins();
	
	public IPlayerCallback proxy_getPlayerCallback() {
		return null;
	}
	
	public KeyPair proxy_getKeyPair() {
		return this.keyPair;
	}
	
	public IServerSource proxy_getServerSource() {
		return this.serverSource;
	}

	public boolean authenticate(String username, String password, String authenticationKey) {
		Map<String, String> logins = this.connect_getLogins();
		if(!logins.containsKey(username)) {
			return false;
		}
		String realPassword = SecurityUtils.shaHex(SecurityUtils.shaHex(authenticationKey) + SecurityUtils.shaHex(logins.get(username)));
		if(!realPassword.equals(password)) {
			return false;
		}
		return true;
	}

}
