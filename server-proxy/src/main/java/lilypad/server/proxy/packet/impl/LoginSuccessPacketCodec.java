package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class LoginSuccessPacketCodec extends PacketCodec<LoginSuccessPacket> {

	public LoginSuccessPacketCodec() {
		super(LoginSuccessPacket.opcode);
	}

	public LoginSuccessPacket decode(ByteBuf buffer) throws Exception {
		return new LoginSuccessPacket(BufferUtils.readString(buffer), BufferUtils.readString(buffer));
	}

	public void encode(LoginSuccessPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getUuid());
		BufferUtils.writeString(buffer, packet.getUsername());
	}

}
