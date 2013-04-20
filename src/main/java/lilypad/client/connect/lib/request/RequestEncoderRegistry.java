package lilypad.client.connect.lib.request;

import java.util.HashMap;
import java.util.Map;

import lilypad.client.connect.api.request.Request;

public class RequestEncoderRegistry {

	private Map<Class<? extends Request<?>>, RequestEncoder<?>> registryByRequest = new HashMap<Class<? extends Request<?>>, RequestEncoder<?>>();
	private RequestEncoder<?>[] registryById = new  RequestEncoder<?>[256];
	
	public void submit(RequestEncoder<?> requestEncoder) {
		if(requestEncoder == null) {
			return;
		}
		this.registryByRequest.put(requestEncoder.getRequest(), requestEncoder);
		this.registryById[requestEncoder.getId()] = requestEncoder;
	}
	
	public RequestEncoder<?> getById(int id) {
		return this.registryById[id];
	}
	
	public RequestEncoder<?> getByRequest(Class<?> request) {
		return this.registryByRequest.get(request);
	}
	
}
