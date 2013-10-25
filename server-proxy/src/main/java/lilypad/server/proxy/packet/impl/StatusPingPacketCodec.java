package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class StatusPingPacketCodec extends PacketCodec<StatusPingPacket> {

	public StatusPingPacketCodec() {
		super(StatusPingPacket.opcode);
	}

	public StatusPingPacket decode(ByteBuf buffer) throws Exception {
		return new StatusPingPacket(buffer.readLong());
	}

	public void encode(StatusPingPacket packet, ByteBuf buffer) {
		buffer.writeLong(packet.getTime());
	}

}
