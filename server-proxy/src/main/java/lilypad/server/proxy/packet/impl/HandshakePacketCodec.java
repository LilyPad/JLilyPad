package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class HandshakePacketCodec extends PacketCodec<HandshakePacket> {

	public HandshakePacketCodec() {
		super(HandshakePacket.opcode);
	}

	public HandshakePacket decode(ByteBuf buffer) throws Exception {
		return new HandshakePacket(BufferUtils.readVarInt(buffer), BufferUtils.readString(buffer), buffer.readUnsignedShort(), BufferUtils.readVarInt(buffer));
	}

	public void encode(HandshakePacket packet, ByteBuf buffer) {
		BufferUtils.writeVarInt(buffer, packet.getProtocolVersion());
		BufferUtils.writeString(buffer, packet.getServerAddress());
		buffer.writeShort(packet.getServerPort());
		BufferUtils.writeVarInt(buffer, packet.getRequestedState());
	}

}
