package lilypad.server.common;

import java.net.InetSocketAddress;

public interface IServer {

	public String getIdentification();
	
	public InetSocketAddress getInboundAddress();
	
	public String getSecurityKey();
	
}
