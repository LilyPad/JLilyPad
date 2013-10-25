package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class ServerPacketCodec extends PacketCodec<ServerPacket> {

	public ServerPacketCodec() {
		super(ServerPacket.opcode);
	}

	public ServerPacket decode(ByteBuf buffer) throws Exception {
		boolean addOrRemove = buffer.readBoolean();
		String server = BufferUtils.readString16(buffer);
		if(addOrRemove) {
		 	return new ServerAddPacket(server, BufferUtils.readString16(buffer), BufferUtils.readString16(buffer), buffer.readUnsignedShort());
		}
		return new ServerPacket(server);
	}

	public void encode(ServerPacket packet, ByteBuf buffer) {
		boolean addOrRemove = packet.isAdding();
		buffer.writeBoolean(addOrRemove);
		BufferUtils.writeString16(packet.getServer(), buffer);
		if(addOrRemove) {
			ServerAddPacket addPacket = (ServerAddPacket) packet;
			BufferUtils.writeString16(addPacket.getSecurityKey(), buffer);
			BufferUtils.writeString16(addPacket.getAddress(), buffer);
			buffer.writeShort(addPacket.getPort());
		}
	}

}
