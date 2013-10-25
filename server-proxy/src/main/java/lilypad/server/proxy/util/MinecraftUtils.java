package lilypad.server.proxy.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class MinecraftUtils {

	public static URI getSessionURI(String username, String serverKey, boolean ssl) {
		if(username == null || serverKey == null) {
			return null;
		}
		try {
			return new URI((ssl ? "https" : "http") + "://sessionserver.mojang.com/session/minecraft/hasJoined?username=" + URLEncoder.encode(username, "UTF-8") + "&serverId=" + URLEncoder.encode(serverKey, "UTF-8"));
		} catch(UnsupportedEncodingException exception) {
			exception.printStackTrace();
		} catch(URISyntaxException exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
}
