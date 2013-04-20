package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AsServerResult;

public class AsServerRequest implements Request<AsServerResult> {
	
	private String ip;
	private int port;
	
	public AsServerRequest(int port) {
		this.port = port;
	}
	
	public AsServerRequest(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public Class<AsServerResult> getResult() {
		return AsServerResult.class;
	}
	
	public String getIp() {
		return this.ip;
	}
	
	public int getPort() {
		return this.port;
	}

}
