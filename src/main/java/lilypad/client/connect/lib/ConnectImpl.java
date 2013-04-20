package lilypad.client.connect.lib;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.ConnectSettings;
import lilypad.client.connect.api.MessageEvent;
import lilypad.client.connect.api.MessageEventListener;
import lilypad.client.connect.api.RedirectEvent;
import lilypad.client.connect.api.RedirectEventListener;
import lilypad.client.connect.api.ServerAddEvent;
import lilypad.client.connect.api.ServerEventListener;
import lilypad.client.connect.api.request.Request;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;
import lilypad.client.connect.lib.request.ConnectRequestEncoderRegistry;
import lilypad.client.connect.lib.request.RequestEncoder;
import lilypad.client.connect.lib.result.ConnectResultDecoderRegistry;
import lilypad.client.connect.lib.result.FutureResultImpl;
import lilypad.client.connect.lib.result.ResultDecoder;
import lilypad.packet.common.PacketDecoder;
import lilypad.packet.common.PacketEncoder;
import lilypad.packet.connect.ConnectPacketCodecRegistry;
import lilypad.packet.connect.impl.RequestPacket;

public class ConnectImpl implements Connect {

	private Bootstrap bootstrap;
	private Channel channel;

	private ConnectSettings settings;
	private String inboundIp;

	private Set<MessageEventListener> messageEventListeners = new HashSet<MessageEventListener>();
	private ReentrantLock messageEventListenersLock = new ReentrantLock();
	private Set<RedirectEventListener> redirectEventListeners = new HashSet<RedirectEventListener>();
	private ReentrantLock redirectEventListenersLock = new ReentrantLock();
	private Set<ServerEventListener> serverEventListeners = new HashSet<ServerEventListener>();
	private ReentrantLock serverEventListenersLock = new ReentrantLock();

	@SuppressWarnings("rawtypes")
	private Map<Integer, FutureResultImpl> pendingFutures = new HashMap<Integer, FutureResultImpl>();
	private AtomicInteger currentFutureId = new AtomicInteger();

	private boolean closed;

	public ConnectImpl(ConnectSettings connectSettings) {
		this(connectSettings, "0.0.0.0");
	}

	public ConnectImpl(ConnectSettings connectSettings, String inboundIp) {
		this.settings = connectSettings;
		this.inboundIp = inboundIp;
	}

	public void connect() throws Throwable {
		this.disconnect();
		this.bootstrap = new Bootstrap();
		this.bootstrap.group(new NioEventLoopGroup())
		.channel(NioSocketChannel.class)
		.localAddress(new InetSocketAddress(InetAddress.getByName(this.inboundIp), 0))
		.handler(new ChannelInitializer<SocketChannel>() {
			public void initChannel(SocketChannel channel) throws Exception {
				channel.pipeline().addLast(new ReadTimeoutHandler(10));
				channel.pipeline().addLast(new PacketEncoder(ConnectPacketCodecRegistry.instance));
				channel.pipeline().addLast(new PacketDecoder(ConnectPacketCodecRegistry.instance));
				channel.pipeline().addLast(new ConnectNetworkHandler(ConnectImpl.this));
			}
		});
		ChannelFuture future = this.bootstrap.connect(this.settings.getOutboundAddress()).sync();
		if(!future.isSuccess()) {
			throw future.cause();
		}
		this.channel = future.channel();
	}

	@SuppressWarnings("rawtypes")
	public void disconnect() {
		try {
			if(this.pendingFutures != null) {
				for(FutureResultImpl pendingFuture : this.pendingFutures.values()) {
					pendingFuture.cancel();
				}
				this.pendingFutures.clear();
			}
			if(this.bootstrap != null) {
				this.bootstrap.shutdown();
			}
			if(this.channel != null && this.channel.isOpen()) {
				this.channel.close().sync();
			}
		} catch(Exception exception) {
			// ignore
		} finally {
			this.bootstrap = null;
			this.channel = null;
		}
	}

	public void close() {
		this.disconnect();
		this.closed = true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T extends Result> FutureResult<T> request(Request<T> request) throws RequestException {
		if(this.isClosed()) {
			throw new RequestException("Not open");
		}
		if(!this.isConnected()) {
			throw new RequestException("Not connected");
		}
		int futureId = this.currentFutureId.incrementAndGet();
		FutureResultImpl<T> futureResult = new FutureResultImpl<T>(request.getResult());
		RequestEncoder requestEncoder = ConnectRequestEncoderRegistry.instance.getByRequest(request.getClass());
		if(requestEncoder == null) {
			throw new RequestException("Unknown request");
		}
		ByteBuf payload = Unpooled.buffer();
		requestEncoder.encode(request, payload);
		this.channel.write(new RequestPacket(futureId, requestEncoder.getId(), payload));
		this.pendingFutures.put(futureId, futureResult);
		return futureResult;
	}

	public void registerMessageEventListener(MessageEventListener messageEventListener) {
		this.messageEventListenersLock.lock();
		try {
			this.messageEventListeners.add(messageEventListener);
		} finally {
			this.messageEventListenersLock.unlock();
		}
	}

	public void unregisterMessageEventListener(MessageEventListener messageEventListener) {
		this.messageEventListenersLock.lock();
		try {
			this.messageEventListeners.remove(messageEventListener);
		} finally {
			this.messageEventListenersLock.unlock();
		}
	}

	public void dispatchMessageEvent(MessageEvent messageEvent) {
		this.messageEventListenersLock.lock();
		try {
			Iterator<MessageEventListener> messageEventListenersIt = this.messageEventListeners.iterator();
			while(messageEventListenersIt.hasNext()) {
				messageEventListenersIt.next().onMessage(this, messageEvent);
			}
		} finally {
			this.messageEventListenersLock.unlock();
		}
	}

	public void registerRedirectEventListener(RedirectEventListener redirectEventListener) {
		this.redirectEventListenersLock.lock();
		try {
			this.redirectEventListeners.add(redirectEventListener);
		} finally {
			this.redirectEventListenersLock.unlock();
		}
	}

	public void unregisterRedirectEventListener(RedirectEventListener redirectEventListener) {
		this.redirectEventListenersLock.lock();
		try {
			this.redirectEventListeners.remove(redirectEventListener);
		} finally {
			this.redirectEventListenersLock.unlock();
		}
	}
	
	public void dispatchRedirectEvent(RedirectEvent redirectEvent) {
		this.redirectEventListenersLock.lock();
		try {
			Iterator<RedirectEventListener> serverEventListenersIt = this.redirectEventListeners.iterator();
			while(serverEventListenersIt.hasNext()) {
				serverEventListenersIt.next().onRedirect(ConnectImpl.this, redirectEvent);
			}
		} finally {
			this.redirectEventListenersLock.unlock();
		}
	}
	
	public void registerServerEventListener(ServerEventListener serverEventListener) {
		this.serverEventListenersLock.lock();
		try {
			this.serverEventListeners.add(serverEventListener);
		} finally {
			this.serverEventListenersLock.unlock();
		}
	}

	public void unregisterServerEventListener(ServerEventListener serverEventListener) {
		this.serverEventListenersLock.lock();
		try {
			this.serverEventListeners.remove(serverEventListener);
		} finally {
			this.serverEventListenersLock.unlock();
		}
	}
	
	public void dispatchServerAddEvent(ServerAddEvent serverAddEvent) {
		this.serverEventListenersLock.lock();
		try {
			Iterator<ServerEventListener> serverEventListenersIt = this.serverEventListeners.iterator();
			while(serverEventListenersIt.hasNext()) {
				serverEventListenersIt.next().onServerAdd(this, serverAddEvent);
			}
		} finally {
			this.serverEventListenersLock.unlock();
		}
	}
	
	public void dispatchServerRemoveEvent(String server) {
		this.serverEventListenersLock.lock();
		try {
			Iterator<ServerEventListener> serverEventListenersIt = this.serverEventListeners.iterator();
			while(serverEventListenersIt.hasNext()) {
				serverEventListenersIt.next().onServerRemove(this, server);
			}
		} finally {
			this.serverEventListenersLock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public <T extends Result> void dispatchResult(int id, StatusCode statusCode, ByteBuf buffer) {
		FutureResultImpl<T> futureResult = this.pendingFutures.remove(id);
		if(futureResult == null) {
			return;
		}
		ResultDecoder<?> resultDecoder = ConnectResultDecoderRegistry.instance.getByResult(futureResult.getResultClass());
		if(resultDecoder == null) {
			return; // encoder without decoder? fail safe anyway
		}
		futureResult.notifyResult((T) resultDecoder.decode(statusCode, buffer));
	}

	public boolean isConnected() {
		return this.bootstrap != null && this.channel != null && this.channel.isOpen();
	}

	public boolean isClosed() {
		return this.closed;
	}

	public ConnectSettings getSettings() {
		return this.settings;
	}

}
