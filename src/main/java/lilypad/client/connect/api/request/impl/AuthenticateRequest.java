package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AuthenticateResult;
import lilypad.client.connect.api.util.SecurityUtils;

/**
 * Request to authenticate with the network.
 */
public class AuthenticateRequest implements Request<AuthenticateResult> {

	private String username;
	private String password;
	
	/**
	 * 
	 * @param username to be authenticated with
	 * @param password to be authenticated with
	 * @param passwordKey salt to hash the password with
	 */
	public AuthenticateRequest(String username, String password, String passwordKey) {
		this.username = username;
		this.password = SecurityUtils.shaHex(SecurityUtils.shaHex(passwordKey) + SecurityUtils.shaHex(password));
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<AuthenticateResult> getResult() {
		return AuthenticateResult.class;
	}
	
	/**
	 * 
	 * @return username to be authenticated with
	 */
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * 
	 * @return password to be authenticated with
	 */
	public String getPassword() {
		return this.password;
	}

}
