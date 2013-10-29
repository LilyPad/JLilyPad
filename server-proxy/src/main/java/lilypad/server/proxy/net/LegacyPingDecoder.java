package lilypad.server.proxy.net;

import java.util.List;

import lilypad.packet.common.VarIntFrameCodec;
import lilypad.server.common.IPlayerCallback;
import lilypad.server.proxy.ProxyConfig;
import lilypad.server.proxy.packet.MinecraftPacketConstants;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class LegacyPingDecoder extends ByteToMessageDecoder {

	private ProxyConfig config;
	private ProxySessionMapper sessionMapper;
	
	public LegacyPingDecoder(ProxyConfig config, ProxySessionMapper sessionMapper) {
		this.config = config;
		this.sessionMapper = sessionMapper;
	}
	
	protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
		if(!in.isReadable()) {
			return;
		}
		in.markReaderIndex();
		if(in.readUnsignedByte() != 0xFE) {
			in.resetReaderIndex();
			context.pipeline().remove(this);
			return;
		}
		in.readerIndex(in.writerIndex());
		in.markReaderIndex();
		context.pipeline().remove(this);
		context.pipeline().remove(VarIntFrameCodec.class);
		IPlayerCallback playerCallback = this.config.proxy_getPlayerCallback();
		int playerCount;
		int playerMaximum;
		if(playerCallback != null) {
			playerCount = playerCallback.getPlayerCount();
			playerMaximum = playerCallback.getPlayerMaximum();
		} else {
			playerCount = this.sessionMapper.getAuthenticatedSize();
			playerMaximum = this.config.proxy_getPlayerMaximum();
		}
		ByteBuf buffer = context.alloc().buffer();
		buffer.writeByte(0xFF);
		char[] chars = (MinecraftPacketConstants.magic + "1\0"
				+ MinecraftPacketConstants.protocolVersion + '\0'
				+ MinecraftPacketConstants.minecraftVersion + '\0'
				+ MinecraftPacketConstants.colorize(this.config.proxy_getPlayerMotd()) + '\0'					
				+ playerCount + '\0'
				+ playerMaximum).toCharArray();
		buffer.writeShort(chars.length);
		for(char c : chars) {
			buffer.writeShort((short) c);
		}
		context.writeAndFlush(buffer);
		context.close();
	}

}
