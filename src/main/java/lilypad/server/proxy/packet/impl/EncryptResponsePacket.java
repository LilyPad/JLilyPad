package lilypad.server.proxy.packet.impl;

import lilypad.packet.common.Packet;

public class EncryptResponsePacket extends Packet {

	public static final int opcode = 0xFC;
	
	private byte[] sharedSecret;
	private byte[] serverVerification;
	
	public EncryptResponsePacket() {
		this(new byte[0], new byte[0]);
	}
	
	public EncryptResponsePacket(byte[] sharedSecret, byte[] serverVerification) {
		super(0xFC);
		this.sharedSecret = sharedSecret;
		this.serverVerification = serverVerification;
	}
	
	public byte[] getSharedSecret() {
		return this.sharedSecret;
	}
	
	public byte[] getServerVerification() {
		return this.serverVerification;
	}
	
}
