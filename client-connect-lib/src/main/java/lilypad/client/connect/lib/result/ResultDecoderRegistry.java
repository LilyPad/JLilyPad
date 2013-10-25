package lilypad.client.connect.lib.result;

import java.util.HashMap;
import java.util.Map;

import lilypad.client.connect.api.result.Result;

public class ResultDecoderRegistry {

	private Map<Class<? extends Result>, ResultDecoder<?>> registryByResult = new HashMap<Class<? extends Result>, ResultDecoder<?>>();
	private ResultDecoder<?>[] registryById = new ResultDecoder<?>[256];
	
	public void submit(ResultDecoder<?> resultDecoder) {
		if(resultDecoder == null) {
			return;
		}
		this.registryByResult.put(resultDecoder.getResult(), resultDecoder);
		this.registryById[resultDecoder.getId()] = resultDecoder;
	}
	
	public ResultDecoder<?> getByResult(Class<? extends Result> resultClass) {
		return this.registryByResult.get(resultClass);
	}
	
	public ResultDecoder<?> getById(int id) {
		return this.registryById[id];
	}
	
}
