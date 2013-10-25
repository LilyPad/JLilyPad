package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetDetailsResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetDetailsResultDecoder implements ResultDecoder<GetDetailsResult> {

	public GetDetailsResult decode(StatusCode statusCode, ByteBuf buffer) {
		if(statusCode == StatusCode.SUCCESS) {
			return new GetDetailsResult(BufferUtils.readString16(buffer), buffer.readUnsignedShort(), BufferUtils.readString16(buffer), BufferUtils.readString16(buffer));
		} else {
			return new GetDetailsResult(statusCode);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestGetDetails;
	}

	public Class<GetDetailsResult> getResult() {
		return GetDetailsResult.class;
	}

}
