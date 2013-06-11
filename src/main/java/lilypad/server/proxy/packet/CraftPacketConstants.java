package lilypad.server.proxy.packet;

public class CraftPacketConstants {

	public static final String minecraftVersion = "1.5.2";
	public static final int protocolVersion = 61;
	public static final char magic = 167;
	public static final int maxPacketSize = 1024 * 128;
	public static final int[][] entityIdPositions = new int[256][];
	static {
		entityIdPositions[0x01] = new int[] {0};
		entityIdPositions[0x05] = new int[] {0};
		entityIdPositions[0x07] = new int[] {0, 4};
		entityIdPositions[0x11] = new int[] {0};
		entityIdPositions[0x12] = new int[] {0};
		entityIdPositions[0x13] = new int[] {0};
		entityIdPositions[0x14] = new int[] {0};
		entityIdPositions[0x15] = new int[] {0};
		entityIdPositions[0x16] = new int[] {0, 4};
		entityIdPositions[0x17] = new int[] {0, 19};
		entityIdPositions[0x18] = new int[] {0};
		entityIdPositions[0x19] = new int[] {0};
		entityIdPositions[0x1A] = new int[] {0};
		entityIdPositions[0x1C] = new int[] {0};
		entityIdPositions[0x1E] = new int[] {0};
		entityIdPositions[0x1F] = new int[] {0};
		entityIdPositions[0x20] = new int[] {0};
		entityIdPositions[0x21] = new int[] {0};
		entityIdPositions[0x22] = new int[] {0};
		entityIdPositions[0x23] = new int[] {0};
		entityIdPositions[0x26] = new int[] {0};
		entityIdPositions[0x27] = new int[] {0};
		entityIdPositions[0x28] = new int[] {0};
		entityIdPositions[0x29] = new int[] {0};
		entityIdPositions[0x2A] = new int[] {0};
		entityIdPositions[0x37] = new int[] {0};
		entityIdPositions[0x47] = new int[] {0};
	}
	
	public static String colorize(String string) {
		return string.replace("&n", "\n").replace('&', CraftPacketConstants.magic).replace(Character.toString(CraftPacketConstants.magic) + Character.toString(CraftPacketConstants.magic), "&");
	}
	
}
