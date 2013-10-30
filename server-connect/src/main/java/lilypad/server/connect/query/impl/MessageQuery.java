package lilypad.server.connect.query.impl;

import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.MessagePacket;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionMapper;
import lilypad.server.connect.query.Query;

public class MessageQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf in, ByteBufAllocator alloc) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		int recipientsCount = in.readUnsignedShort();
		Set<String> recipients = new HashSet<String>();
		while(recipientsCount-- != 0) {
			recipients.add(BufferUtils.readString(in));
		}
		String senderId = sender.getIdentification();
		String channel = BufferUtils.readString(in);
		ByteBuf payload = in.readBytes(in.readUnsignedShort());
		boolean messageSent = false;
		if(recipients.isEmpty()) {
			messageSent = true;
			for(NodeSession otherSession : sender.getConnectService().getSessionMapper().getAuthenticated()) {
				otherSession.write(new MessagePacket(senderId, channel, payload.copy()));
			}
		} else {
			NodeSession otherSession;
			NodeSessionMapper sessionMapper = sender.getConnectService().getSessionMapper();
			for(String recipient : recipients) {
				otherSession = sessionMapper.getAuthenticatedByUsername(recipient);
				if(otherSession == null) {
					continue;
				}
				otherSession.write(new MessagePacket(senderId, channel, payload.copy()));
				messageSent = true;
			}
		}
		if(messageSent) {
			return new ResultPacket(id, ConnectPacketConstants.statusSuccess);
		} else {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidGeneric);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestMessage;
	}

}
