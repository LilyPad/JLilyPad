package lilypad.server.standalone.proxy;

import java.net.InetSocketAddress;

import lilypad.server.common.IServer;

public class ConnectServer implements IServer {

	private String server;
	private InetSocketAddress address;
	private String securityKey;
	
	public ConnectServer(String server, InetSocketAddress address, String securityKey) {
		this.server = server;
		this.address = address;
		this.securityKey = securityKey;
	}
	
	public String getIdentification() {
		return this.server;
	}

	public InetSocketAddress getInboundAddress() {
		return this.address;
	}

	public String getSecurityKey() {
		return this.securityKey;
	}

}
