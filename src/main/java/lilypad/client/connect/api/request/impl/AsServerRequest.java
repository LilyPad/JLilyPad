package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AsServerResult;

/**
 * Request to be assigned the role of a server on the network.
 */
public class AsServerRequest implements Request<AsServerResult> {
	
	private String ip;
	private int port;
	
	/**
	 * Shortcut to dictate that the network should assume it's ip
	 * address.
	 * 
	 * @param port of the server
	 */
	public AsServerRequest(int port) {
		this(null, port);
	}
	
	/**
	 * 
	 * @param ip of the server, null if the network should assume 
	 * it's ip address
	 * @param port of the server
	 */
	public AsServerRequest(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<AsServerResult> getResult() {
		return AsServerResult.class;
	}
	
	/**
	 * 
	 * @return the ip address of the server, null if the network
	 * should assume it's ip address
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * 
	 * @return the port number of the server
	 */
	public int getPort() {
		return this.port;
	}

}
