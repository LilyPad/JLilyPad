package lilypad.server.proxy.packet.state;

import lilypad.packet.common.PacketCodecProvider;
import lilypad.server.proxy.packet.MinecraftPacketCodecRegistry;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair.StateCodecProvider;
import lilypad.server.proxy.packet.impl.StatusPingPacketCodec;
import lilypad.server.proxy.packet.impl.StatusRequestPacketCodec;
import lilypad.server.proxy.packet.impl.StatusResponsePacketCodec;

public class StatusStateCodecProvider implements StateCodecProvider {

	public static final StatusStateCodecProvider instance = new StatusStateCodecProvider();
	
	private MinecraftPacketCodecRegistry clientBound = new MinecraftPacketCodecRegistry();
	private MinecraftPacketCodecRegistry serverBound = new MinecraftPacketCodecRegistry();
	
	private StatusStateCodecProvider() {
		this.clientBound.register(new StatusPingPacketCodec());
		this.clientBound.register(new StatusResponsePacketCodec());
		this.serverBound.register(new StatusPingPacketCodec());
		this.serverBound.register(new StatusRequestPacketCodec());
	}
	
	public PacketCodecProvider getClientBound() {
		return this.clientBound;
	}

	public PacketCodecProvider getServerBound() {
		return this.serverBound;
	}

}
