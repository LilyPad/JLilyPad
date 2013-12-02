package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayClientSettingsPacketCodec extends PacketCodec<PlayClientSettingsPacket> {

	public PlayClientSettingsPacketCodec() {
		super(PlayClientSettingsPacket.opcode);
	}

	public PlayClientSettingsPacket decode(ByteBuf buffer) throws Exception {
		return new PlayClientSettingsPacket(BufferUtils.readString(buffer), buffer.readByte(), buffer.readByte(), buffer.readBoolean(), buffer.readByte(), buffer.readBoolean());
	}

	public void encode(PlayClientSettingsPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getLocale());
		buffer.writeByte(packet.getViewDistance());
		buffer.writeByte(packet.getChatFlags());
		buffer.writeBoolean(packet.isUnused());
		buffer.writeByte(packet.getDifficulty());
		buffer.writeBoolean(packet.isShowCape());
	}

}
