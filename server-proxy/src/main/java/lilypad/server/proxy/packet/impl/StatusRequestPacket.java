package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class StatusRequestPacket extends Packet {

	public static final int opcode = 0x00;
	
	public StatusRequestPacket() {
		super(opcode);
	}

}
