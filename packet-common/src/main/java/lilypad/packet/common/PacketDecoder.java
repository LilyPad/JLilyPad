package lilypad.packet.common;

import java.util.List;

import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	private PacketCodecProvider packetCodecProvider;

	public PacketDecoder(PacketCodecProvider packetCodecProvider) {
		this.packetCodecProvider = packetCodecProvider;
	}

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) throws Exception {
		if(!buffer.isReadable()) {
			return;
		}
		int opcode = BufferUtils.readVarInt(buffer);
		PacketCodec<?> packetCodec = this.packetCodecProvider.getByOpcode(opcode);
		if(packetCodec == null) {
			context.close();
			return;
		}
		out.add(packetCodec.decode(buffer));
	}

}
