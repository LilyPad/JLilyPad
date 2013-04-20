package lilypad.packet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundMessageHandlerAdapter;

public class PacketEncoder extends ChannelOutboundMessageHandlerAdapter<Packet> {

	private PacketCodecRegistry packetCodecRegistry;
	private ByteBuf outboundBuffer;
	
	public PacketEncoder(PacketCodecRegistry packetCodecRegistry) {
		this.packetCodecRegistry = packetCodecRegistry;
		this.outboundBuffer = Unpooled.buffer();
	}

	public void flush(ChannelHandlerContext context, Packet packet) throws Exception {
		this.outboundBuffer.writeByte(packet.getOpcode());
		this.packetCodecRegistry.getOpcode(packet.getOpcode()).encodePacket(packet, this.outboundBuffer);
		context.nextOutboundByteBuffer().writeBytes(this.outboundBuffer);
		this.outboundBuffer.discardSomeReadBytes();
	}
	
	public void freeOutboundBuffer(ChannelHandlerContext context) throws Exception {
		super.freeOutboundBuffer(context);
		try {
			this.outboundBuffer.release();
		} catch(Exception exception) {
			// ignore
		} finally {
			this.outboundBuffer = null;
		}
	}

}
