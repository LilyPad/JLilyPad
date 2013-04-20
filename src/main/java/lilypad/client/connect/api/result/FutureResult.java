package lilypad.client.connect.api.result;

public interface FutureResult<T extends Result> {
	
	public void registerListener(FutureResultListener<T> futureResultListener);
	
	public void unregisterListener(FutureResultListener<T> futureResultListener);
	
	public T await() throws InterruptedException;
	
	public T await(long timeout) throws InterruptedException;
	
	public T awaitUninterruptibly();
	
	public T awaitUninterruptibly(long timeout);
	
}
