package lilypad.client.connect.api.event;

import java.util.UUID;

/**
 * Called when a player has been joined the network
 * when the session is authenticated.
 */
public class PlayerJoinEvent extends Event {

	private String playerName;
	private UUID playerUUID;

	/**
	 *
	 * @param playerName the name of the player that has joined
	 * @param playerUUID the uuid of the player that has joined
	 */
	public PlayerJoinEvent(String playerName, UUID playerUUID) {
		this.playerName = playerName;
		this.playerUUID = playerUUID;
	}
	
	/**
	 * 
	 * @return the name of the player that has joined
	 */
	public String getName() {
		return this.playerName;
	}
	
	/**
	 * 
	 * @return the uuid of the player that has joined
	 */
	public UUID getUUID() {
		return this.playerUUID;
	}
	
}
