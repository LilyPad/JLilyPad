package lilypad.packet.connect.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class KeepalivePacketCodec extends PacketCodec<KeepalivePacket> {

	public KeepalivePacketCodec() {
		super(KeepalivePacket.opcode);
	}

	public KeepalivePacket decode(ByteBuf buffer) throws Exception {
		int random = buffer.readInt();
		return new KeepalivePacket(random);
	}

	public void encode(KeepalivePacket packet, ByteBuf buffer) {
		buffer.writeInt(packet.getRandom());
	}

}
