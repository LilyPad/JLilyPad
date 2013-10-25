package lilypad.server.connect.query;

import lilypad.packet.connect.impl.ResultPacket;
import io.netty.buffer.ByteBuf;

public interface Query<T> {

	public ResultPacket execute(T sender, int id, ByteBuf payload);
	
	public int getId();
	
}
