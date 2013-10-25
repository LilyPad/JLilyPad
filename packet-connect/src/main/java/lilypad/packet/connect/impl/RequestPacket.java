package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.Packet;

public class RequestPacket extends Packet {

	public static final int opcode = 0x01;
	
	private int id;
	private int operation;
	private ByteBuf payload;
	
	public RequestPacket(int id, int operation, ByteBuf payload) {
		super(opcode);
		this.id = id;
		this.operation = operation;
		this.payload = payload;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getOperation() {
		return this.operation;
	}
	
	public ByteBuf getPayload() {
		return this.payload;
	}
	
}
