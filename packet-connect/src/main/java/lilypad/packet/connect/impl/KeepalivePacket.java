package lilypad.packet.connect.impl;

import lilypad.packet.common.Packet;

public class KeepalivePacket extends Packet {

	public static final int opcode = 0x00;
	
	private int random;
	
	public KeepalivePacket(int random) {
		super(opcode);
		this.random = random;
	}
	
	public int getRandom() {
		return this.random;
	}
	
}
