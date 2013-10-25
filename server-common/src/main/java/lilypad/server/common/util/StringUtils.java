package lilypad.server.common.util;

public class StringUtils {

	public static String concat(String[] args, String seperator) {
		StringBuilder stringBuilder = new StringBuilder();
		for(int i = 0; i < args.length; i++) {
			if(i != 0) {
				stringBuilder.append(seperator);
			}
			stringBuilder.append(args[i]);
		}
		return stringBuilder.toString();
	}
	
	public static String[] shift(String[] args, int count) {
		String[] result = new String[args.length - count];
		for(int i = 0; i < result.length; i++) {
			result[i] = args[(i + count)];
		}
		return result;
	}
	
}
