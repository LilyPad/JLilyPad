package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.common.PacketCodec;
import io.netty.buffer.ByteBuf;

public class PlayerListPacketCodec extends PacketCodec<PlayerListPacket> {

	public PlayerListPacketCodec() {
		super(PlayerListPacket.opcode);
	}

	public PlayerListPacket decode(ByteBuf buffer) {
		return new PlayerListPacket(BufferUtils.readString16(buffer), buffer.readByte() != 0, buffer.readUnsignedShort());
	}

	public void encode(PlayerListPacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getPlayer(), buffer);
		buffer.writeByte(packet.isOnline() ? 1 : 0);
		buffer.writeShort(packet.getPing());
	}

}
