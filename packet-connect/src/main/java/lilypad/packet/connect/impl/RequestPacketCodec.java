package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class RequestPacketCodec extends PacketCodec<RequestPacket> {

	public RequestPacketCodec() {
		super(RequestPacket.opcode);
	}

	public RequestPacket decode(ByteBuf buffer) throws Exception {
		return new RequestPacket(buffer.readInt(), buffer.readUnsignedByte(), buffer.readBytes(buffer.readUnsignedShort()));
	}

	public void encode(RequestPacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getId());
		buffer.writeByte(packet.getOperation());
		ByteBuf payload = packet.getPayload();
		buffer.writeShort(payload.readableBytes());
		buffer.writeBytes(payload, payload.readerIndex(), payload.readableBytes());
		packet.getPayload().release();
	}

}
