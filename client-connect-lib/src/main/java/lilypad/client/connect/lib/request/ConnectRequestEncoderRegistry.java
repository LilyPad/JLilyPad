package lilypad.client.connect.lib.request;

import lilypad.client.connect.lib.request.impl.AsProxyRequestEncoder;
import lilypad.client.connect.lib.request.impl.AsServerRequestEncoder;
import lilypad.client.connect.lib.request.impl.AuthenticateRequestEncoder;
import lilypad.client.connect.lib.request.impl.GetDetailsRequestEncoder;
import lilypad.client.connect.lib.request.impl.GetKeyRequestEncoder;
import lilypad.client.connect.lib.request.impl.GetPlayersRequestEncoder;
import lilypad.client.connect.lib.request.impl.GetWhoamiRequestEncoder;
import lilypad.client.connect.lib.request.impl.MessageRequestEncoder;
import lilypad.client.connect.lib.request.impl.NotifyPlayerRequestEncoder;
import lilypad.client.connect.lib.request.impl.RedirectRequestEncoder;

public class ConnectRequestEncoderRegistry extends RequestEncoderRegistry {

	public static final ConnectRequestEncoderRegistry instance = new ConnectRequestEncoderRegistry();
	
	private ConnectRequestEncoderRegistry() {
		this.submit(new AsProxyRequestEncoder());
		this.submit(new AsServerRequestEncoder());
		this.submit(new AuthenticateRequestEncoder());
		this.submit(new GetDetailsRequestEncoder());
		this.submit(new GetKeyRequestEncoder());
		this.submit(new GetPlayersRequestEncoder());
		this.submit(new GetWhoamiRequestEncoder());
		this.submit(new MessageRequestEncoder());
		this.submit(new NotifyPlayerRequestEncoder());
		this.submit(new RedirectRequestEncoder());
	}
	
}
