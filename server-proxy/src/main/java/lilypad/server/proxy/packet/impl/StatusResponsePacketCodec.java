package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class StatusResponsePacketCodec extends PacketCodec<StatusResponsePacket> {

	public StatusResponsePacketCodec() {
		super(StatusResponsePacket.opcode);
	}

	public StatusResponsePacket decode(ByteBuf buffer) throws Exception {
		return new StatusResponsePacket(BufferUtils.readString(buffer));
	}

	public void encode(StatusResponsePacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getJson());
	}

}
