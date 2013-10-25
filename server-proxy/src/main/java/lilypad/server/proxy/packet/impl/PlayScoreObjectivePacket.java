package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayScoreObjectivePacket extends Packet {

	public static final int opcode = 0x3B;
	
	private String name;
	private String value;
	private int action;
	
	public PlayScoreObjectivePacket(String name, String value, int action) {
		super(opcode);
		this.name = name;
		this.value = value;
		this.action = action;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public int getAction() {
		return this.action;
	}

	public boolean isCreating() {
		return this.action == 0;
	}
	
	public boolean isRemoving() {
		return this.action == 1;
	}
	
}
