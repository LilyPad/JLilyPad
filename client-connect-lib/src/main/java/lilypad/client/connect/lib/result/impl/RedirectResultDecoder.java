package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.RedirectResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class RedirectResultDecoder implements ResultDecoder<RedirectResult> {

	public RedirectResult decode(StatusCode statusCode, ByteBuf buffer) {
		return new RedirectResult(statusCode);
	}

	public int getId() {
		return ConnectPacketConstants.requestRedirect;
	}

	public Class<RedirectResult> getResult() {
		return RedirectResult.class;
	}

}
