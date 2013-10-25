package lilypad.server.common;

import java.net.InetSocketAddress;
import java.util.Set;

public interface IPlayable extends IRedirectable {

	public String getMotd();
	
	public InetSocketAddress getBindAddress();
	
	public Set<String> getPlayers();
	
	public int getPlayerMaximum();
	
	public String getVersion();
	
}
