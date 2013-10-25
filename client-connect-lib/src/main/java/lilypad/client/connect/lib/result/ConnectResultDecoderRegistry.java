package lilypad.client.connect.lib.result;

import lilypad.client.connect.lib.result.impl.AsProxyResultDecoder;
import lilypad.client.connect.lib.result.impl.AsServerResultDecoder;
import lilypad.client.connect.lib.result.impl.AuthenticateResultDecoder;
import lilypad.client.connect.lib.result.impl.GetDetailsResultDecoder;
import lilypad.client.connect.lib.result.impl.GetKeyResultDecoder;
import lilypad.client.connect.lib.result.impl.GetPlayersResultDecoder;
import lilypad.client.connect.lib.result.impl.GetWhoamiResultDecoder;
import lilypad.client.connect.lib.result.impl.MessageResultDecoder;
import lilypad.client.connect.lib.result.impl.NotifyPlayerResultDecoder;
import lilypad.client.connect.lib.result.impl.RedirectResultDecoder;

public class ConnectResultDecoderRegistry extends ResultDecoderRegistry {

	public static final ConnectResultDecoderRegistry instance = new ConnectResultDecoderRegistry();
	
	private ConnectResultDecoderRegistry() {
		this.submit(new AsProxyResultDecoder());
		this.submit(new AsServerResultDecoder());
		this.submit(new AuthenticateResultDecoder());
		this.submit(new GetDetailsResultDecoder());
		this.submit(new GetKeyResultDecoder());
		this.submit(new GetPlayersResultDecoder());
		this.submit(new GetWhoamiResultDecoder());
		this.submit(new MessageResultDecoder());
		this.submit(new NotifyPlayerResultDecoder());
		this.submit(new RedirectResultDecoder());
	}
	
}
