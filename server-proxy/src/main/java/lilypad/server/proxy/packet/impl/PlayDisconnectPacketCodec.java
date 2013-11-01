package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayDisconnectPacketCodec extends PacketCodec<PlayDisconnectPacket> {

	public PlayDisconnectPacketCodec() {
		super(PlayDisconnectPacket.opcode);
	}

	public PlayDisconnectPacket decode(ByteBuf buffer) throws Exception {
		return new PlayDisconnectPacket(BufferUtils.readString(buffer));
	}

	public void encode(PlayDisconnectPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getJson());
	}

}
