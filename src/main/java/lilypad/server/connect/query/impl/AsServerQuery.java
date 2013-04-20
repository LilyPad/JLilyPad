package lilypad.server.connect.query.impl;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionRole;
import lilypad.server.connect.query.Query;

public class AsServerQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(sender.getRole() != NodeSessionRole.AUTHENTICATED) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String ip = BufferUtils.readString16(payload);
		if(ip.length() == 0) {
			ip = sender.getAddress().getAddress().getHostAddress();
		}
		sender.markServer(new InetSocketAddress(ip, payload.readUnsignedShort()));
		ByteBuf response = Unpooled.buffer();
		BufferUtils.writeString16(sender.getSecurityKey(), response);
		return new ResultPacket(id, response);
	}

	public int getId() {
		return ConnectPacketConstants.requestAsServer;
	}

}
