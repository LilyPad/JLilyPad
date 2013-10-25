package lilypad.packet.common;

public interface PacketCodecProvider {

	public PacketCodec<?> getByOpcode(int opcode);
	
}
