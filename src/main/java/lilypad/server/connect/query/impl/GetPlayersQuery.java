package lilypad.server.connect.query.impl;

import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class GetPlayersQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		boolean asList = payload.readBoolean();
		Set<String> players = sender.getPlayable().getPlayers();
		ByteBuf response = Unpooled.buffer();
		response.writeBoolean(asList);
		response.writeShort(players.size());
		response.writeShort(sender.getPlayable().getPlayerMaximum());
		if(asList) {
			for(String player : players) {
				BufferUtils.writeString16(player, response);
			}
		}
		return new ResultPacket(id, response);
	}

	public int getId() {
		return ConnectPacketConstants.requestGetPlayers;
	}

}
