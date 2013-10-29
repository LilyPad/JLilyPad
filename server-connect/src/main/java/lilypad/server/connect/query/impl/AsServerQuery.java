package lilypad.server.connect.query.impl;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionRole;
import lilypad.server.connect.query.Query;

public class AsServerQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(sender.getRole() != NodeSessionRole.AUTHENTICATED) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String ip = BufferUtils.readString(in);
		if(ip.length() == 0) {
			ip = sender.getAddress().getAddress().getHostAddress();
		}
		if(!sender.markServer(new InetSocketAddress(ip, in.readUnsignedShort()))) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		ByteBuf out = alloc.buffer();
		BufferUtils.writeString(out, sender.getSecurityKey());
		return new ResultPacket(id, out);
	}

	public int getId() {
		return ConnectPacketConstants.requestAsServer;
	}

}
