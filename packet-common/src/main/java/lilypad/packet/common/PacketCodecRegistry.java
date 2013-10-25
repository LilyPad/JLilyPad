package lilypad.packet.common;

public abstract class PacketCodecRegistry {

	private PacketCodec<?>[] packetCodecs = new PacketCodec<?>[256];
	
	public void submit(PacketCodec<?> packetCodec) {
		this.packetCodecs[packetCodec.getOpcode()] = packetCodec;
	}
	
	public PacketCodec<?> getOpcode(int opcode) {
		return this.packetCodecs[opcode];
	}
	
}
