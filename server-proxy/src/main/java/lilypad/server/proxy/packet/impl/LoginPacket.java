package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class LoginPacket extends Packet {

	public static final int opcode = 0x01;
	
	private int entityId;
	private String levelType;
	private int gamemode;
	private int dimension;
	private int difficulty;
	private int height;
	private int maxPlayers;
	
	public LoginPacket(int entityId, String levelType, int gamemode, int dimension, int difficulty, int height, int maxPlayers) {
		super(opcode);
		this.entityId = entityId;
		this.levelType = levelType;
		this.gamemode = gamemode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.height = height;
		this.maxPlayers = maxPlayers;
	}
	
	public int getEntityId() {
		return this.entityId;
	}
	
	public String getLevelType() {
		return this.levelType;
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
	
	public int getHeight() {
		return this.height;
	}
	
	public int getMaxPlayers() {
		return this.maxPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	
}
