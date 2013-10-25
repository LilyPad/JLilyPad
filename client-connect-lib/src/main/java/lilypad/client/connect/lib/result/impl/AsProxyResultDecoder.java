package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.AsProxyResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class AsProxyResultDecoder implements ResultDecoder<AsProxyResult> {

	public AsProxyResult decode(StatusCode statusCode, ByteBuf buffer) {
		return new AsProxyResult(statusCode);
	}

	public int getId() {
		return ConnectPacketConstants.requestAsProxy;
	}

	public Class<AsProxyResult> getResult() {
		return AsProxyResult.class;
	}

}
