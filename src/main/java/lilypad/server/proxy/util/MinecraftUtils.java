package lilypad.server.proxy.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class MinecraftUtils {

	public static boolean authenticate(String username, String serverKey) {
		if(username == null || serverKey == null) {
			return false;
		}
		BufferedReader bufferedReader = null;
		try {
			URLConnection urlConnection = new URL("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(username, "UTF-8") + "&serverId=" + URLEncoder.encode(serverKey, "UTF-8")).openConnection();
			urlConnection.setConnectTimeout(2000);
			urlConnection.setReadTimeout(2000);
			urlConnection.connect();
			bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String response = bufferedReader.readLine();
			if(response != null && response.trim().equals("YES")) {
				return true;
			} else {
				return false;
			}
		} catch(Exception exception) {
			exception.printStackTrace();
			return false;
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
	
}
