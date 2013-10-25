package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class LoginStartPacketCodec extends PacketCodec<LoginStartPacket> {

	public LoginStartPacketCodec() {
		super(LoginStartPacket.opcode);
	}

	public LoginStartPacket decode(ByteBuf buffer) throws Exception {
		return new LoginStartPacket(BufferUtils.readString(buffer));
	}

	public void encode(LoginStartPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getName());
	}

}
