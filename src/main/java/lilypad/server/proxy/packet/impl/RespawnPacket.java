package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class RespawnPacket extends Packet {

	public static final int opcode = 0x09;
	
	private int dimension;
	private int difficulty;
	private int gamemode;
	private int height;
	private String levelType;
	
	public RespawnPacket(int dimension, int difficulty, int gamemode, int height, String levelType) {
		super(opcode);
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.gamemode = gamemode;
		this.height = height;
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
	
	public int getHeight() {
		return this.height;
	}
	
	public String getLevelType() {
		return this.levelType;
	}
	
}
