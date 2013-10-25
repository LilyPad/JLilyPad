package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class LoginStartPacket extends Packet {

	public static final int opcode = 0x00;
	
	private String name;
	
	public LoginStartPacket(String name) {
		super(opcode);
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}

}
