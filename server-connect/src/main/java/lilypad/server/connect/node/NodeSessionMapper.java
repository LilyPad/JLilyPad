package lilypad.server.connect.node;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NodeSessionMapper {

	private Map<String, NodeSession> authenticatedByUsername = new ConcurrentHashMap<String, NodeSession>();
	private Map<String, NodeSession> serversByUsername = new ConcurrentHashMap<String, NodeSession>();
	private Map<String, NodeSession> proxiesByUsername = new ConcurrentHashMap<String, NodeSession>();

	public void markAuthenticated(NodeSession nodeSession) {
		this.authenticatedByUsername.put(nodeSession.getIdentification(), nodeSession);
	}

	public NodeSession getAuthenticatedByUsername(String username) {
		return this.authenticatedByUsername.get(username);
	}

	public Collection<NodeSession> getAuthenticated() {
		return Collections.unmodifiableCollection(this.authenticatedByUsername.values());
	}

	public void markServer(NodeSession nodeSession) {
		this.serversByUsername.put(nodeSession.getIdentification(), nodeSession);
	}

	public NodeSession getServerByUsername(String username) {
		return this.serversByUsername.get(username);
	}

	public Collection<NodeSession> getServers() {
		return Collections.unmodifiableCollection(this.serversByUsername.values());
	}

	public void markProxy(NodeSession nodeSession) {
		this.proxiesByUsername.put(nodeSession.getIdentification(), nodeSession);
	}

	public NodeSession getProxyByUsername(String username) {
		return this.proxiesByUsername.get(username);
	}

	public Collection<NodeSession> getProxies() {
		return Collections.unmodifiableCollection(this.proxiesByUsername.values());
	}

	public void remove(NodeSession nodeSession) {
		this.authenticatedByUsername.remove(nodeSession.getIdentification());
		this.serversByUsername.remove(nodeSession.getIdentification());
		this.proxiesByUsername.remove(nodeSession.getIdentification());
	}

	public void clear() {
		this.authenticatedByUsername.clear();
		this.serversByUsername.clear();
		this.proxiesByUsername.clear();
	}

}
