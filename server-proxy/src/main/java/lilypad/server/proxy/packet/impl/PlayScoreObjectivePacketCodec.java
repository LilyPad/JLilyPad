package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayScoreObjectivePacketCodec extends PacketCodec<PlayScoreObjectivePacket> {

	public PlayScoreObjectivePacketCodec() {
		super(PlayScoreObjectivePacket.opcode);
	}

	public PlayScoreObjectivePacket decode(ByteBuf buffer) throws Exception {
		return new PlayScoreObjectivePacket(BufferUtils.readString(buffer), BufferUtils.readString(buffer), buffer.readByte());
	}

	public void encode(PlayScoreObjectivePacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getName());
		BufferUtils.writeString(buffer, packet.getValue());
		buffer.writeByte(packet.getAction());
	}

}
