package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.GetDetailsRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetDetailsRequestEncoder implements RequestEncoder<GetDetailsRequest> {

	public void encode(GetDetailsRequest request, ByteBuf buffer) {
		// no payload
	}

	public int getId() {
		return ConnectPacketConstants.requestGetDetails;
	}

	public Class<GetDetailsRequest> getRequest() {
		return GetDetailsRequest.class;
	}

}
