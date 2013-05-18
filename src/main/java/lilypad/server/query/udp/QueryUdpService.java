package lilypad.server.query.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lilypad.server.query.udp.net.QueryUdpHandler;
import lilypad.server.common.service.Service;

public class QueryUdpService extends Service<QueryUdpConfig> {
	
	private Bootstrap bootstrap;
	private boolean running;

	public void enable(QueryUdpConfig config) throws Exception {
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(new NioEventLoopGroup())
				.channel(NioDatagramChannel.class)
				.localAddress(config.queryudp_getBindAddress())
				.handler(new QueryUdpHandler(config.queryudp_getPlayable()));
		this.bootstrap.bind().sync();
		this.running = true;
	}

	@SuppressWarnings("deprecation")
	public void disable() {
		try {
			if (this.bootstrap != null) {
				this.bootstrap.shutdown(); // TODO deprecation
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			this.bootstrap = null;
			this.running = false;
		}
	}

	public boolean isRunning() {
		return this.running;
	}

}
