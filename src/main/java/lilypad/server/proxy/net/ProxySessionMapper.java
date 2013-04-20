package lilypad.server.proxy.net;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProxySessionMapper {

	private Map<String, ProxySession> authenticatedByUsername = new ConcurrentHashMap<String, ProxySession>();

	public void markAuthenticated(ProxySession proxySession) {
		this.authenticatedByUsername.put(proxySession.getUsername(), proxySession);
	}

	public Set<String> getAuthenticatedUsernames() {
		return Collections.unmodifiableSet(this.authenticatedByUsername.keySet());
	}

	public boolean hasAuthenticatedByUsername(String string) {
		return this.authenticatedByUsername.containsKey(string);
	}

	public ProxySession getAuthenticatedByUsername(String string) {
		return this.authenticatedByUsername.get(string);
	}

	public int getAuthenticatedSize() {
		return this.authenticatedByUsername.size();
	}

	public void remove(ProxySession proxySession) {
		this.authenticatedByUsername.remove(proxySession.getUsername());
	}

	public void clear() {
		this.authenticatedByUsername.clear();
	}

}
