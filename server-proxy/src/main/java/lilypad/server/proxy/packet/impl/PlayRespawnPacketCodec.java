package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayRespawnPacketCodec extends PacketCodec<PlayRespawnPacket> {
	
	public PlayRespawnPacketCodec() {
		super(PlayRespawnPacket.opcode);
	}

	public PlayRespawnPacket decode(ByteBuf buffer) throws Exception {
		return new PlayRespawnPacket(buffer.readInt(), buffer.readUnsignedByte(), buffer.readUnsignedByte(), BufferUtils.readString(buffer));
	}

	public void encode(PlayRespawnPacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getDimension());
		buffer.writeByte(packet.getDifficulty());
		buffer.writeByte(packet.getGamemode());
		BufferUtils.writeString(buffer, packet.getLevelType());
	}

}
