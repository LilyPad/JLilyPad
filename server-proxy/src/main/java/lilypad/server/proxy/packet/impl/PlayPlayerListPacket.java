package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayPlayerListPacket extends Packet {

	public static final int opcode = 0x38;
	
	private String name;
	private boolean online;
	private int ping;
	
	public PlayPlayerListPacket(String name, boolean online, int ping) {
		super(opcode);
		this.name = name;
		this.online = online;
		this.ping = ping;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isOnline() {
		return this.online;
	}
	
	public int getPing() {
		return this.ping;
	}

}
