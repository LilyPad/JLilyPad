package lilypad.server.connect.query.impl;

import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class GetPlayersQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		boolean asList = in.readBoolean();
		Set<String> players = sender.getPlayable().getPlayers();
		ByteBuf out = alloc.buffer();
		out.writeBoolean(asList);
		out.writeShort(players.size());
		out.writeShort(sender.getPlayable().getPlayerMaximum());
		if(asList) {
			for(String player : players) {
				BufferUtils.writeString(out, player);
			}
		}
		return new ResultPacket(id, out);
	}

	public int getId() {
		return ConnectPacketConstants.requestGetPlayers;
	}

}
