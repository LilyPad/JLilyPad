package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.PacketCodec;
import io.netty.buffer.ByteBuf;

public class EncryptResponsePacketCodec extends PacketCodec<EncryptResponsePacket> {

	public EncryptResponsePacketCodec() {
		super(EncryptResponsePacket.opcode);
	}

	public EncryptResponsePacket decode(ByteBuf buffer) {
		return new EncryptResponsePacket(buffer.readBytes(buffer.readUnsignedShort()).array(), buffer.readBytes(buffer.readUnsignedShort()).array());
	}

	public void encode(EncryptResponsePacket packet, ByteBuf buffer) {
		buffer.writeShort(packet.getSharedSecret().length);
		buffer.writeBytes(packet.getSharedSecret());
		buffer.writeShort(packet.getServerVerification().length);
		buffer.writeBytes(packet.getServerVerification());
	}

}
