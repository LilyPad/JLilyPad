package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class ScoreboardObjectivePacketCodec extends PacketCodec<ScoreboardObjectivePacket> {

	public ScoreboardObjectivePacketCodec() {
		super(ScoreboardObjectivePacket.opcode);
	}

	public ScoreboardObjectivePacket decode(ByteBuf buffer) throws Exception {
		return new ScoreboardObjectivePacket(BufferUtils.readString16(buffer), BufferUtils.readString16(buffer), buffer.readByte());
	}

	public void encode(ScoreboardObjectivePacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getName(), buffer);
		BufferUtils.writeString16(packet.getValue(), buffer);
		buffer.writeByte(packet.getCreateRemove());
	}

}
