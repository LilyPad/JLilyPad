package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class StatusResponsePacket extends Packet {

	public static final int opcode = 0x00;
	
	private String json;
	
	public StatusResponsePacket(String json) {
		super(opcode);
		this.json = json;
	}
	
	public String getJson() {
		return this.json;
	}

}
