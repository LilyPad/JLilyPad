package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.common.PacketCodec;
import io.netty.buffer.ByteBuf;

public class LoginPacketCodec extends PacketCodec<LoginPacket> {

	public LoginPacketCodec() {
		super(LoginPacket.opcode);
	}

	public LoginPacket decode(ByteBuf buffer) {
		return new LoginPacket(buffer.readInt(), BufferUtils.readString16(buffer), buffer.readByte(), buffer.readByte(), buffer.readByte(), buffer.readUnsignedByte(), buffer.readUnsignedByte());
	}

	public void encode(LoginPacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getEntityId());
		BufferUtils.writeString16(packet.getLevelType(), buffer);
		buffer.writeByte(packet.getGamemode());
		buffer.writeByte(packet.getDimension());
		buffer.writeByte(packet.getDifficulty());
		buffer.writeByte(packet.getHeight());
		buffer.writeByte(packet.getMaxPlayers());
	}
	
}
