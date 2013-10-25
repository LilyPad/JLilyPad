package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetDetailsResult extends Result {

	private String ip;
	private int port;
	private String motd;
	private String version;
	
	/**
	 * Called only when the result is unsuccessful.
	 * 
	 * @param statusCode of the result
	 */
	public GetDetailsResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	/**
	 * Showing the result was successful, passing the data
	 * to accompany the result.
	 * 
	 * @param ip of the result
	 * @param port of the result
	 * @param motd of the result
	 * @param version of the result
	 */
	public GetDetailsResult(String ip, int port, String motd, String version) {
		super(StatusCode.SUCCESS);
		this.ip = ip;
		this.port = port;
		this.motd = motd;
		this.version = version;
	}
	
	/**
	 * Uniform ip address decided by the network deriving from a single
	 * proxy. There is no guarantee which proxy this ip address derives from.
	 * 
	 * @return ip address
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * Uniform port number decided by the network deriving from a single
	 * proxy. There is no guarantee which proxy this port number derives from.
	 * 
	 * @return port number
	 */
	public int getPort() {
		return this.port;
	}
	
	/**
	 * Uniform motd decided by the network deriving from a single proxy.
	 * There is no guarantee which proxy this motd derives from.
	 * 
	 * @return motd
	 */
	public String getMotd() {
		return this.motd;
	}
	
	/**
	 * Uniform version decided by the network deriving from a single proxy.
	 * There is no guarantee which proxy this version derives from.
	 * 
	 * @return motd
	 */
	public String getVersion() {
		return this.version;
	}

}
