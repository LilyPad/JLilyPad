package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.MessageResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class MessageResultDecoder implements ResultDecoder<MessageResult> {

	public MessageResult decode(StatusCode statusCode, ByteBuf buffer) {
		return new MessageResult(statusCode);
	}

	public int getId() {
		return ConnectPacketConstants.requestMessage;
	}

	public Class<MessageResult> getResult() {
		return MessageResult.class;
	}

}
