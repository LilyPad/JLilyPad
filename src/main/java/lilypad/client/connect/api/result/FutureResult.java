package lilypad.client.connect.api.result;

public interface FutureResult<T extends Result> {
	
	/**
	 * Registers a listener to receive a callback when the future
	 * has been completed.
	 * 
	 * @param futureResultListener
	 */
	public void registerListener(FutureResultListener<T> futureResultListener);
	
	/**
	 * Unregisters a listener to exclude from receiving a callback
	 * when the future has been completed.
	 * 
	 * @param futureResultListener
	 */
	public void unregisterListener(FutureResultListener<T> futureResultListener);
	
	/**
	 * Awaits a result with no timeout.
	 * 
	 * @return the result, null if cancelled
	 * @throws InterruptedException
	 */
	public T await() throws InterruptedException;
	
	/**
	 * Awaits a result with a timeout in milliseconds.
	 * 
	 * @return the result, null if cancelled
	 * @throws InterruptedException
	 */
	public T await(long timeout) throws InterruptedException;
	
	/**
	 * Awaits a result uninterruptibly with no timeout.
	 * 
	 * @return the result, null if cancelled
	 */
	public T awaitUninterruptibly();
	
	/**
	 * Awaits a result uninterruptibly with a timeout in milliseconds.
	 * 
	 * @return the result, null if cancelled
	 */
	public T awaitUninterruptibly(long timeout);
	
}
