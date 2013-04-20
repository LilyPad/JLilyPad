package lilypad.server.query.udp;

import java.net.InetSocketAddress;

import lilypad.server.common.IPlayable;
import lilypad.server.common.config.IConfig;

public interface QueryUdpConfig extends IConfig {

	public InetSocketAddress queryudp_getBindAddress();
	
	public IPlayable queryudp_getPlayable();
	
}
