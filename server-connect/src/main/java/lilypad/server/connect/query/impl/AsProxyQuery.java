package lilypad.server.connect.query.impl;

import java.net.InetSocketAddress;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionRole;
import lilypad.server.connect.query.Query;

public class AsProxyQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(sender.getRole() != NodeSessionRole.AUTHENTICATED) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		String inboundIp = BufferUtils.readString(payload);
		if(inboundIp.length() == 0) {
			inboundIp = sender.getAddress().getAddress().getHostAddress();
		}
		if(!sender.markProxy(new InetSocketAddress(inboundIp, payload.readUnsignedShort()),
				BufferUtils.readString(payload), 
				BufferUtils.readString(payload), 
				payload.readUnsignedShort())) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
		return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
	}

	public int getId() {
		return ConnectPacketConstants.requestAsProxy;
	}

}
