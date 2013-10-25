package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.GetKeyRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetKeyRequestEncoder implements RequestEncoder<GetKeyRequest> {

	public void encode(GetKeyRequest request, ByteBuf buffer) {
		// no payload
	}

	public int getId() {
		return ConnectPacketConstants.requestGetKey;
	}

	public Class<GetKeyRequest> getRequest() {
		return GetKeyRequest.class;
	}

}
