package lilypad.server.query.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lilypad.server.query.udp.net.QueryUdpHandler;
import lilypad.server.common.service.Service;

public class QueryUdpService extends Service<QueryUdpConfig> {
	
	private NioEventLoopGroup eventGroup;
	private boolean running;

	public void enable(QueryUdpConfig config) throws Exception {
		Bootstrap bootstrap = new Bootstrap().group(this.eventGroup = new NioEventLoopGroup())
				.channel(NioDatagramChannel.class)
				.localAddress(config.queryudp_getBindAddress())
				.handler(new QueryUdpHandler(config.queryudp_getPlayable()));
		bootstrap.bind().sync();
		this.running = true;
	}

	public void disable() {
		try {
			if (this.eventGroup != null) {
				this.eventGroup.shutdownGracefully();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			this.eventGroup = null;
			this.running = false;
		}
	}

	public boolean isRunning() {
		return this.running;
	}

}
