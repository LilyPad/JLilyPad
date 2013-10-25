package lilypad.server.proxy.packet;

public class MinecraftPacketConstants {

	public static final String minecraftVersion = "1.7.2";
	public static final int protocolVersion = 4;
	public static final char magic = 167;
	public static final int[][] entityIdPositions = new int[256][];
	static {
		entityIdPositions[0x0A] = new int[] {0};
		entityIdPositions[0x0D] = new int[] {4};
		entityIdPositions[0x12] = new int[] {0};
		entityIdPositions[0x1B] = new int[] {0, 4};
		entityIdPositions[0x1C] = new int[] {0};
		entityIdPositions[0x1D] = new int[] {0};
		entityIdPositions[0x1E] = new int[] {0};
		entityIdPositions[0x20] = new int[] {0};
	}
	
	public static String colorize(String string) {
		return string.replace("&n", "\n").replace('&', MinecraftPacketConstants.magic).replace(Character.toString(MinecraftPacketConstants.magic) + Character.toString(MinecraftPacketConstants.magic), "&");
	}
	
}
