package lilypad.server.standalone.proxy;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.RedirectEvent;
import lilypad.client.connect.api.RedirectEventListener;
import lilypad.server.common.IRedirectable;
import lilypad.server.common.IServer;

public class ConnectServerRedirect implements RedirectEventListener {

	private ConnectServerSource connectServerSource;
	private IRedirectable redirectable;
	
	public ConnectServerRedirect(ConnectServerSource connectServerSource, IRedirectable redirectable) {
		this.connectServerSource = connectServerSource;
		this.redirectable = redirectable;
	}
	
	public void onRedirect(Connect connect, RedirectEvent redirectEvent) {
		IServer server = this.connectServerSource.getServerByName(redirectEvent.getServer());
		if(server == null) {
			return;
		}
		this.redirectable.redirect(redirectEvent.getPlayer(), server);
	}

}
