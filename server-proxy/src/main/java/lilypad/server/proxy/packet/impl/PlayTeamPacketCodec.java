package lilypad.server.proxy.packet.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;
import lilypad.packet.common.util.BufferUtils;

public class PlayTeamPacketCodec extends PacketCodec<PlayTeamPacket> {

	public PlayTeamPacketCodec() {
		super(PlayTeamPacket.opcode);
	}

	public PlayTeamPacket decode(ByteBuf buffer) throws Exception {
		String name = BufferUtils.readString(buffer);
		int mode = buffer.readByte();
		String displayName = null;
		String prefix = null;
		String suffix = null;
		int friendlyFire = 0;
		if(mode == 0 || mode == 2) {
			displayName = BufferUtils.readString(buffer);
			prefix = BufferUtils.readString(buffer);
			suffix = BufferUtils.readString(buffer);
			friendlyFire = buffer.readByte();
		}
		String[] players = null;
		if(mode == 0 || mode == 3 || mode == 4) {
			players = new String[buffer.readShort()];
			for(int i = 0; i < players.length; i++) {
				players[i] = BufferUtils.readString(buffer);
			}
		}
		return new PlayTeamPacket(name, mode, displayName, prefix, suffix, friendlyFire, players);
	}

	public void encode(PlayTeamPacket packet, ByteBuf buffer) {
		BufferUtils.writeString(buffer, packet.getName());
		int mode = packet.getMode();
		buffer.writeByte(mode);
		if(mode == 0 || mode == 2) {
			BufferUtils.writeString(buffer, packet.getDisplayName());
			BufferUtils.writeString(buffer, packet.getPrefix());
			BufferUtils.writeString(buffer, packet.getSuffix());
			buffer.writeByte(packet.getFriendlyFire());
		}
		if(mode == 0 || mode == 3 || mode == 4) {
			String[] players = packet.getPlayers();
			buffer.writeShort(players.length);
			for(String player : players) {
				BufferUtils.writeString(buffer, player);
			}
		}
	}

}
