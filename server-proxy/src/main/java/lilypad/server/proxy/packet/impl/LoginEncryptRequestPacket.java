package lilypad.server.proxy.packet.impl;

import java.security.PublicKey;

import lilypad.packet.common.Packet;

public class LoginEncryptRequestPacket extends Packet {

	public static final int opcode = 0x01;
	
	private String serverId;
	private PublicKey publicKey;
	private byte[] verifyToken;
	
	public LoginEncryptRequestPacket(String serverId, PublicKey publicKey, byte[] verifyToken) {
		super(opcode);
		this.serverId = serverId;
		this.publicKey = publicKey;
		this.verifyToken = verifyToken;
	}

	public String getServerId() {
		return this.serverId;
	}

	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}
	
}
