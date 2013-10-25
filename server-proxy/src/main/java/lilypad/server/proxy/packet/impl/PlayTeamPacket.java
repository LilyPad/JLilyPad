package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayTeamPacket extends Packet {

	public static final int opcode = 0x3E;
	
	private String name;
	private int mode;
	private String displayName;
	private String prefix;
	private String suffix;
	private int friendlyFire;
	private String[] players;
	
	public PlayTeamPacket(String name, int mode, String displayName, String prefix, String suffix, int friendlyFire, String[] players) {
		super(opcode);
		this.name = name;
		this.mode = mode;
		this.displayName = displayName;
		this.prefix = prefix;
		this.suffix = suffix;
		this.friendlyFire = friendlyFire;
		this.players = players;
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getMode() {
		return this.mode;
	}
	
	public boolean isCreating() {
		return this.mode == 0;
	}
	
	public boolean isRemoving() {
		return this.mode == 1;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public String getSuffix() {
		return this.suffix;
	}

	public int getFriendlyFire() {
		return this.friendlyFire;
	}
	
	public String[] getPlayers() {
		return this.players;
	}
	
}
