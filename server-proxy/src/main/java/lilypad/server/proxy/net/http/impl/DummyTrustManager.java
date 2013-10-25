package lilypad.server.proxy.net.http.impl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DummyTrustManager implements X509TrustManager {

	private static SSLContext dummySSLContext;
	private static ReentrantLock dummySSLContextLock = new ReentrantLock();
	public static final DummyTrustManager instance = new DummyTrustManager();

	public static SSLContext getDummySSLContext() {
		if(dummySSLContext != null) {
			return dummySSLContext;
		}
		dummySSLContextLock.lock();
		try {
			if(dummySSLContext == null) {
				try {
					dummySSLContext = SSLContext.getInstance("TLS");
				} catch (NoSuchAlgorithmException exception) {
					exception.printStackTrace();
					return null;
				}
				try {
					dummySSLContext.init(null, new TrustManager[] { instance }, null);
				} catch (KeyManagementException exception) {
					exception.printStackTrace();
					return null;
				}
			}
		} finally {
			dummySSLContextLock.unlock();
		}
		return dummySSLContext;
	}

	private DummyTrustManager() {

	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

	public void checkClientTrusted(X509Certificate[] chain, String authType) {
		// blank
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType) {
		// blank
	}

}
