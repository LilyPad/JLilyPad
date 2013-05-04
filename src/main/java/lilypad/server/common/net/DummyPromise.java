package lilypad.server.common.net;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class DummyPromise implements ChannelPromise {
	
	private Channel channel;
	
	public DummyPromise(Channel channel) {
		this.channel = channel;
	}
	
	public Channel channel() {
		return this.channel;
	}

	public boolean await(long time) throws InterruptedException {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean await(long time, TimeUnit timeUnit) throws InterruptedException {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean awaitUninterruptibly(long time) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean awaitUninterruptibly(long time, TimeUnit timeUnit) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public Throwable cause() {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean isDone() {
		return false;
	}

	public boolean isSuccess() {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean tryFailure(Throwable throwable) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public boolean trySuccess() {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise addListener(GenericFutureListener<? extends Future> listener) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise addListeners(GenericFutureListener<? extends Future>... listeners) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise await() throws InterruptedException {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise awaitUninterruptibly() {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise removeListener(GenericFutureListener<? extends Future> listener) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise removeListeners(GenericFutureListener<? extends Future>... listeners) {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise setFailure(Throwable throwable) {
		return this;
	}

	public ChannelPromise setSuccess() {
		return this;
	}

	public ChannelPromise sync() throws InterruptedException {
		throw new UnsupportedOperationException("We're just a dummy");
	}

	public ChannelPromise syncUninterruptibly() {
		throw new UnsupportedOperationException("We're just a dummy");
	}

}
