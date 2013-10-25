package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.Packet;

public class TeamPacket extends Packet {

	public static final int opcode = 0xD1;
	
	private String name;
	private byte mode;
	private ByteBuf payload;
	
	public TeamPacket(String name, byte mode, ByteBuf payload) {
		super(opcode);
		this.name = name;
		this.mode = mode;
		this.payload = payload;
	}
	
	public String getName() {
		return this.name;
	}
	
	public byte getMode() {
		return this.mode;
	}
	
	public boolean isCreating() {
		return this.mode == 0;
	}
	
	public boolean isRemoving() {
		return this.mode == 1;
	}
	
	public ByteBuf getPayload() {
		return this.payload;
	}

}
