package lilypad.server.standalone.allinone;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import lilypad.server.common.config.FileConfig;

public class AllInOneConfig extends Config implements FileConfig {

	public Proxy proxy = new Proxy();
	public QueryTcp queryTcp = new QueryTcp();
	public QueryUdp queryUdp = new QueryUdp();
	public Connect connect = new Connect();
	
	private transient InetSocketAddress querytcpBindAddress;
	private transient InetSocketAddress queryudpBindAddress;
	private transient InetSocketAddress connectBindAddress;
	private transient InetSocketAddress proxyBindAddress;
	private transient InetSocketAddress proxyOutboundAddress;
	private transient Map<String, String> logins;
	private transient Map<String, String> domains;
	
	public class Proxy {
		public Bind bind = new Bind();
		public Outbound outbound = new Outbound();
		public Server[] servers = { new Server() };
		
		public String motd = "A Minecraft Server";
		public int playerMaximum = 1;
		public boolean authenticate = true;
		public long throttle = 2500L;
		public boolean tabEnabled = true;
		
		public String localeFull = "You seem to be already logged in";
		public String localeOffline = "The requested server is currently offline. Try again later!";
		public String localeLoggedIn = "You have logged in from another location";
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
	
	public class QueryTcp {
		public Bind bind = new Bind();
		
		public QueryTcp() {
			this.bind.port = 5555;
		}
	}
	
	public class QueryUdp {
		public Bind bind = new Bind();
		
		public QueryUdp() {
			this.bind.port = 25565;
		}
	}
	
	public class Connect {
		public Bind bind = new Bind();
		public Login[] logins = { new Login() };

		public Connect() {
			this.bind.port = 5091;
		}
		
		public class Login {
			public String username = "example";
			public String password = "example";
		}
	}
	
	public class Bind {
		public String address = "0.0.0.0";
		public int port;
	}
	
	public InetSocketAddress querytcp_getBindAddress() {
		if(this.querytcpBindAddress == null) {
			this.querytcpBindAddress = new InetSocketAddress(this.queryTcp.bind.address, this.queryTcp.bind.port);
		}
		return this.querytcpBindAddress;
	}
	
	public InetSocketAddress queryudp_getBindAddress() {
		if(this.queryudpBindAddress == null) {
			this.queryudpBindAddress = new InetSocketAddress(this.queryUdp.bind.address, this.queryUdp.bind.port);
		}
		return this.queryudpBindAddress;
	}

	public InetSocketAddress connect_getBindAddress() {
		if(this.connectBindAddress == null) {
			this.connectBindAddress = new InetSocketAddress(this.connect.bind.address, this.connect.bind.port);
		}
		return this.connectBindAddress;
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
	
	
	public Map<String, String> connect_getLogins() {
		if(this.logins == null) {
			this.logins = new HashMap<String, String>();
			for(Connect.Login login : this.connect.logins) {
				this.logins.put(login.username, login.password);
			}
		}
		return this.logins;
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
	
}
