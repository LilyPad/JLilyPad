package lilypad.server.proxy.packet;

import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.PacketCodecProvider;

public class ProxyPacketCodecProvider implements PacketCodecProvider {

	private PacketCodecProvider provider;
	
	public ProxyPacketCodecProvider(PacketCodecProvider provider) {
		this.provider = provider;
	}
	
	public PacketCodec<?> getByOpcode(int opcode) {
		return this.provider.getByOpcode(opcode);
	}
	
	public PacketCodecProvider getProvider() {
		return this.provider;
	}
	
	public void setProvider(PacketCodecProvider provider) {
		this.provider = provider;
	}
	
}
