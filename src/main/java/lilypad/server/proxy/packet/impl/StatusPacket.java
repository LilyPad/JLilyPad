package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class StatusPacket extends Packet {

	public static final int opcode = 0xCD;
	
	private int status;
	
	public StatusPacket(int status) {
		super(opcode);
		this.status = status;
	}
	
	public int getStatus() {
		return this.status;
	}

}
