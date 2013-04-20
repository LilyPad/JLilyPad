package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetPlayersRequestEncoder implements RequestEncoder<GetPlayersRequest> {

	public void encode(GetPlayersRequest request, ByteBuf buffer) {
		buffer.writeBoolean(request.getAsList());
	}

	public int getId() {
		return ConnectPacketConstants.requestGetPlayers;
	}

	public Class<GetPlayersRequest> getRequest() {
		return GetPlayersRequest.class;
	}

}
