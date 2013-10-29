package lilypad.server.connect.query.impl;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.Query;

public class GetDetailsQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		ByteBuf out = alloc.buffer();
		InetSocketAddress bindAddress = sender.getPlayable().getBindAddress();
		BufferUtils.writeString(out, bindAddress.getAddress().getHostAddress());
		out.writeShort(bindAddress.getPort());
		BufferUtils.writeString(out, sender.getPlayable().getMotd());
		BufferUtils.writeString(out, sender.getPlayable().getVersion());
		return new ResultPacket(id, out);

	}

	public int getId() {
		return ConnectPacketConstants.requestGetDetails;
	}

}
