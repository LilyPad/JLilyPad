package lilypad.server.connect.query;

import lilypad.packet.connect.impl.ResultPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public interface Query<T> {

	public ResultPacket execute(T sender, int id, ByteBuf in, ByteBufAllocator alloc);
	
	public int getId();
	
}
