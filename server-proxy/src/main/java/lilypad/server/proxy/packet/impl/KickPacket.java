package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class KickPacket extends Packet {

	public static final int opcode = 0xFF;
	
	private String message;

	public KickPacket(String message) {
		super(opcode);
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
