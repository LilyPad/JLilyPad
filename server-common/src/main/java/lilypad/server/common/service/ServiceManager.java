package lilypad.server.common.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import lilypad.server.common.config.IConfig;

public class ServiceManager {

	private Set<Service<?>> services = new HashSet<Service<?>>();
	private ReentrantLock servicesLock = new ReentrantLock();

	public <T extends IConfig> void enableService(Service<T> service, T config) throws Exception {
		this.servicesLock.lock();
		try {
			service.enable(config);
			this.services.add(service);
		} catch(Exception exception) {
			service.disable();
			throw exception;
		} finally {
			this.servicesLock.unlock();
		}
	}

	public void disableService(Service<?> service) {
		this.servicesLock.lock();
		try {
			if(service.isRunning()) {
				service.disable();
			}
			this.services.remove(service);
		} finally {
			this.servicesLock.unlock();
		}
	}

	public void disableAllServices() {
		this.servicesLock.lock();
		try {
			Iterator<Service<?>> services = this.services.iterator();
			Service<?> service;
			while(services.hasNext()) {
				service = services.next();
				if(service.isRunning()) {
					service.disable();
				}
				services.remove();
			}
		} finally {
			this.servicesLock.unlock();
		}
	}

	public boolean hasService(Service<?> service) {
		return this.services.contains(service);
	}

}
