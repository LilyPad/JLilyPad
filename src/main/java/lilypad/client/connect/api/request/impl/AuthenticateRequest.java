package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AuthenticateResult;
import lilypad.client.connect.api.util.SecurityUtils;

public class AuthenticateRequest implements Request<AuthenticateResult> {

	private String username;
	private String password;
	
	public AuthenticateRequest(String username, String password, String passwordKey) {
		this.username = username;
		this.password = SecurityUtils.shaHex(SecurityUtils.shaHex(passwordKey) + SecurityUtils.shaHex(password));
	}

	public Class<AuthenticateResult> getResult() {
		return AuthenticateResult.class;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}

}
