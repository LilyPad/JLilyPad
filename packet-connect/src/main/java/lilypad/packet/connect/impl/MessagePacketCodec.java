package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class MessagePacketCodec extends PacketCodec<MessagePacket> {

	public MessagePacketCodec() {
		super(MessagePacket.opcode);
	}

	public MessagePacket decode(ByteBuf buffer) throws Exception {
		String sender = BufferUtils.readString16(buffer);
		String channel = BufferUtils.readString16(buffer);
		ByteBuf payload = buffer.readBytes(buffer.readUnsignedShort());
		return new MessagePacket(sender, channel, payload);
	}

	public void encode(MessagePacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getSender(), buffer);
		BufferUtils.writeString16(packet.getChannel(), buffer);
		ByteBuf payload = packet.getPayload();
		buffer.writeShort(payload.readableBytes());
		buffer.writeBytes(payload, payload.readerIndex(), payload.readableBytes());
	}

}
