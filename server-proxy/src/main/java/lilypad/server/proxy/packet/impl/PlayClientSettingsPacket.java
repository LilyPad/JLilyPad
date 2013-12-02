package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class PlayClientSettingsPacket extends Packet {

	public static final int opcode = 0x15;
	
	private String locale;
	private byte viewDistance;
	private byte chatFlags;
	private boolean unused;
	private byte difficulty;
	private boolean showCape;
	
	public PlayClientSettingsPacket(String locale, byte viewDistance, byte chatFlags, boolean unused, byte difficulty, boolean showCape) {
		super(opcode);
		this.locale = locale;
		this.viewDistance = viewDistance;
		this.chatFlags = chatFlags;
		this.unused = unused;
		this.difficulty = difficulty;
		this.showCape = showCape;
	}

	public String getLocale() {
		return this.locale;
	}

	public byte getViewDistance() {
		return this.viewDistance;
	}

	public byte getChatFlags() {
		return this.chatFlags;
	}

	public boolean isUnused() {
		return this.unused;
	}

	public byte getDifficulty() {
		return this.difficulty;
	}

	public boolean isShowCape() {
		return this.showCape;
	}
	
}
