package lilypad.server.query.tcp;

import java.net.InetSocketAddress;

import lilypad.server.common.config.IConfig;
import lilypad.server.common.IPlayable;

public interface QueryTcpConfig extends IConfig {

	public InetSocketAddress querytcp_getBindAddress();
	
	public IPlayable querytcp_getPlayable();
	
}
