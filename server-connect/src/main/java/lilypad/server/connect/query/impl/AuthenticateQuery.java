package lilypad.server.connect.query.impl;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class AuthenticateQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String username = BufferUtils.readString(payload);
		String password = BufferUtils.readString(payload);
		if(!sender.getAuthenticator().authenticate(username, password, sender.getAuthenticationKey())) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		sender.markAuthenticated(username);
		return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
	}

	public int getId() {
		return ConnectPacketConstants.requestAuthenticate;
	}

}
