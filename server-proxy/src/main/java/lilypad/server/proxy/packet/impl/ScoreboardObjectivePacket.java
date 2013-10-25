package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class ScoreboardObjectivePacket extends Packet {
	
	public static final int opcode = 0xCE;
	
	private String name;
	private String value;
	private byte createRemove;
	
	public ScoreboardObjectivePacket(String name, String value, byte createRemove) {
		super(opcode);
		this.name = name;
		this.value = value;
		this.createRemove = createRemove;
	}
	
	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}
	
	public byte getCreateRemove() {
		return this.createRemove;
	}
	
	public boolean isCreating() {
		return this.createRemove == 0;
	}
	
	public boolean isRemoving() {
		return this.createRemove == 1;
	}
	
}
