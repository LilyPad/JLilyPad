package lilypad.server.query.udp.net;

import lilypad.server.common.util.SecurityUtils;

public class QueryUdpIdentification {
	
	private long time;
	private int requestId;
	private int challenge;
	
	public QueryUdpIdentification(int requestId) {
		this.time = System.currentTimeMillis();
		this.requestId = requestId;
		this.challenge = SecurityUtils.randomInt(16777216);
	}
	
	public void updateTime() {
		this.time = System.currentTimeMillis();
	}
	
	public boolean hasExpired() {
		return System.currentTimeMillis() - this.time > 30000L;
	}
	
	public int getRequestId() {
		return this.requestId;
	}
	
	public int getChallenge() {
		return this.challenge;
	}
	
}
