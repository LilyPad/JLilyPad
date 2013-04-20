package lilypad.client.connect.lib.result.impl;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.NotifyPlayerResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.connect.ConnectPacketConstants;

public class NotifyPlayerResultDecoder implements ResultDecoder<NotifyPlayerResult> {

	public NotifyPlayerResult decode(StatusCode statusCode, ByteBuf buffer) {
		return new NotifyPlayerResult(statusCode);
	}

	public int getId() {
		return ConnectPacketConstants.requestNotifyPlayer;
	}

	public Class<NotifyPlayerResult> getResult() {
		return NotifyPlayerResult.class;
	}

}
