package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.AsProxyRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class AsProxyRequestEncoder implements RequestEncoder<AsProxyRequest> {

	public void encode(AsProxyRequest request, ByteBuf buffer) {
		if(request.getIp() == null) {
			BufferUtils.writeVarInt(buffer, 0);
		} else {
			BufferUtils.writeString(buffer, request.getIp());
		}
		buffer.writeShort(request.getPort());
		BufferUtils.writeString(buffer, request.getMotd());
		BufferUtils.writeString(buffer, request.getVersion());
		buffer.writeShort(request.getMaximumPlayers());
	}

	public int getId() {
		return ConnectPacketConstants.requestAsProxy;
	}

	public Class<AsProxyRequest> getRequest() {
		return AsProxyRequest.class;
	}

}
