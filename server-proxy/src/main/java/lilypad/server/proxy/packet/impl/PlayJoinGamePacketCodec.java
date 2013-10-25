package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayJoinGamePacketCodec extends PacketCodec<PlayJoinGamePacket> {

	public PlayJoinGamePacketCodec() {
		super(PlayJoinGamePacket.opcode);
	}

	public PlayJoinGamePacket decode(ByteBuf buffer) throws Exception {
		return new PlayJoinGamePacket(buffer.readInt(), buffer.readUnsignedByte(), buffer.readByte(), buffer.readUnsignedByte(), buffer.readUnsignedByte(), BufferUtils.readString(buffer));
	}

	public void encode(PlayJoinGamePacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getEntityId());
		buffer.writeByte(packet.getGamemode());
		buffer.writeByte(packet.getDimension());
		buffer.writeByte(packet.getDifficulty());
		buffer.writeByte(packet.getMaxPlayers());
		BufferUtils.writeString(buffer, packet.getLevelType());
	}
	
}
