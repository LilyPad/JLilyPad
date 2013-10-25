package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class HandshakePacket extends Packet {

	public static final int opcode = 0x00;
	
	private int protocolVersion;
	private String serverAddress;
	private int serverPort;
	private int requestedState;
	
	public HandshakePacket(int protocolVersion, String serverAddress, int serverPort, int requestedState) {
		super(opcode);
		this.protocolVersion = protocolVersion;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.requestedState = requestedState;
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getServerAddress() {
		return this.serverAddress;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public int getRequestedState() {
		return this.requestedState;
	}
	
}
