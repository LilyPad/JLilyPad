package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.AsProxyRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class AsProxyRequestEncoder implements RequestEncoder<AsProxyRequest> {

	public void encode(AsProxyRequest request, ByteBuf buffer) {
		if(request.getIp() != null) {
			BufferUtils.writeString16(request.getIp(), buffer);
		} else {
			buffer.writeShort(0);
		}
		buffer.writeShort(request.getPort());
		BufferUtils.writeString16(request.getMotd(), buffer);
		BufferUtils.writeString16(request.getVersion(), buffer);
		buffer.writeShort(request.getMaximumPlayers());
	}

	public int getId() {
		return ConnectPacketConstants.requestAsProxy;
	}

	public Class<AsProxyRequest> getRequest() {
		return AsProxyRequest.class;
	}

}
