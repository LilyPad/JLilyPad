package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayDisconnectPacket extends Packet {

	public static final int opcode = 0x40;
	
	private String reason;
	
	public PlayDisconnectPacket(String reason) {
		super(opcode);
		this.reason = reason;
	}
	
	public String getReason() {
		return this.reason;
	}
	
}
