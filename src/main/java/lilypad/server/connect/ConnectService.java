package lilypad.server.connect;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.packet.connect.ConnectPacketCodecRegistry;
import lilypad.server.connect.node.NodeHandler;
import lilypad.server.connect.node.NodeSessionKeepalive;
import lilypad.server.connect.node.NodeSessionMapper;
import lilypad.server.connect.node.ProxyCache;
import lilypad.server.common.service.Service;
import lilypad.server.common.IServer;
import lilypad.server.common.IServerSource;

public class ConnectService extends Service<ConnectConfig> implements IServerSource {
	
	private NodeHandler nodeHandler;
	private NodeSessionMapper nodeSessionMapper;
	private NodeSessionKeepalive nodeSessionPinger;
	private ProxyCache proxyCache;
	
	private NioEventLoopGroup parentEventGroup;
	private NioEventLoopGroup childEventGroup;
	private boolean running;

	public void enable(ConnectConfig config) throws Exception {
		this.proxyCache = new ProxyCache();
		this.nodeSessionMapper = new NodeSessionMapper();
		this.nodeSessionPinger = new NodeSessionKeepalive(this.nodeSessionMapper);
		this.nodeSessionPinger.start();
		this.nodeHandler = new NodeHandler(this, config.connect_getAuthenticator(), config.connect_getPlayable());
		ServerBootstrap serverBootstrap = new ServerBootstrap().group(this.parentEventGroup = new NioEventLoopGroup(), this.childEventGroup = new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.localAddress(config.connect_getBindAddress())
				.childHandler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new ReadTimeoutHandler(10));
						channel.pipeline().addLast(new PacketEncoder(ConnectPacketCodecRegistry.instance));
						channel.pipeline().addLast(new PacketDecoder(ConnectPacketCodecRegistry.instance));
						channel.pipeline().addLast(nodeHandler);
					}
		});
		serverBootstrap.bind().sync();
		this.running = true;
	}

	public void disable() {
		try {
			if (this.parentEventGroup != null) {
				this.parentEventGroup.shutdownGracefully();
			}
			if (this.childEventGroup != null) {
				this.childEventGroup.shutdownGracefully();
			}
			if(this.nodeSessionMapper != null) {
				this.nodeSessionMapper.clear();
			}
			if(this.nodeSessionPinger != null) {
				this.nodeSessionPinger.stop();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			this.nodeHandler = null;
			this.nodeSessionMapper = null;
			this.nodeSessionPinger = null;
			this.parentEventGroup = null;
			this.childEventGroup = null;
			this.running = false;
		}
	}

	public boolean isRunning() {
		return this.running;
	}

	public IServer getServerByName(String name) {
		return this.nodeSessionMapper.getServerByUsername(name);
	}
	
	public NodeSessionMapper getSessionMapper() {
		return this.nodeSessionMapper;
	}
	
	public ProxyCache getProxyCache() {
		return this.proxyCache;
	}

}
