package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.GetWhoamiRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetWhoamiRequestEncoder implements RequestEncoder<GetWhoamiRequest> {

	public void encode(GetWhoamiRequest request, ByteBuf buffer) {
		// no payload
	}

	public int getId() {
		return ConnectPacketConstants.requestGetWhoami;
	}

	public Class<GetWhoamiRequest> getRequest() {
		return GetWhoamiRequest.class;
	}

}
