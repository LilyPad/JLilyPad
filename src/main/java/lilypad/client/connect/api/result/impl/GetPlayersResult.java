package lilypad.client.connect.api.result.impl;

import java.util.Set;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetPlayersResult extends Result {

	private int currentPlayers;
	private int maximumPlayers;
	private Set<String> players;
	
	/**
	 * Called only when the result is unsuccessful.
	 * 
	 * @param statusCode of the result
	 */
	public GetPlayersResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	/**
	 * Showing the result was successful, passing the data
	 * to accompany the result.
	 * 
	 * @param currentPlayers
	 * @param maximumPlayers
	 * @param players
	 */
	public GetPlayersResult(int currentPlayers, int maximumPlayers, Set<String> players) {
		super(StatusCode.SUCCESS);
		this.currentPlayers = currentPlayers;
		this.maximumPlayers = maximumPlayers;
		this.players = players;
	}
	
	/**
	 * An accurate representation of the current player count
	 * as reported by the network
	 * 
	 * @return current players
	 */
	public int getCurrentPlayers() {
		return this.currentPlayers;
	}
	
	/**
	 * The maximum players allowed on the network. This is normally
	 * calculated through the sum of all proxies' maximum player count,
	 * however when at least one proxies' player count is below 2,
	 * it will return the single proxies' player count instead
	 * 
	 * @return max players
	 */
	public int getMaximumPlayers() {
		return this.maximumPlayers;
	}
	
	/**
	 * If the original request asked for the list of players, an accurate
	 * representation of all players on the network will be returned.
	 * Otherwise, it will return null.
	 * 
	 * @return set of players
	 */
	public Set<String> getPlayers() {
		return this.players;
	}

}
