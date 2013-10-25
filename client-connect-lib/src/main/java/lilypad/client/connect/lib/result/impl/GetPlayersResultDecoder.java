package lilypad.client.connect.lib.result.impl;

import java.util.HashSet;
import java.util.Set;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.util.BufferUtils;
import lilypad.packet.connect.ConnectPacketConstants;

public class GetPlayersResultDecoder implements ResultDecoder<GetPlayersResult> {

	public GetPlayersResult decode(StatusCode statusCode, ByteBuf buffer) {
		if(statusCode == StatusCode.SUCCESS) {
			boolean hasList = buffer.readBoolean();
			int currentPlayers = buffer.readUnsignedShort();
			int maximumPlayers = buffer.readUnsignedShort();
			Set<String> players = new HashSet<String>();
			if(hasList) {
				for(int i = 0; i < currentPlayers; i++) {
					players.add(BufferUtils.readString(buffer));
				}
			}
			return new GetPlayersResult(currentPlayers, maximumPlayers, players);
		} else {
			return new GetPlayersResult(statusCode);
		}
	}

	public int getId() {
		return ConnectPacketConstants.requestGetPlayers;
	}

	public Class<GetPlayersResult> getResult() {
		return GetPlayersResult.class;
	}

}
