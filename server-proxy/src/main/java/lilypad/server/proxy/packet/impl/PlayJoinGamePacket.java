package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayJoinGamePacket extends Packet {

	public static final int opcode = 0x01;
	
	private int entityId;
	private int gamemode;
	private int dimension;
	private int difficulty;
	private int maxPlayers;
	private String levelType;
	
	public PlayJoinGamePacket(int entityId, int gamemode, int dimension, int difficulty, int maxPlayers, String levelType) {
		super(opcode);
		this.entityId = entityId;
		this.gamemode = gamemode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
		this.levelType = levelType;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public int getGamemode() {
		return this.gamemode;
	}
	
	public int getDimension() {
		return this.dimension;
	}
	
	public int getDifficulty() {
		return this.difficulty;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
	public String getLevelType() {
		return this.levelType;
	}

}
