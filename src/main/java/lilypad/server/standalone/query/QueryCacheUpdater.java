package lilypad.server.standalone.query;

import java.net.InetSocketAddress;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.ConnectSettings;
import lilypad.client.connect.api.request.impl.AuthenticateRequest;
import lilypad.client.connect.api.request.impl.GetDetailsRequest;
import lilypad.client.connect.api.request.impl.GetKeyRequest;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.AuthenticateResult;
import lilypad.client.connect.api.result.impl.GetDetailsResult;
import lilypad.client.connect.api.result.impl.GetKeyResult;
import lilypad.client.connect.api.result.impl.GetPlayersResult;

public class QueryCacheUpdater implements Runnable {

	public static final long playersInterval = 5000L;
	public static final long detailsInterval = 60000L;
	
	private Connect connect;
	private QueryCache queryCache;
	private Thread thread;
	
	public QueryCacheUpdater(Connect connect, QueryCache queryCache) {
		this.connect = connect;
		this.queryCache = queryCache;
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
			CLOSED_WHILE: while(!this.connect.isClosed()) {
				// preconnect
				this.queryCache.invalidate();
				
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
				
				// pause
				System.out.println("[Connect] Connected to the cloud");
				long lastDetails = 0L;
				long lastPlayers = 0L;
				while(this.connect.isConnected()) {
					if(System.currentTimeMillis() - lastDetails > detailsInterval) {
						GetDetailsResult getDetailsResult = this.connect.request(new GetDetailsRequest()).await(10000L);
						if(getDetailsResult == null) {
							this.connect.disconnect();
							System.out.println("[Connect] Connection timed out while getting details, retrying");
							Thread.sleep(1000L);
							continue CLOSED_WHILE;
						}
						if(getDetailsResult.getStatusCode() != StatusCode.SUCCESS) {
							this.connect.disconnect();
							System.out.println("[Connect] Invalid status code while getting details, retrying");
							Thread.sleep(1000L);
							continue CLOSED_WHILE;
						}
						this.queryCache.setMotd(getDetailsResult.getMotd());
						this.queryCache.setServerAddress(new InetSocketAddress(getDetailsResult.getIp(), getDetailsResult.getPort()));
						this.queryCache.setVersion(getDetailsResult.getVersion());
						lastDetails = System.currentTimeMillis();
					}
					if(System.currentTimeMillis() - lastPlayers > playersInterval) {
						GetPlayersResult getPlayersResult = this.connect.request(new GetPlayersRequest(true)).await(10000L);
						if(getPlayersResult == null) {
							this.connect.disconnect();
							System.out.println("[Connect] Connection timed out while getting players, retrying");
							Thread.sleep(1000L);
							continue CLOSED_WHILE;
						}
						if(getPlayersResult.getStatusCode() != StatusCode.SUCCESS) {
							this.connect.disconnect();
							System.out.println("[Connect] Invalid status code while getting players, retrying");
							Thread.sleep(1000L);
							continue CLOSED_WHILE;
						}
						this.queryCache.replacePlayers(getPlayersResult.getPlayers());
						this.queryCache.setPlayerMaximum(getPlayersResult.getMaximumPlayers());
						lastPlayers = System.currentTimeMillis();
					}
					Thread.sleep(1000L);
				}
				System.out.println("[Connect] Lost connection to the cloud, reconnecting");
			}
		} catch(InterruptedException exception) {
			//ignore
		} catch(Exception exception) {
			System.out.println("-=== FATAL ===- Please report this error to http://lilypadmc.org:");
			exception.printStackTrace();
		}
	}
	
}
