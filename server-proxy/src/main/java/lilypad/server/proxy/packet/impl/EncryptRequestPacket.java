package lilypad.server.proxy.packet.impl;

import java.security.PublicKey;

import lilypad.packet.common.Packet;

public class EncryptRequestPacket extends Packet {

	public static final int opcode = 0xFD;
	
	private String serverKey;
	private PublicKey publicKey;
	private byte[] serverVerification;
	
	public EncryptRequestPacket(String serverKey, PublicKey publicKey, byte[] serverVerification) {
		super(opcode);
		this.serverKey = serverKey;
		this.publicKey = publicKey;
		this.serverVerification = serverVerification;
	}
	
	public String getServerKey() {
		return this.serverKey;
	}
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	public byte[] getServerVerification() {
		return this.serverVerification;
	}

}
