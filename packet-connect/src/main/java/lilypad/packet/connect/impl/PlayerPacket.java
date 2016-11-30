package lilypad.packet.connect.impl;

import lilypad.packet.common.Packet;

import java.util.UUID;

public class PlayerPacket extends Packet {

	public static final int opcode = 0x06;

    private boolean joining;
    private String playerName;
	private UUID playerUUID;

	public PlayerPacket(boolean joining, String playerName, UUID playerUUID) {
		super(opcode);
        this.joining = joining;
        this.playerName = playerName;
        this.playerUUID = playerUUID;
	}
	
	public boolean isJoining() {
		return this.joining;
	}

    public String getPlayerName() {
        return playerName;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
