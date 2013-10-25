package lilypad.server.proxy.packet.state;

import lilypad.packet.common.PacketCodecProvider;
import lilypad.server.proxy.packet.MinecraftPacketCodecRegistry;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair.StateCodecProvider;
import lilypad.server.proxy.packet.impl.HandshakePacketCodec;

public class HandshakeStateCodecProvider implements StateCodecProvider {

	public static final HandshakeStateCodecProvider instance = new HandshakeStateCodecProvider();
	
	private MinecraftPacketCodecRegistry clientBound = new MinecraftPacketCodecRegistry();
	private MinecraftPacketCodecRegistry serverBound = new MinecraftPacketCodecRegistry();
	
	private HandshakeStateCodecProvider() {
		this.serverBound.register(new HandshakePacketCodec());
	}
	
	public PacketCodecProvider getClientBound() {
		return this.clientBound;
	}

	public PacketCodecProvider getServerBound() {
		return this.serverBound;
	}

}
