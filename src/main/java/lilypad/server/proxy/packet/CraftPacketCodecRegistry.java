package lilypad.server.proxy.packet;

import lilypad.server.proxy.packet.impl.EncryptRequestPacketCodec;
import lilypad.server.proxy.packet.impl.EncryptResponsePacketCodec;
import lilypad.server.proxy.packet.impl.HandshakePacketCodec;
import lilypad.server.proxy.packet.impl.KickPacketCodec;
import lilypad.server.proxy.packet.impl.LoginPacketCodec;
import lilypad.server.proxy.packet.impl.PlayerListPacketCodec;
import lilypad.server.proxy.packet.impl.RespawnPacketCodec;
import lilypad.server.proxy.packet.impl.ScoreboardObjectivePacketCodec;
import lilypad.server.proxy.packet.impl.StatusPacketCodec;
import lilypad.server.proxy.packet.impl.TeamPacketCodec;
import lilypad.packet.common.PacketCodecRegistry;

public class CraftPacketCodecRegistry extends PacketCodecRegistry {

	public static final CraftPacketCodecRegistry instance = new CraftPacketCodecRegistry();
	
	private CraftPacketCodecRegistry() {
		for(int i = 0; i < 256; i++) {
			super.submit(new GenericPacketCodec(i));
		}
		super.submit(new LoginPacketCodec());
		super.submit(new HandshakePacketCodec());
		super.submit(new RespawnPacketCodec());
		super.submit(new PlayerListPacketCodec());
		super.submit(new StatusPacketCodec());
		super.submit(new EncryptResponsePacketCodec());
		super.submit(new EncryptRequestPacketCodec());
		super.submit(new KickPacketCodec());
		super.submit(new ScoreboardObjectivePacketCodec());
		super.submit(new TeamPacketCodec());
	}
	
}
