package lilypad.server.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	private static final Gson gson = new Gson();
	private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	
	public static Gson gson() {
		return gson;
	}
	
	public static Gson prettyGson() {
		return prettyGson;
	}
	
}
