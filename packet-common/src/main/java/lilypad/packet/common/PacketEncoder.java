package lilypad.packet.common;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private PacketCodecRegistry packetCodecRegistry;
	private ByteBuf outboundBuffer;
	
	public PacketEncoder(PacketCodecRegistry packetCodecRegistry) {
		this.packetCodecRegistry = packetCodecRegistry;
		this.outboundBuffer = Unpooled.buffer();
	}

	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, ByteBuf out) throws Exception {
		this.outboundBuffer.writeByte(packet.getOpcode());
		this.packetCodecRegistry.getOpcode(packet.getOpcode()).encodePacket(packet, this.outboundBuffer);
		out.writeBytes(this.outboundBuffer);
		this.outboundBuffer.discardSomeReadBytes();
	}

}
