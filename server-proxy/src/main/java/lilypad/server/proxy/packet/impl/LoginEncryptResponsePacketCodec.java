package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class LoginEncryptResponsePacketCodec extends PacketCodec<LoginEncryptResponsePacket> {

	public LoginEncryptResponsePacketCodec() {
		super(LoginEncryptResponsePacket.opcode);
	}

	public LoginEncryptResponsePacket decode(ByteBuf buffer) throws Exception {
		byte[] sharedSecret = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(sharedSecret);
		byte[] verifyToken = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(verifyToken);
		return new LoginEncryptResponsePacket(sharedSecret, verifyToken);
	}

	public void encode(LoginEncryptResponsePacket packet, ByteBuf buffer) {
		buffer.writeShort(packet.getSharedSecret().length);
		buffer.writeBytes(packet.getSharedSecret());
		buffer.writeShort(packet.getVerifyToken().length);
		buffer.writeBytes(packet.getVerifyToken());
	}

}
