package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayPlayerListPacketCodec extends PacketCodec<PlayPlayerListPacket> {

	public PlayPlayerListPacketCodec() {
		super(PlayPlayerListPacket.opcode);
	}

	public PlayPlayerListPacket decode(ByteBuf buffer) throws Exception {
		return new PlayPlayerListPacket(BufferUtils.readString(buffer), buffer.readBoolean(), buffer.readShort());
	}

	public void encode(PlayPlayerListPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getName());
		buffer.writeBoolean(packet.isOnline());
		buffer.writeShort(packet.getPing());
	}

}
