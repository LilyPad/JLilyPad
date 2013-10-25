package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class LoginEncryptResponsePacket extends Packet {

	public static final int opcode = 0x01;
	
	private byte[] sharedSecret;
	private byte[] verifyToken;
	
	public LoginEncryptResponsePacket(byte[] sharedSecret, byte[] verifyToken) {
		super(opcode);
		this.sharedSecret = sharedSecret;
		this.verifyToken = verifyToken;
	}

	public byte[] getSharedSecret() {
		return this.sharedSecret;
	}

	public byte[] getVerifyToken() {
		return this.verifyToken;
	}
	
}
