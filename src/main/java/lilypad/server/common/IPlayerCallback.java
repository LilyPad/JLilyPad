package lilypad.server.common;

public interface IPlayerCallback {

	public int getPlayerCount();
	
	public int getPlayerMaximum();
	
	public int notifyPlayerJoin(String playerName);
	
	public void notifyPlayerLeave(String playerName);
	
}
