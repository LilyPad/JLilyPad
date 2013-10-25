package lilypad.packet.common.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import io.netty.buffer.ByteBuf;

public class BufferUtils {

	public static String readString16(ByteBuf buffer) {
		StringBuilder stringBuilder = new StringBuilder();
		int length = buffer.readUnsignedShort();
		while(length-- != 0) {
			stringBuilder.append((char) buffer.readUnsignedShort());
		}
		return stringBuilder.toString();
	}
	
	public static void writeString16(String string, ByteBuf buffer) {
		buffer.writeShort(string.length());
		for(char c : string.toCharArray()) {
			buffer.writeShort(c);
		}
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
	
	public static void writePublicKey(PublicKey publicKey, ByteBuf buffer) {
		byte[] encoded = publicKey.getEncoded();
		buffer.writeShort(encoded.length);
		buffer.writeBytes(encoded);
	}
	
}
