package lilypad.packet.common;

import io.netty.buffer.ByteBuf;

public abstract class PacketCodec<T extends Packet> {

	private int opcode;
	
	public PacketCodec(int opcode) {
		this.opcode = opcode;
	}
	
	public abstract T decode(ByteBuf buffer) throws Exception;
	
	public abstract void encode(T packet, ByteBuf buffer);
	
	@SuppressWarnings("unchecked")
	public final void encodePacket(Packet packet, ByteBuf buffer) {
		this.encode((T) packet, buffer);
	}
	
	public int getOpcode() {
		return this.opcode;
	}
	
}
