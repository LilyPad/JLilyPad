package lilypad.client.connect.api.request.impl;

import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.result.impl.RedirectResult;

/**
 * Request asking the network to redirect a player to a specified
 * server.
 */
public class RedirectRequest implements Request<RedirectResult> {

	private String server;
	private String player;
	
	/**
	 * 
	 * @param server to be redirected to
	 * @param player to be redirected
	 */
	public RedirectRequest(String server, String player) {
		this.server = server;
		this.player = player;
	}

	/**
	 * 
	 * @return accompanying result of the request
	 */
	public Class<RedirectResult> getResult() {
		return RedirectResult.class;
	}
	
	/**
	 * 
	 * @return the server to be redirected to
	 */
	public String getServer() {
		return this.server;
	}
	
	/**
	 * 
	 * @return the player to be redirected
	 */
	public String getPlayer() {
		return this.player;
	}

}
