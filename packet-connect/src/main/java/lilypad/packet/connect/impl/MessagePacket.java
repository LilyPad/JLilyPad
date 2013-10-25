package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.Packet;

public class MessagePacket extends Packet {

	public static final int opcode = 0x03;
	
	private String sender;
	private String channel;
	private ByteBuf payload;
	
	public MessagePacket(String sender, String channel, ByteBuf payload) {
		super(opcode);
		this.sender = sender;
		this.channel = channel;
		this.payload = payload;
	}
	
	public String getSender() {
		return this.sender;
	}
	
	public String getChannel() {
		return this.channel;
	}
	
	public ByteBuf getPayload() {
		return this.payload;
	}
	
}
