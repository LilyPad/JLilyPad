package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.AsServerRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class AsServerRequestEncoder implements RequestEncoder<AsServerRequest> {

	public void encode(AsServerRequest request, ByteBuf buffer) {
		if(request.getIp() == null) {
			buffer.writeShort(0);
		} else {
			BufferUtils.writeString(buffer, request.getIp());
		}
		buffer.writeShort(request.getPort());
	}

	public int getId() {
		return ConnectPacketConstants.requestAsServer;
	}

	public Class<AsServerRequest> getRequest() {
		return AsServerRequest.class;
	}

}
