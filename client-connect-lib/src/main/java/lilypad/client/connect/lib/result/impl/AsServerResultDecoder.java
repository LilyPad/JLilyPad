package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.AsServerResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class AsServerResultDecoder implements ResultDecoder<AsServerResult> {

	public AsServerResult decode(StatusCode statusCode, ByteBuf buffer) {
		if(statusCode == StatusCode.SUCCESS) {
			return new AsServerResult(BufferUtils.readString(buffer));
		} else {
			return new AsServerResult(statusCode);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestAsServer;
	}

	public Class<AsServerResult> getResult() {
		return AsServerResult.class;
	}

}
