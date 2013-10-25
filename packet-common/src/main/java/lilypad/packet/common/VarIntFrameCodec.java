package lilypad.packet.common;

import java.util.List;

import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.CorruptedFrameException;

@Sharable
public class VarIntFrameCodec extends ByteToMessageCodec<ByteBuf> {

	protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
		in.markReaderIndex();
		byte[] buffer = new byte[3];
		for(int i = 0; i < buffer.length; i++) {
			if(!in.isReadable()) {
				in.resetReaderIndex();
				return;
			}
			buffer[i] = in.readByte();
			if(buffer[i] >= 0) {
				int size = BufferUtils.readVarInt(Unpooled.wrappedBuffer(buffer));
				if(size > in.readableBytes()) {
					in.resetReaderIndex();
					return;
				}
				out.add(in.readBytes(size));
				return;
			}
		}
		throw new CorruptedFrameException("VarInt size is longer than 21-bit");
	}

	protected void encode(ChannelHandlerContext context, ByteBuf in, ByteBuf out) throws Exception {
		BufferUtils.writeVarInt(out, in.readableBytes());
		out.writeBytes(in);
	}

}
