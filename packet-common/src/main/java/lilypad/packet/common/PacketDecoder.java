package lilypad.packet.common;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class PacketDecoder extends ReplayingDecoder<Void> {

	private PacketCodecRegistry packetCodecRegistry;

	public PacketDecoder(PacketCodecRegistry packetCodecRegistry) {
		this.packetCodecRegistry = packetCodecRegistry;
	}

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) throws Exception {
		while(true) {
			PacketCodec<?> packetCodec = this.packetCodecRegistry.getOpcode(buffer.readUnsignedByte());
			if(packetCodec == null) {
				context.close();
				return;
			}
			out.add(packetCodec.decode(buffer));
			super.checkpoint();
		}
	}

}
