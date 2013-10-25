package lilypad.server.proxy.packet;

import io.netty.buffer.ByteBuf;
import lilypad.packet.common.Packet;

public class GenericPacket extends Packet {

	private ByteBuf buffer;
	
	public GenericPacket(int opcode, ByteBuf buffer) {
		super(opcode);
		this.buffer = buffer;
	}
	
	public ByteBuf getBuffer() {
		return this.buffer;
	}
	
	public void swapEntityId(int a, int b) {
		if(a == b) {
			return;
		}
		int[] entityIdPositions = CraftPacketConstants.entityIdPositions[this.getOpcode()];
		if(entityIdPositions != null) {
			int id;
			for(int position : entityIdPositions) {
				id = this.buffer.getInt(position);
				if(id == a) {
					this.buffer.setInt(position, b);
				} else if(id == b) {
					this.buffer.setInt(position, a);
				}
			}
		}
	}

}
