package lilypad.client.connect.lib.request.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.impl.AuthenticateRequest;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class AuthenticateRequestEncoder implements RequestEncoder<AuthenticateRequest> {

	public void encode(AuthenticateRequest request, ByteBuf buffer) {
		BufferUtils.writeString(buffer, request.getUsername());
		BufferUtils.writeString(buffer, request.getPassword());
	}

	public int getId() {
		return ConnectPacketConstants.requestAuthenticate;
	}

	public Class<AuthenticateRequest> getRequest() {
		return AuthenticateRequest.class;
	}

}
