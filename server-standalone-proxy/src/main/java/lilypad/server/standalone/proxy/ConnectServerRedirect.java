package lilypad.server.standalone.proxy;

import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.RedirectEvent;
import lilypad.server.common.IRedirectable;
import lilypad.server.common.IServer;

public class ConnectServerRedirect {

	private ConnectServerSource connectServerSource;
	private IRedirectable redirectable;
	
	public ConnectServerRedirect(ConnectServerSource connectServerSource, IRedirectable redirectable) {
		this.connectServerSource = connectServerSource;
		this.redirectable = redirectable;
	}
	
	@EventListener
	public void onRedirect(RedirectEvent redirectEvent) {
		IServer server = this.connectServerSource.getServerByName(redirectEvent.getServer());
		if(server == null) {
			return;
		}
		this.redirectable.redirect(redirectEvent.getPlayer(), server);
	}

}
