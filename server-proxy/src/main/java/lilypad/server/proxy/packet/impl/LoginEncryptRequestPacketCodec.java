package lilypad.server.proxy.packet.impl;

import java.security.PublicKey;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class LoginEncryptRequestPacketCodec extends PacketCodec<LoginEncryptRequestPacket> {

	public LoginEncryptRequestPacketCodec() {
		super(LoginEncryptRequestPacket.opcode);
	}

	public LoginEncryptRequestPacket decode(ByteBuf buffer) throws Exception {
		String serverId = BufferUtils.readString(buffer);
		PublicKey publicKey = BufferUtils.readPublicKey(buffer);
		byte[] verifyToken = new byte[buffer.readUnsignedShort()];
		buffer.readBytes(verifyToken);
		return new LoginEncryptRequestPacket(serverId, publicKey, verifyToken);
	}

	public void encode(LoginEncryptRequestPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getServerId());
		BufferUtils.writePublicKey(buffer, packet.getPublicKey());
		buffer.writeShort(packet.getVerifyToken().length);
		buffer.writeBytes(packet.getVerifyToken());
	}

}
