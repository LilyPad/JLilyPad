package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.NotifyPlayerRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class NotifyPlayerRequestEncoder implements RequestEncoder<NotifyPlayerRequest> {

	public void encode(NotifyPlayerRequest request, ByteBuf buffer) {
		buffer.writeBoolean(request.isAdding());
		BufferUtils.writeString(buffer, request.getPlayer());
	}

	public int getId() {
		return ConnectPacketConstants.requestNotifyPlayer;
	}

	public Class<NotifyPlayerRequest> getRequest() {
		return NotifyPlayerRequest.class;
	}

}
