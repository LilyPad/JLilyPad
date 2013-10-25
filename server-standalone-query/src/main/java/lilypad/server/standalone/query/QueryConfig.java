package lilypad.server.standalone.query;

import java.net.InetSocketAddress;

import lilypad.server.common.config.FileConfig;

public class QueryConfig extends Config implements FileConfig {

	public Connect connect = new Connect();
	public QueryTcp queryTcp = new QueryTcp();
	public QueryUdp queryUdp = new QueryUdp();
	
	private transient InetSocketAddress connectAddress;
	private transient InetSocketAddress querytcpBindAddress;
	private transient InetSocketAddress queryudpBindAddress;
	
	public class Connect {
		public String address = "127.0.0.1";
		public int port = 5091;
		public Credentials credentials = new Credentials();
		
		public class Credentials {
			public String username = "example";
			public String password = "example";
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
