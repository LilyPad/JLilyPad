package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.AsProxyResult;

public class AsProxyRequest implements Request<AsProxyResult> {
	
	private String ip;
	private int port;
	private String motd;
	private String version;
	private int maximumPlayers;
	
	public AsProxyRequest(int port, String motd, String version, int maximumPlayers) {
		this.port = port;
		this.motd = motd;
		this.version = version;
		this.maximumPlayers = maximumPlayers;
	}
	
	public AsProxyRequest(String ip, int port, String motd, String version, int maximumPlayers) {
		this.ip = ip;
		this.port = port;
		this.motd = motd;
		this.version = version;
		this.maximumPlayers = maximumPlayers;
	}

	public Class<AsProxyResult> getResult() {
		return AsProxyResult.class;
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
	
	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}

}
