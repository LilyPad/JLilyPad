package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

import java.util.UUID;

public class PlayerPacketCodec extends PacketCodec<PlayerPacket> {

	public PlayerPacketCodec() {
		super(PlayerPacket.opcode);
	}

	public PlayerPacket decode(ByteBuf buffer) throws Exception {
		boolean addOrRemove = buffer.readBoolean();
		String name = BufferUtils.readString(buffer);
		long high = buffer.readLong();
		long low = buffer.readLong();
		UUID uuid = new UUID(high, low);
		return new PlayerPacket(addOrRemove, name, uuid);
	}

	public void encode(PlayerPacket packet, ByteBuf buffer) {
		buffer.writeBoolean(packet.isJoining());
		BufferUtils.writeString(buffer, packet.getPlayerName());
		buffer.writeLong(packet.getPlayerUUID().getMostSignificantBits());
		buffer.writeLong(packet.getPlayerUUID().getLeastSignificantBits());
	}

}
