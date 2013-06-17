package lilypad.client.connect.api;

import java.net.InetSocketAddress;

public interface ConnectSettings {

	/**
	 * 
	 * @return the address of the remote network's server
	 */
	public InetSocketAddress getOutboundAddress();
	
	/**
	 * 
	 * @return the username to be authenticated with on the network
	 */
	public String getUsername();
	
	/**
	 * 
	 * @return the password to be authenticated with on the network
	 */
	public String getPassword();
	
}
