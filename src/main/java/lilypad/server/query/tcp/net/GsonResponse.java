package lilypad.server.query.tcp.net;

import java.util.Set;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class GsonResponse {

	private static final Gson gson = new Gson();
	
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
		return gson.toJson(this);
	}
	
}
