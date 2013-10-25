package lilypad.server.connect.query.impl;

import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;
import lilypad.packet.connect.impl.MessagePacket;
import lilypad.packet.connect.impl.ResultPacket;
import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.node.NodeSessionMapper;
import lilypad.server.connect.query.Query;

public class MessageQuery implements Query<NodeSession> {

	public ResultPacket execute(NodeSession sender, int id, ByteBuf payload) {
		if(!sender.isAuthenticated()) {
			return new ResultPacket(id, ConnectPacketConstants.statusInvalidRole);
		}
		int recipientsCount = payload.readUnsignedShort();
		Set<String> recipients = new HashSet<String>();
		while(recipientsCount-- != 0) {
			recipients.add(BufferUtils.readString16(payload));
		}
		MessagePacket message = new MessagePacket(sender.getIdentification(), BufferUtils.readString16(payload), payload.readBytes(payload.readUnsignedShort()));
		boolean messageSent = false;
		if(recipients.isEmpty()) {
			messageSent = true;
			for(NodeSession otherSession : sender.getConnectService().getSessionMapper().getAuthenticated()) {
				otherSession.write(message);
			}
		} else {
			NodeSession otherSession;
			NodeSessionMapper sessionMapper = sender.getConnectService().getSessionMapper();
			for(String recipient : recipients) {
				otherSession = sessionMapper.getAuthenticatedByUsername(recipient);
				if(otherSession == null) {
					continue;
				}
				otherSession.write(message);
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
