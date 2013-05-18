package lilypad.packet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.MessageBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class PacketDecoder extends ReplayingDecoder<Void> {

	private PacketCodecRegistry packetCodecRegistry;
	
	public PacketDecoder(PacketCodecRegistry packetCodecRegistry) {
		this.packetCodecRegistry = packetCodecRegistry;
	}
	
	public void decode(ChannelHandlerContext context, ByteBuf buffer, MessageBuf<Object> messageBuf) throws Exception {
		messageBuf.add(this.packetCodecRegistry.getOpcode(buffer.readUnsignedByte()).decode(buffer));
	}

}
