package lilypad.client.connect.api.event;

import java.util.UUID;

/**
 * Called when a player has been left the network
 * when the session is authenticated.
 */
public class PlayerLeaveEvent extends Event {

	private String playerName;
	private UUID playerUUID;

	/**
	 *
	 * @param playerName the name of the player that has left
	 * @param playerUUID the uuid of the player that has left
	 */
	public PlayerLeaveEvent(String playerName, UUID playerUUID) {
		this.playerName = playerName;
		this.playerUUID = playerUUID;
	}
	
	/**
	 * 
	 * @return the name of the player that has left
	 */
	public String getName() {
		return this.playerName;
	}
	
	/**
	 * 
	 * @return the uuid of the player that has left
	 */
	public UUID getUUID() {
		return this.playerUUID;
	}
	
}
