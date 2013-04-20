package lilypad.server.standalone.proxy;

import java.util.HashSet;
import java.util.Set;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.GetPlayersRequest;
import lilypad.client.connect.api.request.impl.NotifyPlayerRequest;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.api.result.impl.GetPlayersResult;
import lilypad.client.connect.api.result.impl.NotifyPlayerResult;
import lilypad.server.common.IPlayerCallback;

public class ConnectPlayerCallback implements IPlayerCallback {

	private Connect connect;
	private Set<String> localPlayers = new HashSet<String>();
	private int playerCount;
	private int playerMaximum;
	
	public ConnectPlayerCallback(Connect connect) {
		this.connect = connect;
	}
	
	public void resendLocalPlayers() throws RequestException {		
		for(String player : localPlayers) {
			this.connect.request(new NotifyPlayerRequest(true, player));
		}
	}
	
	public boolean queryPlayers() throws RequestException, InterruptedException {
		GetPlayersResult result = this.connect.request(new GetPlayersRequest(false)).await(10000L);
		if(result == null) {
			return false;
		}
		this.playerCount = result.getCurrentPlayers();
		this.playerMaximum = result.getMaximumPlayers();
		return true;
	}
	
	public int getPlayerCount() {
		return this.playerCount;
	}
	
	public int getPlayerMaximum() {
		return this.playerMaximum;
	}

	public int notifyPlayerJoin(String player) {
		try {
			NotifyPlayerResult result = this.connect.request(new NotifyPlayerRequest(true, player)).await(1000L);
			if(result == null) {
				return -1;
			}
			if(result.getStatusCode() == StatusCode.SUCCESS) {
				this.localPlayers.add(player);
				return 1;
			}
			return 0;
		} catch(InterruptedException exception) {
			// ignore
		} catch(RequestException exception) {
			// ignore
		}
		return -1;
	}

	public void notifyPlayerLeave(String player) {
		try {
			this.connect.request(new NotifyPlayerRequest(false, player));
		} catch(RequestException exception) {
			// ignore
		}
		this.localPlayers.remove(player);
	}

}
