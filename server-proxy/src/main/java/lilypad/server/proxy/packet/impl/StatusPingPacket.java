package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class StatusPingPacket extends Packet {

	public static final int opcode = 0x01;
	
	private long time;
	
	public StatusPingPacket(long time) {
		super(opcode);
		this.time = time;
	}
	
	public long getTime() {
		return this.time;
	}

}
