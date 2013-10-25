package lilypad.server.connect.query.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class RedirectQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String serverName = BufferUtils.readString(payload);
		String playerName = BufferUtils.readString(payload);
		NodeSession server = sender.getConnectService().getSessionMapper().getServerByUsername(serverName);
		if(server == null) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		if(!sender.getPlayable().redirect(playerName, server)) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
	}

	public int getId() {
		return ConnectPacketConstants.requestRedirect;
	}

}
