package lilypad.server.proxy.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class MinecraftUtils {

	public static URI getSessionURI(String username, String serverKey) {
		if(username == null || serverKey == null) {
			return null;
		}
		try {
			return new URI("http://session.minecraft.net/game/checkserver.jsp?user=" + URLEncoder.encode(username, "UTF-8") + "&serverId=" + URLEncoder.encode(serverKey, "UTF-8"));
		} catch(UnsupportedEncodingException exception) {
			exception.printStackTrace();
		} catch(URISyntaxException exception) {
			exception.printStackTrace();
		}
		return null;
	}
	
}
