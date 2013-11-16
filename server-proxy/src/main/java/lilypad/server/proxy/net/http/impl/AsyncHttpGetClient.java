package lilypad.server.proxy.net.http.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLEngine;

import lilypad.server.proxy.net.http.HttpGetClient;
import lilypad.server.proxy.net.http.HttpGetClientListener;

public class AsyncHttpGetClient implements HttpGetClient {

	private static final long timeout = 10000L;

	private String host;
	private int port;
	private boolean ssl;
	private String path;
	private EventLoop eventLoop;
	private ByteBufAllocator allocator;
	private List<HttpGetClientListener> listeners = new ArrayList<HttpGetClientListener>();
	private Channel channel;

	public AsyncHttpGetClient(URI uri, EventLoop eventLoop, ByteBufAllocator allocator) {
		String scheme = uri.getScheme();
		if(scheme == null) {
			scheme = "http";
		} else if(!scheme.equals("http") && !scheme.equals("https")) {
			throw new IllegalArgumentException("Unsupported scheme: " + scheme);
		} else if(scheme.equals("https")) {
			this.ssl = true;
		}
		this.host = uri.getHost();
		this.port = uri.getPort();
		if(this.port == -1) {
			if(scheme.equals("http")) {
				this.port = 80;
			} else if(scheme.equals("https")) {
				this.port = 443;
			}
		}
		this.path = uri.getRawPath() + (uri.getRawQuery() == null ? "" : "?" + uri.getRawQuery());
		this.eventLoop = eventLoop;
		this.allocator = allocator;
	}

	public void run() {
		new Bootstrap().group(this.eventLoop)
		.channel(NioSocketChannel.class)
		.remoteAddress(this.host, this.port)
		.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, (int) timeout)
		.handler(new ChannelInitializer<SocketChannel>() {
			public void initChannel(SocketChannel channel) throws Exception {
				channel.config().setAllocator(AsyncHttpGetClient.this.allocator);
				channel.pipeline().addLast(new ReadTimeoutHandler(timeout, TimeUnit.MILLISECONDS));
				if(AsyncHttpGetClient.this.ssl) {
					SSLEngine engine = DummyTrustManager.getDummySSLContext().createSSLEngine();
					engine.setUseClientMode(true);
					channel.pipeline().addLast("ssl", new SslHandler(engine));
				}
				channel.pipeline().addLast(new HttpClientCodec());
				channel.pipeline().addLast(new AsyncHttpGetClientHandler(AsyncHttpGetClient.this));
			}
		}).connect().addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()) {
					HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, AsyncHttpGetClient.this.path);
					request.headers().set(HttpHeaders.Names.HOST, AsyncHttpGetClient.this.host);
					request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
					AsyncHttpGetClient.this.channel = future.channel();
					AsyncHttpGetClient.this.channel.writeAndFlush(request);
				} else {
					AsyncHttpGetClient.this.dispatchExceptionCaught(future.cause());
				}
			}
		});
	}

	public boolean isRunning() {
		return this.channel != null && this.channel.isOpen();
	}

	public void close() {
		try {
			if(this.channel != null && this.channel.isOpen()) {
				this.channel.close();
			}
		} catch(Exception exception) {
			exception.printStackTrace();
		} finally {
			this.channel = null;
		}
	}

	public void dispatchHttpResponse(String response) {
		for(HttpGetClientListener listener : this.listeners) {
			listener.httpResponse(this, response);
		}
	}

	public void dispatchExceptionCaught(Throwable throwable) {
		for(HttpGetClientListener listener : this.listeners) {
			listener.exceptionCaught(this, throwable);
		}
	}

	public void registerListener(HttpGetClientListener listener) {
		this.listeners.add(listener);
	}

	public void unregisterListener(HttpGetClientListener listener) {
		this.listeners.remove(listener);
	}

}
