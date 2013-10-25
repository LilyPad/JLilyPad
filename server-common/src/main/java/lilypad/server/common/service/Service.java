package lilypad.server.common.service;

import lilypad.server.common.config.IConfig;

public abstract class Service<T extends IConfig> {

	public abstract void enable(T config) throws Exception;
	
	public abstract void disable();
	
	public abstract boolean isRunning();
	
}
