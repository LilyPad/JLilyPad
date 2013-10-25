package lilypad.server.standalone.connect;

import java.util.Map;

import lilypad.server.common.util.SecurityUtils;
import lilypad.server.connect.ConnectConfig;
import lilypad.server.common.IAuthenticator;
import lilypad.server.common.IPlayable;

public abstract class Config implements ConnectConfig, IAuthenticator {

	private transient IPlayable playable;
	
	public void init(IPlayable playable) {
		this.playable = playable;
	}
	
	public IAuthenticator connect_getAuthenticator() {
		return this;
	}
	
	public IPlayable connect_getPlayable() {
		return this.playable;
	}
	
	public abstract Map<String, String> connect_getLogins();

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
