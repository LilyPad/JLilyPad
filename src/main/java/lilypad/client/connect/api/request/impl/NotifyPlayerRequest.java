package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.NotifyPlayerResult;

/**
 * Request to notify the network that a player has been added
 * or removed from our proxy.
 */
public class NotifyPlayerRequest implements Request<NotifyPlayerResult> {

	private boolean addOrRemove;
	private String player;
	
	/**
	 * 
	 * @param addOrRemove true if adding, false if removing
	 * @param player in question
	 */
	public NotifyPlayerRequest(boolean addOrRemove, String player) {
		this.addOrRemove = addOrRemove;
		this.player = player;
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<NotifyPlayerResult> getResult() {
		return NotifyPlayerResult.class;
	}
	
	/**
	 * 
	 * @return if this is a request to add
	 */
	public boolean isAdding() {
		return this.addOrRemove;
	}
	
	/**
	 * 
	 * @return if this is a request to remove
	 */
	public boolean isRemoving() {
		return !this.addOrRemove;
	}
	
	/**
	 * 
	 * @return the player in question
	 */
	public String getPlayer() {
		return this.player;
	}

}
