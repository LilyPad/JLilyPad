package lilypad.server.proxy.net;

import java.util.List;

import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class AesCodec extends ByteToMessageCodec<ByteBuf> {

	private static final int bufferSize = 1024;

	private BufferedBlockCipher aesCipherDecoder = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
	private BufferedBlockCipher aesCipherEncoder = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
	private byte[] decoderIn;
	private byte[] decoderOut;
	private byte[] encoderIn;
	private byte[] encoderOut;

	public AesCodec(byte[] iv) {
		this.aesCipherDecoder.init(false, new ParametersWithIV(new KeyParameter(iv), iv, 0, 16));
		this.aesCipherEncoder.init(true, new ParametersWithIV(new KeyParameter(iv), iv, 0, 16));
		this.decoderIn = new byte[bufferSize];
		this.decoderOut = new byte[this.aesCipherDecoder.getOutputSize(bufferSize)];
		this.encoderIn = new byte[bufferSize];
		this.encoderOut = new byte[this.aesCipherEncoder.getOutputSize(bufferSize)];
	}

	protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) {
		ByteBuf buffer = context.alloc().buffer(this.aesCipherDecoder.getOutputSize(in.readableBytes()));
		int read;
		while(in.isReadable()) {
			if(in.readableBytes() > bufferSize) {
				read = bufferSize;
			} else {
				read = in.readableBytes();
			}
			in.readBytes(this.decoderIn, 0, read);
			read = this.aesCipherDecoder.processBytes(this.decoderIn, 0, read, this.decoderOut, 0);
			buffer.writeBytes(this.decoderOut, 0, read);
		}
		out.add(buffer);
	}

	protected void encode(ChannelHandlerContext context, ByteBuf in, ByteBuf out) throws Exception {
		int read;
		while(in.isReadable()) {
			if(in.readableBytes() > bufferSize) {
				read = bufferSize;
			} else {
				read = in.readableBytes();
			}
			in.readBytes(this.encoderIn, 0, read);
			read = this.aesCipherEncoder.processBytes(this.encoderIn, 0, read, this.encoderOut, 0);
			out.writeBytes(this.encoderOut, 0, read);
		}
	}
	
}
