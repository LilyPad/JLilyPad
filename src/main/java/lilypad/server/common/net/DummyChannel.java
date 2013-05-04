package lilypad.server.common.net;

import java.net.SocketAddress;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.MessageBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoop;
import io.netty.channel.FileRegion;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

public class DummyChannel implements Channel {

	private Channel channel;
	private DummyPromise writePromise;
	
	public DummyChannel(Channel channel) {
		this.channel = channel;
		this.writePromise = new DummyPromise(channel);
	}
	
	public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
		return this.channel.attr(attributeKey);
	}

	public ChannelFuture bind(SocketAddress socketAddress) {
		return this.channel.bind(socketAddress);
	}

	public ChannelFuture bind(SocketAddress socketAddress, ChannelPromise channelPromise) {
		return this.channel.bind(socketAddress, channelPromise);
	}

	public ChannelFuture close() {
		return this.channel.close();
	}

	public ChannelFuture close(ChannelPromise channelPromise) {
		return this.channel.close(channelPromise);
	}

	public ChannelFuture connect(SocketAddress socketAddress) {
		return this.channel.connect(socketAddress);
	}

	public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress) {
		return this.channel.connect(remoteAddress, localAddress);
	}

	public ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise channelPromise) {
		return this.channel.connect(remoteAddress, channelPromise);
	}

	public ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise channelPromise) {
		return this.channel.connect(remoteAddress, localAddress, channelPromise);
	}

	public ChannelFuture deregister() {
		return this.channel.deregister();
	}

	public ChannelFuture deregister(ChannelPromise channelPromise) {
		return this.channel.deregister(channelPromise);
	}

	public ChannelFuture disconnect() {
		return this.channel.disconnect();
	}

	public ChannelFuture disconnect(ChannelPromise channelPromise) {
		return this.channel.disconnect(channelPromise);
	}

	public ChannelFuture flush() {
		return this.channel.flush();
	}

	public ChannelFuture flush(ChannelPromise channelPromise) {
		return this.channel.flush(channelPromise);
	}

	public void read() {
		this.channel.read();
	}

	public ChannelFuture sendFile(FileRegion fileRegion) {
		return this.channel.sendFile(fileRegion);
	}

	public ChannelFuture sendFile(FileRegion fileRegion, ChannelPromise channelPromise) {
		return this.channel.sendFile(fileRegion, channelPromise);
	}

	public ChannelFuture write(Object object) {
		return this.channel.write(object, this.writePromise);
	}

	public ChannelFuture write(Object object, ChannelPromise channelPromise) {
		return this.channel.write(object, channelPromise);
	}

	public ByteBufAllocator alloc() {
		return this.channel.alloc();
	}

	public ChannelFuture newFailedFuture(Throwable throwable) {
		return this.channel.newFailedFuture(throwable);
	}

	public ChannelPromise newPromise() {
		return this.channel.newPromise();
	}

	public ChannelFuture newSucceededFuture() {
		return this.channel.newSucceededFuture();
	}

	public ChannelPipeline pipeline() {
		return this.channel.pipeline();
	}

	public int compareTo(Channel channel) {
		return this.channel.compareTo(channel);
	}

	public ChannelFuture closeFuture() {
		return this.channel.closeFuture();
	}
	
	public ChannelConfig config() {
		return this.channel.config();
	}

	public EventLoop eventLoop() {
		return this.channel.eventLoop();
	}

	public Integer id() {
		return this.channel.id();
	}

	public boolean isActive() {
		return this.channel.isActive();
	}

	public boolean isOpen() {
		return this.channel.isOpen();
	}

	public boolean isRegistered() {
		return this.channel.isRegistered();
	}

	public SocketAddress localAddress() {
		return this.channel.localAddress();
	}

	public ChannelMetadata metadata() {
		return this.channel.metadata();
	}

	public ByteBuf outboundByteBuffer() {
		return this.channel.outboundByteBuffer();
	}

	public <T> MessageBuf<T> outboundMessageBuffer() {
		return this.channel.outboundMessageBuffer();
	}

	public Channel parent() {
		return this.channel.parent();
	}

	public SocketAddress remoteAddress() {
		return this.channel.remoteAddress();
	}

	public Unsafe unsafe() {
		return this.channel.unsafe();
	}

}
