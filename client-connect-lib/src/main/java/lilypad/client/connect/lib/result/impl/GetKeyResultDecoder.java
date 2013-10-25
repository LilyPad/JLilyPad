package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetKeyResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetKeyResultDecoder implements ResultDecoder<GetKeyResult> {

	public GetKeyResult decode(StatusCode statusCode, ByteBuf buffer) {
		if(statusCode == StatusCode.SUCCESS) {
			return new GetKeyResult(BufferUtils.readString16(buffer));
		} else {
			return new GetKeyResult(statusCode);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestGetKey;
	}

	public Class<GetKeyResult> getResult() {
		return GetKeyResult.class;
	}

}
