package lilypad.server.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class SecurityUtils {
	
	private static final SecureRandom secureRandom = new SecureRandom();
	
	public static String randomHash() {
		long randomHash;
		synchronized(secureRandom) {
			randomHash = secureRandom.nextLong() & 0xFFFFFFFF;
		}
		return Long.toHexString(randomHash);
	}
	
	public static byte[] randomBytes(int size) {
		byte[] bytes = new byte[size];
		synchronized(secureRandom) {
			secureRandom.nextBytes(bytes);
		}
		return bytes;
	}
	
	public static int randomInt(int size) {
		int i;
		synchronized(secureRandom) {
			i = secureRandom.nextInt(size);
		}
		return i;
	}
	
	public static String shaHex(String string) {
		return shaHex(string.getBytes());
	}
	
	public static String shaHex(byte[]... bytesArray) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			for(byte[] bytes : bytesArray) {
				messageDigest.update(bytes);
			}
			String hash = new BigInteger(1, messageDigest.digest()).toString(16);
			if(hash.length() == 39) {
				hash = "0" + hash;
			}
			return hash;
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public static String mojangShaHex(String string) {
		return mojangShaHex(string.getBytes());
	}
	
	public static String mojangShaHex(byte[]... bytesArray) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			for(byte[] bytes : bytesArray) {
				messageDigest.update(bytes);
			}
			return new BigInteger(messageDigest.digest()).toString(16);
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
}
