package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.RedirectResult;

public class RedirectRequest implements Request<RedirectResult> {

	private String server;
	private String player;
	
	public RedirectRequest(String server, String player) {
		this.server = server;
		this.player = player;
	}

	public Class<RedirectResult> getResult() {
		return RedirectResult.class;
	}
	
	public String getServer() {
		return this.server;
	}
	
	public String getPlayer() {
		return this.player;
	}

}
