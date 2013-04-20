package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.NotifyPlayerResult;

public class NotifyPlayerRequest implements Request<NotifyPlayerResult> {

	private boolean addOrRemove;
	private String player;
	
	public NotifyPlayerRequest(boolean addOrRemove, String player) {
		this.addOrRemove = addOrRemove;
		this.player = player;
	}

	public Class<NotifyPlayerResult> getResult() {
		return NotifyPlayerResult.class;
	}
	
	public boolean isAdding() {
		return this.addOrRemove;
	}
	
	public boolean isRemoving() {
		return !this.addOrRemove;
	}
	
	public String getPlayer() {
		return this.player;
	}

}
