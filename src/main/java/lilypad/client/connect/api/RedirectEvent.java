package lilypad.client.connect.api;

public class RedirectEvent {

	private String server;
	private String player;
	
	public RedirectEvent(String server, String player) {
		this.server = server;
		this.player = player;
	}
	
	public String getServer() {
		return this.server;
	}
	
	public String getPlayer() {
		return this.player;
	}
	
}
