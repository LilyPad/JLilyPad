package lilypad.server.connect.node;

import java.util.Random;

public class NodeSessionKeepalive implements Runnable {

	private NodeSessionMapper nodeSessionMapper;
	private Thread thread;

	public NodeSessionKeepalive(NodeSessionMapper nodeSessionMapper) {
		this.nodeSessionMapper = nodeSessionMapper;
	}

	public void start() {
		if(this.thread != null) {
			return;
		}
		this.thread = new Thread(this);
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
		try {
			Random random = new Random();
			while(this.thread != null) {
				int randomInt;
				for(NodeSession nodeSession : this.nodeSessionMapper.getAuthenticated()) {
					do {
						randomInt = random.nextInt();
					} while(randomInt == 0);
					nodeSession.ping(randomInt);
				}
				Thread.sleep(5000L);
			}
		} catch(InterruptedException exception) {
			// ignore
		} catch(Exception exception) {
			exception.printStackTrace();
		}
	}

}
