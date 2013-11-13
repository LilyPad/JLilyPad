package lilypad.client.connect.api.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SecurityUtils {

	/**
	 * Calculates a SHA-1 Hex-Encoded Hash of a String's bytes given 
	 * the default system Charset.
	 * 
	 * @param string input by which to derive the hash
	 * @return SHA-1 Hex-Encoded Hash derived from the input
	 */
	public static String shaHex(String string) {
		return shaHex(string.getBytes());
	}
	
	/**
	 * Calculates a SHA-1 Hex-Encoded Hash of an input.
	 * 
	 * @param bytesArray input by which to derive the hash
	 * @return SHA-1 Hex-Encoded Hash derived from the input
	 */
	public static String shaHex(byte[]... bytesArray) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			for(byte[] bytes : bytesArray) {
				messageDigest.update(bytes);
			}
			return new BigInteger(1, messageDigest.digest()).toString(16);
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
}
