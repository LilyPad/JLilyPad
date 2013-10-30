package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.connect.ConnectPacketConstants;

public class ResultPacketCodec extends PacketCodec<ResultPacket> {

	public ResultPacketCodec() {
		super(ResultPacket.opcode);
	}

	public ResultPacket decode(ByteBuf buffer) throws Exception {
		int id = buffer.readInt();
		int statusCode = buffer.readUnsignedByte();
		if(statusCode == ConnectPacketConstants.statusSuccess) {
			return new ResultPacket(id, buffer.readBytes(buffer.readUnsignedShort()));
		} else {
			return new ResultPacket(id, statusCode);
		}
	}

	public void encode(ResultPacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getId());
		buffer.writeByte(packet.getStatusCode());
		ByteBuf payload = packet.getPayload();
		if(payload != null) {
			buffer.writeShort(payload.readableBytes());
			buffer.writeBytes(payload, payload.readerIndex(), payload.readableBytes());
			packet.getPayload().release();
		} else if(packet.getStatusCode() == ConnectPacketConstants.statusSuccess) {
			buffer.writeShort(0);
		}
	}
	
}
