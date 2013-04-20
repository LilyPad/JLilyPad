package lilypad.server.standalone.query;

import lilypad.client.connect.api.ConnectSettings;
import lilypad.server.common.IPlayable;
import lilypad.server.query.tcp.QueryTcpConfig;
import lilypad.server.query.udp.QueryUdpConfig;

public abstract class Config implements QueryTcpConfig, QueryUdpConfig, ConnectSettings {

	private transient IPlayable playable;
	
	public void init(IPlayable playable) {
		this.playable = playable;
	}
	
	public IPlayable querytcp_getPlayable() {
		return this.playable;
	}
	
	public IPlayable queryudp_getPlayable() {
		return this.playable;
	}

}
