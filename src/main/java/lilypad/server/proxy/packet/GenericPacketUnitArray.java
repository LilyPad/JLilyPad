package lilypad.server.proxy.packet;

public class GenericPacketUnitArray {

	public enum Op {
		JUMP_FIXED,
		BYTE_SIZED,
		BYTE_SIZED_QUAD,
		SHORT_SIZED,
		SHORT_SIZED_DOUBLED,
		SHORT_SIZED_QUAD,
		INT_SIZED,
		INT_SIZED_TRIPLE,
		INT_SIZED_QUAD,
		ENTITY_META_DATA,
		MAP_DATA,
		TEAM_DATA,
		OPTIONAL_MOTION,
		ITEM,
		ITEM_ARRAY;
	}
	
	public static class OpPair {
		
		private Op operation;
		private int parameter;
		
		OpPair(Op operation) {
			this(operation, 0);
		}
		
		OpPair(Op operation, int parameter) {
			this.operation = operation;
			this.parameter = parameter;
		}
		
		public Op getOperation() {
			return this.operation;
		}
		
		public int getParameter() {
			return this.parameter;
		}
		
	}
	
	public static OpPair byteSized = new OpPair(Op.BYTE_SIZED);
	public static OpPair byteSizedQuad = new OpPair(Op.BYTE_SIZED_QUAD);
	public static OpPair shortSized = new OpPair(Op.SHORT_SIZED);
	public static OpPair shortSizedDoubled = new OpPair(Op.SHORT_SIZED_DOUBLED);
	public static OpPair shortSizedQuad = new OpPair(Op.SHORT_SIZED_QUAD);
	public static OpPair intSized = new OpPair(Op.INT_SIZED);
	public static OpPair intSizedTriple = new OpPair(Op.INT_SIZED_TRIPLE);
	public static OpPair intSizedQuad = new OpPair(Op.INT_SIZED_QUAD);
	public static OpPair entityMetaData = new OpPair(Op.ENTITY_META_DATA);
	public static OpPair mapData = new OpPair(Op.MAP_DATA);
	public static OpPair teamData = new OpPair(Op.TEAM_DATA);
	public static OpPair optionalMotion = new OpPair(Op.OPTIONAL_MOTION);
	public static OpPair item = new OpPair(Op.ITEM);
	public static OpPair itemArray = new OpPair(Op.ITEM_ARRAY);
	
	public static OpPair[][] opPairs = new OpPair[256][];
	
	static {
		opPairs[0x00] = new OpPair[] { jump(4) };
		opPairs[0x01] = new OpPair[] { jump(4), shortSizedDoubled, jump(1 + 1 + 1 + 1 + 1) }; // has another codec
		opPairs[0x02] = new OpPair[] { jump(1), shortSizedDoubled, shortSizedDoubled, jump(4) }; // has another codec
		opPairs[0x03] = new OpPair[] { shortSizedDoubled };
		opPairs[0x04] = new OpPair[] { jump(8 + 8) };
		opPairs[0x05] = new OpPair[] { jump(4 + 2), item };
		opPairs[0x06] = new OpPair[] { jump(4 + 4 + 4) };
		opPairs[0x07] = new OpPair[] { jump(4 + 4 + 1) };
		opPairs[0x08] = new OpPair[] { jump(2 + 2 + 4) };
		opPairs[0x09] = new OpPair[] { jump(4 + 1 + 1 + 2), shortSizedDoubled }; // has another codec
		opPairs[0x0A] = new OpPair[] { jump(1) };
		opPairs[0x0B] = new OpPair[] { jump(8 + 8 + 8 + 8 + 1) };
		opPairs[0x0C] = new OpPair[] { jump(4 + 4 + 1) };
		opPairs[0x0D] = new OpPair[] { jump(8 + 8 + 8 + 8 + 4 + 4 + 1) };
		opPairs[0x0E] = new OpPair[] { jump(1 + 4 + 1 + 4 + 1) };
		opPairs[0x0F] = new OpPair[] { jump(4 + 1 + 4 + 1), item, jump(1 + 1 + 1) };
		opPairs[0x10] = new OpPair[] { jump(2) };
		opPairs[0x11] = new OpPair[] { jump(4 + 1 + 4 + 1 + 4) };
		opPairs[0x12] = new OpPair[] { jump(4 + 1) };
		opPairs[0x13] = new OpPair[] { jump(4 + 1) };
		opPairs[0x14] = new OpPair[] { jump(4), shortSizedDoubled, jump(4 + 4 + 4 + 1 + 1 + 2), entityMetaData };
		opPairs[0x16] = new OpPair[] { jump(4 + 4) };
		opPairs[0x17] = new OpPair[] { jump(4 + 1 + 4 + 4 + 4 + 1 + 1), optionalMotion };
		opPairs[0x18] = new OpPair[] { jump(4 + 1 + 4 + 4 + 4 + 1 + 1 + 1 + 2 + 2 + 2), entityMetaData };
		opPairs[0x19] = new OpPair[] { jump(4), shortSizedDoubled, jump(4 + 4 + 4 + 4) };
		opPairs[0x1A] = new OpPair[] { jump(4 + 4 + 4 + 4 + 2) };
		opPairs[0x1C] = new OpPair[] { jump(4 + 2 + 2 + 2) };
		opPairs[0x1D] = new OpPair[] { byteSizedQuad };
		opPairs[0x1E] = new OpPair[] { jump(4) };
		opPairs[0x1F] = new OpPair[] { jump(4 + 1 + 1 + 1) };
		opPairs[0x20] = new OpPair[] { jump(4 + 1 + 1) };
		opPairs[0x21] = new OpPair[] { jump(4 + 1 + 1 + 1 + 1 + 1) };
		opPairs[0x22] = new OpPair[] { jump(4 + 4 + 4 + 4 + 1 + 1) };
		opPairs[0x23] = new OpPair[] { jump(4 + 1) };
		opPairs[0x26] = new OpPair[] { jump(4 + 1) };
		opPairs[0x27] = new OpPair[] { jump(4 + 4) };
		opPairs[0x28] = new OpPair[] { jump(4), entityMetaData };
		opPairs[0x29] = new OpPair[] { jump(4 + 1 + 1 + 2) };
		opPairs[0x2A] = new OpPair[] { jump(4 + 1) };
		opPairs[0x2B] = new OpPair[] { jump(4 + 2 + 2) };
		opPairs[0x33] = new OpPair[] { jump(4 + 4 + 1 + 2 + 2), intSized };
		opPairs[0x34] = new OpPair[] { jump(4 + 4 + 2), intSized };
		opPairs[0x35] = new OpPair[] { jump(4 + 1 + 4 + 2 + 1) };
		opPairs[0x36] = new OpPair[] { jump(4 + 2 + 4 + 1 + 1 + 2) };
		opPairs[0x37] = new OpPair[] { jump(4 + 4 + 4 + 4 + 1) };
		opPairs[0x38] = new OpPair[] { mapData };
		opPairs[0x3C] = new OpPair[] { jump(8 + 8 + 8 + 4), intSizedTriple, jump(4 + 4 + 4) };
		opPairs[0x3D] = new OpPair[] { jump(4 + 4 + 1 + 4 + 4 + 1) };
		opPairs[0x3E] = new OpPair[] { shortSizedDoubled, jump(4 + 4 + 4 + 4 + 1) };
		opPairs[0x3F] = new OpPair[] { shortSizedDoubled, jump(8 + 8 + 8 + 8 + 8 + 8 + 8 + 4)};
		opPairs[0x46] = new OpPair[] { jump(1 + 1) };
		opPairs[0x47] = new OpPair[] { jump(4 + 1 + 4 + 4 + 4) };
		opPairs[0x64] = new OpPair[] { jump(1 + 1), shortSizedDoubled, jump(1 + 1) };
		opPairs[0x65] = new OpPair[] { jump(1) };
		opPairs[0x66] = new OpPair[] { jump(1 + 2 + 1 + 2 + 1), item };
		opPairs[0x67] = new OpPair[] { jump(1 + 2), item };
		opPairs[0x68] = new OpPair[] { jump(1), itemArray };
		opPairs[0x69] = new OpPair[] { jump(1 + 2 + 2) };
		opPairs[0x6A] = new OpPair[] { jump(1 + 2 + 1) };
		opPairs[0x6B] = new OpPair[] { jump(2), item };
		opPairs[0x6C] = new OpPair[] { jump(1 + 1) };
		opPairs[0x82] = new OpPair[] { jump(4 + 2 + 4), shortSizedDoubled, shortSizedDoubled, shortSizedDoubled, shortSizedDoubled };
		opPairs[0x83] = new OpPair[] { jump(2 + 2), shortSized };
		opPairs[0x84] = new OpPair[] { jump(4 + 2 + 4 + 1), shortSized };
		opPairs[0xC8] = new OpPair[] { jump(4 + 1) };
		opPairs[0xC9] = new OpPair[] { shortSizedDoubled, jump(1 + 2) }; // has another codec
		opPairs[0xCA] = new OpPair[] { jump(1 + 1 + 1) };
		opPairs[0xCB] = new OpPair[] { shortSizedDoubled };
		opPairs[0xCC] = new OpPair[] { shortSizedDoubled, jump(4) };
		opPairs[0xCD] = new OpPair[] { jump(1) }; // has another codec
		opPairs[0xCE] = new OpPair[] { shortSizedDoubled, shortSizedDoubled, jump(1)}; // has another codec
		opPairs[0xCF] = new OpPair[] { shortSizedDoubled, jump(1), shortSizedDoubled, jump(4)};
		opPairs[0xD0] = new OpPair[] { jump(1), shortSizedDoubled};
		opPairs[0xD1] = new OpPair[] { shortSizedDoubled, teamData};
		opPairs[0xFA] = new OpPair[] { shortSizedDoubled, shortSized };
		opPairs[0xFC] = new OpPair[] { shortSized, shortSized }; // is not actually used
		opPairs[0xFD] = new OpPair[] { shortSizedDoubled, shortSized, shortSized }; // has another codec
		opPairs[0xFE] = new OpPair[] { jump(1) };
		opPairs[0xFF] = new OpPair[] { shortSizedDoubled }; // has another codec
	}
	
	public static OpPair jump(int size) {
		return new OpPair(Op.JUMP_FIXED, size);
	}
	
}
