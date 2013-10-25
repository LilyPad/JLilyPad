package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;
import lilypad.server.proxy.packet.GenericPacketCodec;
import lilypad.server.proxy.packet.GenericPacketUnitArray;
import lilypad.server.proxy.packet.GenericPacketUnitArray.OpPair;

public class TeamPacketCodec extends PacketCodec<TeamPacket> {

	public static OpPair[] opPairs = new OpPair[] { GenericPacketUnitArray.teamData };
	
	public TeamPacketCodec() {
		super(TeamPacket.opcode);
	}

	public TeamPacket decode(ByteBuf buffer) throws Exception {
		String name = BufferUtils.readString16(buffer);
		ByteBuf payload = GenericPacketCodec.decode(buffer, opPairs);
		return new TeamPacket(name, payload.readByte(), payload);
	}

	public void encode(TeamPacket packet, ByteBuf buffer) {
		BufferUtils.writeString16(packet.getName(), buffer);
		buffer.writeByte(packet.getMode());
		ByteBuf payload = packet.getPayload();
		if(payload == null) {
			return;
		}
		buffer.writeBytes(payload);
		payload.discardReadBytes();
		payload.release();
	}

}
