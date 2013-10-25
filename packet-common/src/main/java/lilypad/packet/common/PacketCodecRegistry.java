package lilypad.packet.common;

public class PacketCodecRegistry implements PacketCodecProvider {

	private PacketCodec<?>[] packetCodecs = new PacketCodec<?>[256];
	
	public void register(PacketCodec<?> packetCodec) {
		this.packetCodecs[packetCodec.getOpcode()] = packetCodec;
	}
	
	public PacketCodec<?> getByOpcode(int opcode) {
		return this.packetCodecs[opcode];
	}
	
}
