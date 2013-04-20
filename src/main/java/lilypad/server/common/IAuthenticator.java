package lilypad.server.common;

public interface IAuthenticator {

	public boolean authenticate(String username, String password, String authenticationKey);
	
}
