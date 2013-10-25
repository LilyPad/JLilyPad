package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;

public class KickPacketCodec extends PacketCodec<KickPacket> {

	public KickPacketCodec() {
		super(KickPacket.opcode);
	}

	public KickPacket decode(ByteBuf buffer) {
		return new KickPacket(BufferUtils.readString16(buffer));
	}

	public void encode(KickPacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getMessage(), buffer);
	}

}
