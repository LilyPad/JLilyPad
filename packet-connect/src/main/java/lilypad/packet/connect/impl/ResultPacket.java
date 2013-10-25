package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.Packet;
import lilypad.packet.connect.ConnectPacketConstants;

public class ResultPacket extends Packet {

	public static final int opcode = 0x02;
	
	private int id;
	private int statusCode;
	private ByteBuf payload;
	
	public ResultPacket(int id, int statusCode) {
		super(opcode);
		this.id = id;
		this.statusCode = statusCode;
	}
	
	public ResultPacket(int id, ByteBuf payload) {
		super(opcode);
		this.id = id;
		this.statusCode = ConnectPacketConstants.statusSuccess;
		this.payload = payload;
	}
	
	public int getId(){
		return this.id;
	}
	
	public int getStatusCode() {
		return this.statusCode;
	}
	
	public ByteBuf getPayload() {
		return this.payload;
	}
	
}
