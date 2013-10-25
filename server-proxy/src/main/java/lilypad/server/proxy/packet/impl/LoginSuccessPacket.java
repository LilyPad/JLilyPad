package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class LoginSuccessPacket extends Packet {

	public static final int opcode = 0x02;
	
	private String uuid;
	private String username;
	
	public LoginSuccessPacket(String uuid, String username) {
		super(opcode);
		this.uuid = uuid;
		this.username = username;
	}
	
	public String getUuid() {
		return this.uuid;
	}
	
	public String getUsername() {
		return this.username;
	}

}
