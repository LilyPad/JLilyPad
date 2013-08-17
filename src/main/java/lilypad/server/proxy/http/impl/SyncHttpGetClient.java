package lilypad.server.proxy.http.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import lilypad.server.proxy.http.HttpGetClient;
import lilypad.server.proxy.http.HttpGetClientListener;

public class SyncHttpGetClient implements HttpGetClient {

	private static final long timeout = 2500L;
	
	private URI uri;
	private List<HttpGetClientListener> listeners = new ArrayList<HttpGetClientListener>();
	
	public SyncHttpGetClient(URI uri) {
		this.uri = uri;
	}
	
	public void run() {
		BufferedReader bufferedReader = null;
		try {
			URLConnection urlConnection = this.uri.toURL().openConnection();
			if(urlConnection instanceof HttpsURLConnection) {
				HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urlConnection;
				httpsURLConnection.setSSLSocketFactory(DummyTrustManager.getDummySSLContext().getSocketFactory());
			}
			urlConnection.setConnectTimeout((int) timeout);
			urlConnection.setReadTimeout((int) timeout);
			urlConnection.connect();
			bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = bufferedReader.readLine()) != null) {
				response.append(line + "\r\n");
			}
			this.dispatchHttpResponse(response.toString());
		} catch(Exception exception) {
			this.dispatchExceptionCaught(exception);
		} finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch(Exception exception) {
					//ignore
				}
			}
		}
	}
	
	public void dispatchHttpResponse(String response) {
		for(HttpGetClientListener listener : this.listeners) {
			listener.httpResponse(this, response);
		}
	}
	
	public void dispatchExceptionCaught(Throwable throwable) {
		for(HttpGetClientListener listener : this.listeners) {
			listener.exceptionCaught(this, throwable);
		}
	}

	public void registerListener(HttpGetClientListener listener) {
		this.listeners.add(listener);
	}

	public void unregisterListener(HttpGetClientListener listener) {
		this.listeners.remove(listener);
	}

}
