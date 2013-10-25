package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayRespawnPacket extends Packet {

	public static final int opcode = 0x07;
	
	private int dimension;
	private int difficulty;
	private int gamemode;
	private String levelType;
	
	public PlayRespawnPacket(int dimension, int difficulty, int gamemode, String levelType) {
		super(opcode);
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.levelType = levelType;
	}

	public int getDimension() {
		return this.dimension;
	}

	public int getDifficulty() {
		return this.difficulty;
	}

	public int getGamemode() {
		return this.gamemode;
	}

	public String getLevelType() {
		return this.levelType;
	}

}
