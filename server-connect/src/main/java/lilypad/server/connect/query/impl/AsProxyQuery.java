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

public class AsProxyQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(sender.getRole() != NodeSessionRole.AUTHENTICATED) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String inboundIp = BufferUtils.readString(in);
		if(inboundIp.length() == 0) {
			inboundIp = sender.getAddress().getAddress().getHostAddress();
		}
		if(!sender.markProxy(new InetSocketAddress(inboundIp, in.readUnsignedShort()),
				BufferUtils.readString(in), 
				BufferUtils.readString(in), 
				in.readUnsignedShort())) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
	}

	public int getId() {
		return ConnectPacketConstants.requestAsProxy;
	}

}
