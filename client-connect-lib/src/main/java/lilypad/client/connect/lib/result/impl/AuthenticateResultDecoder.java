package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.AuthenticateResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class AuthenticateResultDecoder implements ResultDecoder<AuthenticateResult> {

	public AuthenticateResult decode(StatusCode statusCode, ByteBuf buffer) {
		return new AuthenticateResult(statusCode);
	}

	public int getId() {
		return ConnectPacketConstants.requestAuthenticate;
	}

	public Class<AuthenticateResult> getResult() {
		return AuthenticateResult.class;
	}

}
