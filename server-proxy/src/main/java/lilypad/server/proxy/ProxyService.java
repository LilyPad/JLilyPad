package lilypad.server.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.Set;

import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.packet.common.VarIntFrameCodec;
import lilypad.server.proxy.net.LegacyPingDecoder;
import lilypad.server.proxy.net.ProxyInboundHandler;
import lilypad.server.proxy.net.ProxySession;
import lilypad.server.proxy.net.ProxySessionMapper;
import lilypad.server.proxy.packet.MinecraftPacketConstants;
import lilypad.server.proxy.packet.StatefulPacketCodecProviderPair;
import lilypad.server.proxy.packet.state.HandshakeStateCodecProvider;
import lilypad.server.common.service.Service;
import lilypad.server.common.IPlayable;
import lilypad.server.common.IServer;

public class ProxyService extends Service<ProxyConfig> implements IPlayable {
	
	private ProxyInboundHandler proxyInboundHandler;
	private ProxySessionMapper proxySessionMapper;
	
	private NioEventLoopGroup parentEventGroup;
	private NioEventLoopGroup childEventGroup;
	private boolean running;
	
	private ProxyConfig config;
	
	public void enable(ProxyConfig config) throws Exception {
		this.config = config;
		this.proxySessionMapper = new ProxySessionMapper();
		this.proxyInboundHandler = new ProxyInboundHandler(config, this.proxySessionMapper);
		ServerBootstrap serverBootstrap = new ServerBootstrap().group(this.parentEventGroup = new NioEventLoopGroup(), this.childEventGroup = new NioEventLoopGroup())
				.channel(NioServerSocketChannel.class)
				.localAddress(config.proxy_getBindAddress())
				.childHandler(new ChannelInitializer<SocketChannel>() {
					public void initChannel(SocketChannel channel) throws Exception {
						StatefulPacketCodecProviderPair packetCodecProvider = new StatefulPacketCodecProviderPair(HandshakeStateCodecProvider.instance);
						channel.attr(StatefulPacketCodecProviderPair.attributeKey).set(packetCodecProvider);
						channel.config().setAllocator(PooledByteBufAllocator.DEFAULT);
						channel.pipeline().addLast(new ReadTimeoutHandler(30));
						channel.pipeline().addLast(new LegacyPingDecoder(ProxyService.this.config, proxySessionMapper));
						channel.pipeline().addLast(new VarIntFrameCodec());
						channel.pipeline().addLast(new PacketEncoder(packetCodecProvider.getClientBound()));
						channel.pipeline().addLast(new PacketDecoder(packetCodecProvider.getServerBound()));
						channel.pipeline().addLast(proxyInboundHandler);
					}
		});
		serverBootstrap.bind().sync();
		this.running = true;
	}

	public void disable() {
		try {
			if(this.proxySessionMapper != null) {
				this.proxySessionMapper.disconnectAuthenticated(MinecraftPacketConstants.colorize(this.config.proxy_getLocaleShutdown()));
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
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
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
		return MinecraftPacketConstants.minecraftVersion;
	}
	
}
