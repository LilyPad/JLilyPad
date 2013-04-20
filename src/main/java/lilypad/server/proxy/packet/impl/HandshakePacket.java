package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class HandshakePacket extends Packet {

	public static final int opcode = 0x02;
	
	private int protocolVersion;
	private String username;
	private String serverHost;
	private int serverPort;
	
	public HandshakePacket(int protocolVersion, String username, String serverHost, int serverPort) {
		super(opcode);
		this.protocolVersion = protocolVersion;
		this.username = username;
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}
	
	public int getProtocolVersion() {
		return this.protocolVersion;
	}
	
	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getServerHost() {
		return this.serverHost;
	}
	
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}
	
	public int getServerPort() {
		return this.serverPort;
	}
	
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
