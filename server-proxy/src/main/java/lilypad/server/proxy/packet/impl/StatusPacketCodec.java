package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.PacketCodec;
import io.netty.buffer.ByteBuf;

public class StatusPacketCodec extends PacketCodec<StatusPacket> {

	public StatusPacketCodec() {
		super(StatusPacket.opcode);
	}

	public StatusPacket decode(ByteBuf buffer) {
		return new StatusPacket(buffer.readUnsignedByte());
	}

	public void encode(StatusPacket packet, ByteBuf buffer) {
		buffer.writeByte(packet.getStatus());
	}

}
