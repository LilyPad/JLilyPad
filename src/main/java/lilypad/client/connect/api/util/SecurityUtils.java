package lilypad.client.connect.api.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SecurityUtils {

	public static String shaHex(String string) {
		return shaHex(string.getBytes());
	}
	
	public static String shaHex(byte[]... bytesArray) {
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
