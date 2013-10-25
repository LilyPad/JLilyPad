package lilypad.server.proxy.packet;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class GenericPacketCodec extends PacketCodec<GenericPacket> {

	public GenericPacketCodec(int opcode) {
		super(opcode);
	}

	public GenericPacket decode(ByteBuf buffer) throws Exception {
		return new GenericPacket(super.getOpcode(), buffer.readBytes(buffer.readableBytes()));
	}

	public void encode(GenericPacket packet, ByteBuf buffer) {
		buffer.writeBytes(packet.getBuffer());
	}

}
