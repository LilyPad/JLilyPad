package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class RedirectPacketCodec extends PacketCodec<RedirectPacket> {

	public RedirectPacketCodec() {
		super(RedirectPacket.opcode);
	}

	public RedirectPacket decode(ByteBuf buffer) throws Exception {
		String server = BufferUtils.readString(buffer);
		String player = BufferUtils.readString(buffer);
		return new RedirectPacket(server, player);
	}

	public void encode(RedirectPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getServer());
		BufferUtils.writeString(buffer, packet.getPlayer());
	}

}
