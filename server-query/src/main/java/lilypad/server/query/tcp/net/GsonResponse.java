package lilypad.server.query.tcp.net;

import java.util.Set;

import lilypad.server.common.util.GsonUtils;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class GsonResponse {
	
	private int serverPort;
	private int playerCount;
	private int maxPlayers;
	private Set<String> playerList;
	
	public GsonResponse(int serverPort, int maxPlayers, Set<String> players) {
		this.serverPort = serverPort;
		this.playerCount = players.size();
		this.maxPlayers = maxPlayers;
		this.playerList = players;
	}
	
	public String toGson() {
		return GsonUtils.gson().toJson(this);
	}
	
}
