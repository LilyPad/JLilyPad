package lilypad.server.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.server.proxy.net.ProxyInboundHandler;
import lilypad.server.proxy.net.ProxySession;
import lilypad.server.proxy.net.ProxySessionMapper;
import lilypad.server.proxy.packet.CraftPacketCodecRegistry;
import lilypad.server.proxy.packet.CraftPacketConstants;
import lilypad.server.common.service.Service;
import lilypad.server.common.IPlayable;
import lilypad.server.common.IServer;

public class ProxyService extends Service<ProxyConfig> implements IPlayable {
	
	private ExecutorService authExecutorService;
	private ProxyInboundHandler proxyInboundHandler;
	private ProxySessionMapper proxySessionMapper;
	
	private NioEventLoopGroup parentEventGroup;
	private NioEventLoopGroup childEventGroup;
	private boolean running;
	
	private ProxyConfig config;
	
	public void enable(ProxyConfig config) throws Exception {
		this.config = config;
		this.authExecutorService = Executors.newCachedThreadPool();
		this.proxySessionMapper = new ProxySessionMapper();
		this.proxyInboundHandler = new ProxyInboundHandler(config, this.proxySessionMapper, this.authExecutorService);
		ServerBootstrap serverBootstrap = new ServerBootstrap().group(this.parentEventGroup = new NioEventLoopGroup(), this.childEventGroup = new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.localAddress(config.proxy_getBindAddress())
				.childHandler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel channel) throws Exception {
						channel.pipeline().addLast(new ReadTimeoutHandler(30));
						channel.pipeline().addLast(new PacketEncoder(CraftPacketCodecRegistry.instance));
						channel.pipeline().addLast(new PacketDecoder(CraftPacketCodecRegistry.instance));
						channel.pipeline().addLast(proxyInboundHandler);
					}
		});
		serverBootstrap.bind().sync();
		this.running = true;
	}

	public void disable() {
		try {
			if(this.proxySessionMapper != null) {
				this.proxySessionMapper.kickAuthenticated(CraftPacketConstants.colorize(this.config.proxy_getLocaleShutdown()));
			}
			if(this.parentEventGroup != null) {
				this.parentEventGroup.shutdownGracefully();
			}
			if(this.childEventGroup != null) {
				this.childEventGroup.shutdownGracefully();
			}
			if(this.proxySessionMapper != null) {
				this.proxySessionMapper.clear();
			}
			if(this.authExecutorService != null) {
				this.authExecutorService.shutdownNow();
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
			this.authExecutorService = null;
			this.proxyInboundHandler = null;
			this.proxySessionMapper = null;
			this.parentEventGroup = null;
			this.childEventGroup = null;
			this.running = false;
			this.config = null;
		}
	}
	
	public boolean redirect(String name, IServer server) {
		ProxySession proxySession = this.proxySessionMapper.getAuthenticatedByUsername(name);
		if(proxySession == null) {
			return false;
		}
		proxySession.redirect(server);
		return true;
	}

	public boolean isRunning() {
		return this.running;
	}

	public String getMotd() {
		return this.config.proxy_getPlayerMotd();
	}
	
	public InetSocketAddress getBindAddress() {
		return this.config.proxy_getBindAddress();
	}

	public Set<String> getPlayers() {
		return this.proxySessionMapper.getAuthenticatedUsernames();
	}

	public int getPlayerMaximum() {
		return this.config.proxy_getPlayerMaximum();
	}

	public String getVersion() {
		return CraftPacketConstants.minecraftVersion;
	}
	
}
