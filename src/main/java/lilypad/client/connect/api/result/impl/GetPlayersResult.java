package lilypad.client.connect.api.result.impl;

import java.util.Set;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetPlayersResult extends Result {

	private int currentPlayers;
	private int maximumPlayers;
	private Set<String> players;
	
	public GetPlayersResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	public GetPlayersResult(int currentPlayers, int maximumPlayers, Set<String> players) {
		super(StatusCode.SUCCESS);
		this.currentPlayers = currentPlayers;
		this.maximumPlayers = maximumPlayers;
		this.players = players;
	}
	
	public int getCurrentPlayers() {
		return this.currentPlayers;
	}
	
	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}
	
	public Set<String> getPlayers() {
		return this.players;
	}

}
