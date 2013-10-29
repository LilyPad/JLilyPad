package lilypad.server.connect.query.impl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionRole;
import lilypad.server.connect.query.Query;

public class NotifyPlayer implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(sender.getRole() != NodeSessionRole.PROXY) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		boolean addOrRemove = in.readBoolean();
		String player = BufferUtils.readString(in);
		if(addOrRemove) {
			if(!sender.addPlayer(player)) {
				return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
			}
		} else {
			sender.removePlayer(player);
		}
		return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
	}

	public int getId() {
		return ConnectPacketConstants.requestNotifyPlayer;
	}

}
