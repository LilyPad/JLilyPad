package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class LoginDisconnectPacketCodec extends PacketCodec<LoginDisconnectPacket> {

	public LoginDisconnectPacketCodec() {
		super(LoginDisconnectPacket.opcode);
	}

	public LoginDisconnectPacket decode(ByteBuf buffer) throws Exception {
		return new LoginDisconnectPacket(BufferUtils.readString(buffer));
	}

	public void encode(LoginDisconnectPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getJson());
	}

}
