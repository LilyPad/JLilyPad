package lilypad.server.standalone.connect;

import java.net.InetSocketAddress;
import java.util.Set;

import lilypad.packet.connect.impl.RedirectPacket;
import lilypad.server.common.IPlayable;
import lilypad.server.common.IServer;
import lilypad.server.connect.ConnectService;
import lilypad.server.connect.node.NodeSession;

public class ConnectPlayable implements IPlayable {

	private ConnectService connectService;
	
	public ConnectPlayable(ConnectService connectService) {
		this.connectService = connectService;
	}
	
	public String getMotd() {
		return this.connectService.getProxyCache().getMotd();
	}

	public InetSocketAddress getBindAddress() {
		return this.connectService.getProxyCache().getAddress();
	}

	public Set<String> getPlayers() {
		return this.connectService.getProxyCache().getPlayers();
	}

	public int getPlayerMaximum() {
		return this.connectService.getProxyCache().getMaximumPlayers();
	}

	public String getVersion() {
		return this.connectService.getProxyCache().getVersion();
	}

	public boolean redirect(String name, IServer server) {
		NodeSession nodeSession = this.connectService.getProxyCache().getProxyByPlayer(name);
		if(nodeSession == null) {
			return false;
		}
		nodeSession.write(new RedirectPacket(server.getIdentification(), name));
		return true;
	}

}
