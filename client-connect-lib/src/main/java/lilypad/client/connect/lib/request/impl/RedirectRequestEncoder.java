package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.RedirectRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class RedirectRequestEncoder implements RequestEncoder<RedirectRequest> {

	public void encode(RedirectRequest request, ByteBuf buffer) {
		BufferUtils.writeString(buffer, request.getServer());
		BufferUtils.writeString(buffer, request.getPlayer());
	}

	public int getId() {
		return ConnectPacketConstants.requestRedirect;
	}

	public Class<RedirectRequest> getRequest() {
		return RedirectRequest.class;
	}

}
