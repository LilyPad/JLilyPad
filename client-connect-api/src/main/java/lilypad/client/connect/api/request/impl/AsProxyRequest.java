package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AsProxyResult;

/**
 * Request to be assigned the role of a proxy on the network.
 */
public class AsProxyRequest implements Request<AsProxyResult> {
	
	private String ip;
	private int port;
	private String motd;
	private String version;
	private int maximumPlayers;
	
	/**
	 * Shortcut to dictate that the network should assume it's ip
	 * address.
	 * 
	 * @param port of this specific proxy
	 * @param motd of this specific proxy
	 * @param version of this specific proxy
	 * @param maximumPlayers of this specific proxy
	 */
	public AsProxyRequest(int port, String motd, String version, int maximumPlayers) {
		this(null, port, motd, version, maximumPlayers);
	}
	
	/**
	 * 
	 * @param ip of this specific proxy, null if the network should 
	 * assume it's ip address
	 * @param port of this specific proxy
	 * @param motd of this specific proxy
	 * @param version of this specific proxy
	 * @param maximumPlayers of this specific proxy
	 */
	public AsProxyRequest(String ip, int port, String motd, String version, int maximumPlayers) {
		this.ip = ip;
		this.port = port;
		this.motd = motd;
		this.version = version;
		this.maximumPlayers = maximumPlayers;
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<AsProxyResult> getResult() {
		return AsProxyResult.class;
	}
	
	/**
	 * 
	 * @return the ip address of this specific proxy, null if the
	 * network should assume it's ip address
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * 
	 * @return the port number of this specific proxy
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * 
	 * @return the motd of this specific proxy
	 */
	public String getMotd() {
		return this.motd;
	}
	
	/**
	 * 
	 * @return the version of this specific proxy
	 */
	public String getVersion() {
		return this.version;
	}
	
	/**
	 * 
	 * @return the maximum players of this specific proxy
	 */
	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}

}
