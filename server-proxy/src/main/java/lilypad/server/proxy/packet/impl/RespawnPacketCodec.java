package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.common.PacketCodec;
import io.netty.buffer.ByteBuf;

public class RespawnPacketCodec extends PacketCodec<RespawnPacket> {

	public RespawnPacketCodec() {
		super(RespawnPacket.opcode);
	}

	public RespawnPacket decode(ByteBuf buffer) {
		return new RespawnPacket(buffer.readInt(), buffer.readByte(), buffer.readByte(), buffer.readShort(), BufferUtils.readString16(buffer));
	}

	public void encode(RespawnPacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getDimension());
		buffer.writeByte(packet.getDifficulty());
		buffer.writeByte(packet.getGamemode());
		buffer.writeShort(packet.getHeight());
		BufferUtils.writeString16(packet.getLevelType(), buffer);
	}

}
