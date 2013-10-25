package lilypad.packet.connect.impl;

import lilypad.packet.common.Packet;

public class RedirectPacket extends Packet {

	public static final int opcode = 0x04;
	
	private String server;
	private String player;
	
	public RedirectPacket(String server, String player) {
		super(opcode);
		this.server = server;
		this.player = player;
	}
	
	public String getServer() {
		return this.server;
	}
	
	public String getPlayer() {
		return this.player;
	}
	
}
