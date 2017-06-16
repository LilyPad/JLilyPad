package lilypad.packet.common.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import io.netty.buffer.ByteBuf;
import io.netty.util.CharsetUtil;

public class BufferUtils {

	public static int readVarInt(ByteBuf buffer) {
		int value = 0;
		int bytes = 0;
		byte in;
		while(true) {
			in = buffer.readByte();
			value |= (in & 0x7F) << (bytes++ * 7);
			if(bytes > 32) {
				throw new IllegalArgumentException("VarInt is too long: " + bytes);
			}
			if((in & 0x80) == 0x80) {
				continue;
			}
			break;
		}
		return value;
	}

	public static void writeVarInt(ByteBuf buffer, int value) {
		byte in;
		while(true) {
			in = (byte) (value & 0x7F);
			value >>>= 7;
			if(value != 0) {
				in |= 0x80;
			}
			buffer.writeByte(in);
			if(value != 0) {
				continue;
			}
			break;
		}
	}

	public static String readString(ByteBuf buffer) {
		byte[] bytes = new byte[readVarInt(buffer)];
		buffer.readBytes(bytes);
		return new String(bytes, CharsetUtil.UTF_8);
	}

	public static void writeString(ByteBuf buffer, String string) {
		byte[] bytes = string.getBytes(CharsetUtil.UTF_8);
		writeVarInt(buffer, bytes.length);
		buffer.writeBytes(bytes);
	}

	public static byte[] readBytes(ByteBuf from, int length) {
		byte[] data = new byte[length];
		from.readBytes(data);
		return data;
	}

	public static PublicKey readPublicKey(ByteBuf buffer) {
		int size = buffer.readUnsignedShort();
		try {
			X509EncodedKeySpec x509 = new X509EncodedKeySpec(buffer.readBytes(size).array());
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return keyFactory.generatePublic(x509);
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
		} catch (InvalidKeySpecException exception) {
			exception.printStackTrace();
		}
		return null;
	}

	public static void writePublicKey(ByteBuf buffer, PublicKey publicKey) {
		byte[] encoded = publicKey.getEncoded();
		buffer.writeShort(encoded.length);
		buffer.writeBytes(encoded);
	}

}
