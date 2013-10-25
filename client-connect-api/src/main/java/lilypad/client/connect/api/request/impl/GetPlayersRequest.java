package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.GetPlayersResult;

/**
 * Request to get an accurate representation of all players
 * on the network.
 */
public class GetPlayersRequest implements Request<GetPlayersResult> {

	private boolean asList;
	
	/**
	 * Shortcut to dictate that we needn't a list of every
	 * player.
	 */
	public GetPlayersRequest() {
		this(false);
	}
	
	/**
	 * 
	 * @param asList if a list of every player is required
	 */
	public GetPlayersRequest(boolean asList) {
		this.asList = asList;
	}
	
	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<GetPlayersResult> getResult() {
		return GetPlayersResult.class;
	}
	
	/**
	 * 
	 * @return if a list of every player is required
	 */
	public boolean getAsList() {
		return this.asList;
	}

}
