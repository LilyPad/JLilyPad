package lilypad.client.connect.api.result.impl;

import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public class GetWhoamiResult extends Result {

	private String identification;
	
	/**
	 * Called only when the result is unsuccessful.
	 * 
	 * @param statusCode of the result
	 */
	public GetWhoamiResult(StatusCode statusCode) {
		super(statusCode);
	}
	
	/**
	 * Showing the result was successful, passing the data
	 * to accompany the result.
	 * 
	 * @param identification
	 */
	public GetWhoamiResult(String identification) {
		super(StatusCode.SUCCESS);
		this.identification = identification;
	}
	
	/**
	 * The network will identify you differently based on your state.
	 * If you are unauthenticated, your identification will be blank, while if
	 * you are authenticated, your identification will be [username].[uniqueInt],
	 * and if you are either a server or a proxy, your identification will simply
	 * be [username].
	 * 
	 * @return identification by the network
	 */
	public String getIdentification() {
		return this.identification;
	}

}
