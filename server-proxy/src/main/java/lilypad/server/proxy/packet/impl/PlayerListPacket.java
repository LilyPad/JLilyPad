package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayerListPacket extends Packet {

	public static final int opcode = 0xC9;
	
	private String player;
	private boolean online;
	private int ping;
	
	public PlayerListPacket(String player, boolean online, int ping) {
		super(opcode);
		this.player = player;
		this.online = online;
		this.ping = ping;
	}
	
	public String getPlayer() {
		return this.player;
	}
	
	public boolean isOnline() {
		return this.online;
	}
	
	public int getPing() {
		return this.ping;
	}
	
}
