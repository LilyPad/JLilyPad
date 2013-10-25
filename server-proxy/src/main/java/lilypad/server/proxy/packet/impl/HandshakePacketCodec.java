package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;

public class HandshakePacketCodec extends PacketCodec<HandshakePacket> {
	
	public HandshakePacketCodec() {
		super(HandshakePacket.opcode);
	}

	public HandshakePacket decode(ByteBuf buffer) {
		return new HandshakePacket(buffer.readByte(), BufferUtils.readString16(buffer), BufferUtils.readString16(buffer), buffer.readInt());
	}

	public void encode(HandshakePacket packet, ByteBuf buffer) {
		buffer.writeByte(packet.getProtocolVersion());
		BufferUtils.writeString16(packet.getUsername(), buffer);
		BufferUtils.writeString16(packet.getServerHost(), buffer);
		buffer.writeInt(packet.getServerPort());
	}

}
