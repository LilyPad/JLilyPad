package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetDetailsResult extends Result {

	private String ip;
	private int port;
	private String motd;
	private String version;
	
	public GetDetailsResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	public GetDetailsResult(String ip, int port, String motd, String version) {
		super(StatusCode.SUCCESS);
		this.ip = ip;
		this.port = port;
		this.motd = motd;
		this.version = version;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getMotd() {
		return this.motd;
	}
	
	public String getVersion() {
		return this.version;
	}

}
