package lilypad.packet.connect.impl;

import lilypad.packet.common.Packet;

public class ServerPacket extends Packet {

	public static final int opcode = 0x05;
	
	private String server;
	
	public ServerPacket(String server) {
		super(opcode);
		this.server = server;
	}
	
	public boolean isAdding() {
		return false;
	}
	
	public String getServer() {
		return this.server;
	}
	
}
