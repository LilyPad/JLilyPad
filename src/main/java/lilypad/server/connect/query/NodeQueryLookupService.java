package lilypad.server.connect.query;

import lilypad.server.connect.node.NodeSession;
import lilypad.server.connect.query.impl.AsProxyQuery;
import lilypad.server.connect.query.impl.AsServerQuery;
import lilypad.server.connect.query.impl.AuthenticateQuery;
import lilypad.server.connect.query.impl.GetDetailsQuery;
import lilypad.server.connect.query.impl.GetKeyQuery;
import lilypad.server.connect.query.impl.GetPlayersQuery;
import lilypad.server.connect.query.impl.GetWhoamiQuery;
import lilypad.server.connect.query.impl.MessageQuery;
import lilypad.server.connect.query.impl.NotifyPlayer;
import lilypad.server.connect.query.impl.RedirectQuery;

public class NodeQueryLookupService extends QueryLookupService<NodeSession> {

	public static final NodeQueryLookupService instance = new NodeQueryLookupService();
	
	private NodeQueryLookupService() {
		this.submit(new AsProxyQuery());
		this.submit(new AsServerQuery());
		this.submit(new AuthenticateQuery());
		this.submit(new GetDetailsQuery());
		this.submit(new GetKeyQuery());
		this.submit(new GetPlayersQuery());
		this.submit(new GetWhoamiQuery());
		this.submit(new MessageQuery());
		this.submit(new NotifyPlayer());
		this.submit(new RedirectQuery());
	}
	
}
