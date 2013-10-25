package lilypad.packet.connect.impl;

public class ServerAddPacket extends ServerPacket {

	private String securityKey;
	private String address;
	private int port;
	
	public ServerAddPacket(String server, String securityKey, String address, int port) {
		super(server);
		this.securityKey = securityKey;
		this.address = address;
		this.port = port;
	}
	
	@Override
	public boolean isAdding() {
		return true;
	}

	public String getSecurityKey() {
		return this.securityKey;
	}
	
	public String getAddress() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
	
}
