package lilypad.packet.common;

import lilypad.packet.common.util.BufferUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	private PacketCodecProvider packetCodecProvider;
	
	public PacketEncoder(PacketCodecProvider packetCodecProvider) {
		this.packetCodecProvider = packetCodecProvider;
	}

	@Override
	protected void encode(ChannelHandlerContext context, Packet packet, ByteBuf out) throws Exception {
		BufferUtils.writeVarInt(out, packet.getOpcode());
		this.packetCodecProvider.getByOpcode(packet.getOpcode()).encodePacket(packet, out);
	}

}
