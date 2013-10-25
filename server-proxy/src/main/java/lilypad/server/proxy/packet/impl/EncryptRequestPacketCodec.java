package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;

public class EncryptRequestPacketCodec extends PacketCodec<EncryptRequestPacket> {
	
	public EncryptRequestPacketCodec() {
		super(EncryptRequestPacket.opcode);
	}

	public EncryptRequestPacket decode(ByteBuf buffer) {
		return new EncryptRequestPacket(BufferUtils.readString16(buffer), BufferUtils.readPublicKey(buffer), buffer.readBytes(buffer.readUnsignedShort()).array());
	}

	public void encode(EncryptRequestPacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getServerKey(), buffer);
		BufferUtils.writePublicKey(packet.getPublicKey(), buffer);
		buffer.writeShort(packet.getServerVerification().length);
		buffer.writeBytes(packet.getServerVerification());
	}

}
