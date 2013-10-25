package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class StatusRequestPacketCodec extends PacketCodec<StatusRequestPacket> {

	public StatusRequestPacketCodec() {
		super(StatusRequestPacket.opcode);
	}

	public StatusRequestPacket decode(ByteBuf buffer) throws Exception {
		return new StatusRequestPacket();
	}

	public void encode(StatusRequestPacket packet, ByteBuf buffer) {
		// no payload
	}

}
