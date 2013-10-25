package lilypad.server.standalone.proxy;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;

import lilypad.server.common.config.FileConfig;

public class ProxyConfig extends Config implements FileConfig {

	public Connect connect = new Connect();
	public Proxy proxy = new Proxy();
	
	private transient InetSocketAddress connectAddress;
	private transient InetSocketAddress proxyBindAddress;
	private transient InetSocketAddress proxyOutboundAddress;
	private transient Map<String, String> domains;
	private transient String favicon;
	
	public class Connect {
		public String address = "127.0.0.1";
		public int port = 5091;
		public Credentials credentials = new Credentials();
		
		public class Credentials {
			public String username = "example";
			public String password = "example";
		}
	}
	
	public class Proxy {
		public Bind bind = new Bind();
		public Outbound outbound = new Outbound();
		public Server[] servers = { new Server() };
		
		public String motd = "A Minecraft Server";
		public int playerMaximum = 1;
		public boolean authenticate = true;
		public long throttle = 2500L;
		public boolean tabEnabled = true;
		
		public String localeFull = "The server seems to be currently full. Try again later!";
		public String localeOffline = "The requested server is currently offline. Try again later!";
		public String localeLoggedIn = "You seem to be logged in already. Try again later!";
		public String localeLostConn = "Lost connection... Please try to reconnect";
		public String localeShutdown = "The server is being restarted. Please try to reconnect";
		
		public Proxy() {
			this.bind.port = 25565;
		}
		
		public class Server {
			public String domain = "*";
			public String server = "example";
		}
		
		public class Outbound {
			public String address = "0.0.0.0";
		}
	}
	
	public class Bind {
		public String address = "0.0.0.0";
		public int port;
	}

	public InetSocketAddress proxy_getBindAddress() {
		if(this.proxyBindAddress == null) {
			this.proxyBindAddress = new InetSocketAddress(this.proxy.bind.address, this.proxy.bind.port);
		}
		return this.proxyBindAddress;
	}
	
	public InetSocketAddress proxy_getOutboundAddress() {
		if(this.proxyOutboundAddress == null) {
			this.proxyOutboundAddress = new InetSocketAddress(this.proxy.outbound.address, 0);
		}
		return this.proxyOutboundAddress;
	}

	public String proxy_getPlayerMotd() {
		return this.proxy.motd;
	}
	
	public String proxy_getPlayerFavicon() {
		if(this.favicon == null) {
			File file = new File("server-icon.png");
			if(file.exists()) {
				try {
					this.favicon = "data:image/png;base64," + BaseEncoding.base64().encode(Files.toByteArray(file));
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			if(this.favicon == null) {
				this.favicon = "";
			}
		}
		return this.favicon;
	}
	
	public int proxy_getPlayerMaximum() {
		return this.proxy.playerMaximum;
	}

	public boolean proxy_isPlayerTab() {
		return this.proxy.tabEnabled;
	}

	public boolean proxy_isPlayerAuthenticate() {
		return this.proxy.authenticate;
	}
	
	public Map<String, String> proxy_getDomains() {
		if(this.domains == null) {
			this.domains = new HashMap<String, String>();
			for(Proxy.Server server : this.proxy.servers) {
				this.domains.put(server.domain, server.server);
			}
		}
		return this.domains;
	}

	public long proxy_getPlayerThrottle() {
		return this.proxy.throttle;
	}

	public String proxy_getLocaleFull() {
		return this.proxy.localeFull;
	}

	public String proxy_getLocaleOffline() {
		return this.proxy.localeOffline;
	}

	public String proxy_getLocaleLoggedIn() {
		return this.proxy.localeLoggedIn;
	}

	public String proxy_getLocaleLostConn() {
		return this.proxy.localeLostConn;
	}
	
	public String proxy_getLocaleShutdown() {
		return this.proxy.localeShutdown;
	}

	public InetSocketAddress getOutboundAddress() {
		if(this.connectAddress == null) {
			this.connectAddress = new InetSocketAddress(this.connect.address, this.connect.port);
		}
		return this.connectAddress;
	}

	public String getUsername() {
		return this.connect.credentials.username;
	}

	public String getPassword() {
		return this.connect.credentials.password;
	}

}
