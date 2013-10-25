package lilypad.server.proxy.packet.state;

import lilypad.packet.common.PacketCodecProvider;
import lilypad.server.proxy.packet.MinecraftPacketCodecRegistry;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair.StateCodecProvider;
import lilypad.server.proxy.packet.impl.PlayDisconnectPacketCodec;
import lilypad.server.proxy.packet.impl.PlayJoinGamePacketCodec;
import lilypad.server.proxy.packet.impl.PlayPlayerListPacketCodec;
import lilypad.server.proxy.packet.impl.PlayRespawnPacketCodec;
import lilypad.server.proxy.packet.impl.PlayScoreObjectivePacketCodec;
import lilypad.server.proxy.packet.impl.PlayTeamPacketCodec;

public class PlayStateCodecProvider implements StateCodecProvider {

	public static final PlayStateCodecProvider instance = new PlayStateCodecProvider();
	
	private MinecraftPacketCodecRegistry clientBound = new MinecraftPacketCodecRegistry();
	private MinecraftPacketCodecRegistry serverBound = new MinecraftPacketCodecRegistry();
	
	private PlayStateCodecProvider() {
		this.clientBound.register(new PlayDisconnectPacketCodec());
		this.clientBound.register(new PlayJoinGamePacketCodec());
		this.clientBound.register(new PlayRespawnPacketCodec());
		this.clientBound.register(new PlayPlayerListPacketCodec());
		this.clientBound.register(new PlayScoreObjectivePacketCodec());
		this.clientBound.register(new PlayTeamPacketCodec());
	}
	
	public PacketCodecProvider getClientBound() {
		return this.clientBound;
	}

	public PacketCodecProvider getServerBound() {
		return this.serverBound;
	}

}
