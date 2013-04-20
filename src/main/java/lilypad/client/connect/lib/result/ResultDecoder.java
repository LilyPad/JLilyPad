package lilypad.client.connect.lib.result;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.result.Result;
import lilypad.client.connect.api.result.StatusCode;

public interface ResultDecoder<T extends Result> {

	public T decode(StatusCode statusCode, ByteBuf buffer);

	public int getId();
	
	public Class<T> getResult();
	
}
