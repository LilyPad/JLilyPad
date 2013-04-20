package lilypad.packet.common;

public abstract class Packet {
	
	private int opcode;
	
	public Packet(int opcode) {
		this.opcode = opcode;
	}
	
	public int getOpcode() {
		return this.opcode;
	}
	
}
