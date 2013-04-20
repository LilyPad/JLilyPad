package lilypad.client.connect.lib.util;

import java.util.List;

public class StringUtils {
	
	public static String join(List<String> args, String seperator) {
		return join(args.toArray(new String[0]), seperator);
	}
	
	public static String join(String[] args, String seperator) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			if (i != 0) {
				builder.append(seperator);
			}
			builder.append(args[i]);
		}
		return builder.toString();
	}

}