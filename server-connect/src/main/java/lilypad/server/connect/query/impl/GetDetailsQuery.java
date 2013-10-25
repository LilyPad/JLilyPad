package lilypad.server.connect.query.impl;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class GetDetailsQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		ByteBuf response = Unpooled.buffer();
		InetSocketAddress bindAddress = sender.getPlayable().getBindAddress();
		BufferUtils.writeString(response, bindAddress.getAddress().getHostAddress());
		response.writeShort(bindAddress.getPort());
		BufferUtils.writeString(response, sender.getPlayable().getMotd());
		BufferUtils.writeString(response, sender.getPlayable().getVersion());
		return new ResultPacket(id, response);

	}

	public int getId() {
		return ConnectPacketConstants.requestGetDetails;
	}

}
