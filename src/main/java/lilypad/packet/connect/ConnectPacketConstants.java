package lilypad.packet.connect;

public class ConnectPacketConstants {

	public static final int statusInvalidRole = 0x02;
	public static final int statusInvalidGeneric = 0x01;
	public static final int statusSuccess = 0x00;
	
	public static final int requestAuthenticate = 0x00;
	public static final int requestAsServer = 0x01;
	public static final int requestAsProxy = 0x02;
	public static final int requestGetKey = 0x03;
	public static final int requestGetWhoami = 0x04;
	public static final int requestMessage = 0x10;
	public static final int requestRedirect = 0x11;
	public static final int requestGetPlayers = 0x20;
	public static final int requestNotifyPlayer = 0x21;
	public static final int requestGetDetails = 0x22;
	
}
