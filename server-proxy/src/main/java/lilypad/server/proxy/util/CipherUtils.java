package lilypad.server.proxy.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class CipherUtils {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public static KeyPair generateRSAKeyPair(int size) {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(size);
			return keyPairGenerator.genKeyPair();
		} catch(Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
}
