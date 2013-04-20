package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetWhoamiResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetWhoamiResultDecoder implements ResultDecoder<GetWhoamiResult> {

	public GetWhoamiResult decode(StatusCode statusCode, ByteBuf buffer) {
		if(statusCode == StatusCode.SUCCESS) {
			return new GetWhoamiResult(BufferUtils.readString16(buffer));
		} else {
			return new GetWhoamiResult(statusCode);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestGetWhoami;
	}

	public Class<GetWhoamiResult> getResult() {
		return GetWhoamiResult.class;
	}

}
