package lilypad.server.proxy.packet;

import lilypad.packet.common.PacketCodecRegistry;

public class MinecraftPacketCodecRegistry extends PacketCodecRegistry {
	
	public MinecraftPacketCodecRegistry() {
		for(int i = 0; i < 256; i++) {
			super.register(new GenericPacketCodec(i));
		}
	}
	
}
