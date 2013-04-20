package lilypad.client.connect.lib.result;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import lilypad.client.connect.api.result.FutureResult;
import lilypad.client.connect.api.result.FutureResultListener;
import lilypad.client.connect.api.result.Result;

public class FutureResultImpl<T extends Result> implements FutureResult<T> {

	private T result;
	private Class<T> resultClass;
	private boolean resultCancelled;
	private Set<FutureResultListener<T>> resultListeners = new HashSet<FutureResultListener<T>>();
	private ReentrantLock resultLock = new ReentrantLock();

	public FutureResultImpl(Class<T> resultClass) {
		this.resultClass = resultClass;
	}
	
	public void registerListener(FutureResultListener<T> futureResultListener) {
		this.resultLock.lock();
		try {
			if(this.result != null || this.resultCancelled) {
				futureResultListener.onResult(result);
				return;
			}
			this.resultListeners.add(futureResultListener);
		} finally {
			this.resultLock.unlock();
		}
	}

	public void unregisterListener(FutureResultListener<T> futureResultListener) {
		this.resultLock.lock();
		try {
			this.resultListeners.remove(futureResultListener);
		} finally {
			this.resultLock.unlock();
		}
	}

	public synchronized T await() throws InterruptedException {
		if(this.result == null && !this.resultCancelled) {
			this.wait();
		}
		return this.result;
	}

	public synchronized T await(long timeout) throws InterruptedException {
		if(this.result == null && !this.resultCancelled) {
			this.wait(timeout);
		}
		return this.result;
	}

	public synchronized T awaitUninterruptibly() {
		if(this.result == null && !this.resultCancelled) {
			do {
				try {
					this.wait();
					break;
				} catch(InterruptedException exception) {
					//ignore
				}
			} while(true);
		}
		return this.result;
	}

	public synchronized T awaitUninterruptibly(long timeout) {
		if(this.result == null && !this.resultCancelled) {
			long start = System.currentTimeMillis();
			do {
				try {
					this.wait(timeout - (System.currentTimeMillis() - start));
					break;
				} catch(InterruptedException exception) {
					//ignore
				}
			} while(true);
		}
		return this.result;
	}

	public synchronized void notifyResult(T result) {
		this.resultLock.lock();
		try {
			this.result = result;
			this.notifyAll();
			for(FutureResultListener<T> futureResultListener : this.resultListeners) {
				futureResultListener.onResult(this.result);
			}
		} finally {
			this.resultLock.unlock();
		}
	}
	
	public synchronized void cancel() {
		this.resultLock.lock();
		try {
			this.resultCancelled = true;
			this.notifyAll();
		} finally {
			this.resultLock.unlock();
		}
	}
	
	public Class<T> getResultClass() {
		return this.resultClass;
	}

}
