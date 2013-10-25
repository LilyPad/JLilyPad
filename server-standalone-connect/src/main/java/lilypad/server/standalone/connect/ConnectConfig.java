package lilypad.server.standalone.connect;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import lilypad.server.common.config.FileConfig;

public class ConnectConfig extends Config implements FileConfig {

	private transient InetSocketAddress connectBindAddress;
	private transient Map<String, String> loginsMap;
		
	public Bind bind = new Bind();
	public Login[] logins = { new Login() };

	public ConnectConfig() {
		this.bind.port = 5091;
	}
		
	public class Login {
		public String username = "example";
		public String password = "example";	
	}
		
	public class Bind {
		public String address = "0.0.0.0";
		public int port;
	}

	public InetSocketAddress connect_getBindAddress() {
		if(this.connectBindAddress == null) {
			this.connectBindAddress = new InetSocketAddress(this.bind.address, this.bind.port);
		}
		return this.connectBindAddress;
	}
	
	public Map<String, String> connect_getLogins() {
		if(this.loginsMap == null) {
			this.loginsMap = new HashMap<String, String>();
			for(Login login : this.logins) {
				this.loginsMap.put(login.username, login.password);
			}
		}
		return this.loginsMap;
	}
	
}
