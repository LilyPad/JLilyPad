package lilypad.server.proxy.packet;

import lilypad.server.proxy.packet.GenericPacketUnitArray.OpPair;
import io.netty.buffer.ByteBuf;
import lilypad.packet.common.PacketCodec;

public class GenericPacketCodec extends PacketCodec<GenericPacket> {

	public GenericPacketCodec(int opcode) {
		super(opcode);
	}

	public GenericPacket decode(ByteBuf buffer) throws Exception {
		return new GenericPacket(super.getOpcode(), decode(buffer, GenericPacketUnitArray.opPairs[super.getOpcode()]));
	}
	
	public static ByteBuf decode(ByteBuf buffer, OpPair[] opPairs) throws Exception {
		int start = buffer.readerIndex();
		int position = start;
		int a;
		int b;
		int c;
		int d;
		for(OpPair opPair : opPairs) {
			switch(opPair.getOperation()) {
			case JUMP_FIXED:
				position += opPair.getParameter();
				break;
			case BYTE_SIZED:
				a = buffer.getUnsignedByte(position);
				position += a + 1;
				break;
			case BYTE_SIZED_QUAD:
				a = buffer.getUnsignedByte(position);
				position += (a * 4) + 1;
				break;
			case SHORT_SIZED:
				a = buffer.getShort(position);
				if(a > 0) {
					position += a + 2;
				} else {
					position += 2;
				}
				break;
			case SHORT_SIZED_DOUBLED:
				a = buffer.getUnsignedShort(position);
				position += (a * 2) + 2;
				break;
			case SHORT_SIZED_QUAD:
				a = buffer.getUnsignedShort(position);
				position += (a * 4) + 2;
				break;
			case INT_SIZED:
				a = buffer.getInt(position);
				position += a + 4;
				break;
			case INT_SIZED_TRIPLE:
				a = buffer.getInt(position);
				position += (a * 3) + 4;
				break;
			case INT_SIZED_QUAD:
				a = buffer.getInt(position);
				position += (a * 4) + 4;
				break;
			case ENTITY_META_DATA:
				while(true) {
					b = ((a = buffer.getByte(position)) & 0xE0) >> 5;
					position += 1;
					if(a == 127) {
						break;
					}
					switch(b) {
					case 0:
						position += 1;
						break;
					case 1:
						position += 2;
						break;
					case 2:
					case 3:
						position += 4;
						break;
					case 4:
						c = buffer.getUnsignedShort(position);
						position += (c * 2) + 2;
						break;
					case 5:
						c = buffer.getShort(position);
						if(c > 0) {
							position += 5;
							c = buffer.getShort(position);
							if(c > 0) {
								position += c + 2;
							} else {
								position += 2;
							}
						} else {
							position += 2;
						}
						break;
					case 6:
						position += 12;
						break;
					}
				}
				break;
			case MAP_DATA:
				a = buffer.getUnsignedShort(position);
				position += 2;
				b = buffer.getInt(position);
				position += 5 + b + (a * 12);
				break;
			case TEAM_DATA:
				a = buffer.getUnsignedByte(position);
				position += 1;
				if(a == 0 || a == 2) {
					b = buffer.getUnsignedShort(position);
					position += (b * 2) + 2;
					b = buffer.getUnsignedShort(position);
					position += (b * 2) + 2;
					b = buffer.getUnsignedShort(position);
					position += (b * 2) + 3;
				}
				if(a == 0 || a == 3 || a == 4) {
					b = buffer.getUnsignedShort(position);
					position += 2;
					for(c = 0; c < b; c++) {
						d = buffer.getUnsignedShort(position);
						position += (d * 2) + 2;
					}
				}
				break;
			case WINDOW_DATA:
				a = buffer.getUnsignedByte(position);
				position += 1;
				b = buffer.getUnsignedShort(position);
				position += (b * 2) + 2 + 1 + 1;
				if(a == 11) {
					position += 4;
				}
				break;
			case SCOREBOARD_DATA:
				a = buffer.getByte(position);
				position += 1;
				if(a != -1) {
					b = buffer.getUnsignedShort(position);
					position += (b * 2) + 2 + 4;
				}
				break;
			case OPTIONAL_MOTION:
				a = buffer.getInt(position);
				if(a > 0) {
					position += 10;
				} else {
					position += 4;
				}
				break;
			case ITEM:
				a = buffer.getShort(position);
				if(a != -1) {
					position += 5;
					b = buffer.getShort(position);
					if(b > 0) {
						position += b + 2;
					} else {
						position += 2;
					}
				} else {
					position += 2;
				}
				break;
			case ITEM_ARRAY:
				a = buffer.getShort(position);
				position += 2;
				for(b = 0; b < a ; b++) {
					c = buffer.getShort(position);
					if(c != -1) {
						position += 5;
						d = buffer.getShort(position);
						if(d > 0) {
							position += d + 2;
						} else {
							position += 2;
						}
					} else {
						position += 2;
					}
				}
				break;
			case ENTITY_PROPERTIES:
				a = buffer.getInt(position);
				position += 4;
				for(b = 0; b < a; b++) {
					c = buffer.getShort(position);
					position += (2 * c) + 2 + 8;
					d = buffer.getShort(position);
					position += (d * (8 + 8 + 8 + 1)) + 2;
				}
				break;
			}
			if(position - start > CraftPacketConstants.maxPacketSize) {
				throw new Exception("Max packet size passed, dropping");
			}
		}
		return buffer.readBytes(position - start);
	}

	public void encode(GenericPacket packet, ByteBuf buffer) {
		ByteBuf toWrite = packet.getBuffer();
		buffer.writeBytes(toWrite);
		toWrite.discardReadBytes();
		toWrite.release();
	}

}
