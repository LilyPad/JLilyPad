package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayDisconnectPacket extends Packet {

	public static final int opcode = 0x40;
	
	private String json;
	
	public PlayDisconnectPacket(String json) {
		super(opcode);
		this.json = json;
	}
	
	public String getJson() {
		return this.json;
	}
	
}
