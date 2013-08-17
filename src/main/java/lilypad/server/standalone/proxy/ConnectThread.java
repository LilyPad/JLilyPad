package lilypad.server.standalone.proxy;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.ConnectSettings;
import lilypad.client.connect.api.request.impl.AsProxyRequest;
import lilypad.client.connect.api.request.impl.AuthenticateRequest;
import lilypad.client.connect.api.request.impl.GetKeyRequest;
import lilypad.client.connect.api.result.impl.AsProxyResult;
import lilypad.client.connect.api.result.impl.AuthenticateResult;
import lilypad.client.connect.api.result.impl.GetKeyResult;
import lilypad.server.proxy.packet.CraftPacketConstants;

public class ConnectThread implements Runnable {

	private static final long playersInterval = 5000L;
	
	private Connect connect;
	private ConnectServerSource connectServerSource;
	private ConnectPlayerCallback connectPlayerCallback;
	private ProxyConfig proxyConfig;
	private Thread thread;
	
	public ConnectThread(Connect connect, ConnectServerSource connectServerSource, ConnectPlayerCallback connectPlayerCallback, ProxyConfig proxyConfig) {
		this.connect = connect;
		this.connectServerSource = connectServerSource;
		this.connectPlayerCallback = connectPlayerCallback;
		this.proxyConfig = proxyConfig;
	}
	
	public void start() {
		if(this.thread != null) {
			return;
		}
		this.thread = new Thread(this);
		this.thread.setName("query-cache-updater");
		this.thread.start();
	}

	public void stop() {
		if(this.thread == null) {
			return;
		}
		this.thread.interrupt();
		this.thread = null;
	}
	
	public void run() {
		ConnectSettings settings = connect.getSettings();
		try {
			while(!this.connect.isClosed()) {
				// preconnect
				this.connectServerSource.clearServers();
				this.connectPlayerCallback.clearPlayers();
				
				// connect
				try {
					this.connect.connect();
				} catch(Throwable throwable) {
					this.connect.disconnect();
					System.out.println("[Connect] Couldn't connect to remote host: \"" + throwable.getMessage() + "\", retrying");
					Thread.sleep(1000L);
					continue;
				}
				
				// key
				GetKeyResult getKeyResult = this.connect.request(new GetKeyRequest()).await(2000L);
				if(getKeyResult == null) {
					this.connect.disconnect();
					System.out.println("[Connect] Connection timed out while keying, retrying");
					Thread.sleep(1000L);
					continue;
				}
				
				// authenticate
				AuthenticateResult authenticationResult = this.connect.request(new AuthenticateRequest(settings.getUsername(), settings.getPassword(), getKeyResult.getKey())).await(2000L);
				if(authenticationResult == null) {
					this.connect.disconnect();
					System.out.println("[Connect] Connection timed out while authenticating, retrying");
					Thread.sleep(1000L);
					continue;
				}
				switch(authenticationResult.getStatusCode()) {
				case SUCCESS:
					break;
				case INVALID_GENERIC:
					this.connect.disconnect();
					System.out.println("[Connect] Invalid username or password, retrying");
					Thread.sleep(1000L);
					continue;
				default:
					this.connect.disconnect();
					System.out.println("[Connect] Unknown error while authenticating: \"" + authenticationResult.getStatusCode() + "\", retrying");
					Thread.sleep(1000L);
					continue;
				}
				
				// asproxy
				AsProxyResult asProxyResult = this.connect.request(new AsProxyRequest(this.proxyConfig.proxy_getBindAddress().getPort(), this.proxyConfig.proxy_getPlayerMotd(), CraftPacketConstants.minecraftVersion, this.proxyConfig.proxy_getPlayerMaximum())).await(2000L);
				if(asProxyResult == null) {
					this.connect.disconnect();
					System.out.println("[Connect] Connection timed out while acquiring role, retrying");
					Thread.sleep(1000L);
					continue;
				}
				switch(asProxyResult.getStatusCode()) {
				case SUCCESS:
					break;
				default:
					connect.disconnect();
					System.out.println("[Connect] Unknown error while acquiring role: \"" + asProxyResult.getStatusCode() + "\", retrying");
					Thread.sleep(1000L);
					continue;
				}
				
				
				// postconnect
				this.connectPlayerCallback.resendLocalPlayers();
				
				// pause
				System.out.println("[Connect] Connected to the cloud");
				long lastPlayers = 0L;
				while(this.connect.isConnected()) {
					if(System.currentTimeMillis() - lastPlayers > playersInterval) {
						this.connectPlayerCallback.queryPlayers();
						lastPlayers = System.currentTimeMillis();
					}
					Thread.sleep(1000L);
				}
				System.out.println("[Connect] Lost connection to the cloud, reconnecting");
			}
		} catch(InterruptedException exception) {
			//ignore
		} catch(Exception exception) {
			System.out.println("-=== FATAL ===- Please report this error to http://lilypadmc.com:");
			exception.printStackTrace();
		}
	}
	
}
