package lilypad.client.connect.lib.request;

import io.netty.buffer.ByteBuf;
import lilypad.client.connect.api.request.Request;

public interface RequestEncoder<T extends Request<?>> {

	public void encode(T request, ByteBuf buffer);
	
	public int getId();
	
	public Class<T> getRequest();
	
}
