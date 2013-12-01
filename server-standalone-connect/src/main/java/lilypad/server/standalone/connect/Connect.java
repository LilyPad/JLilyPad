package lilypad.server.standalone.connect;

import java.util.Scanner;

import lilypad.server.common.config.FileConfigGson;
import lilypad.server.common.service.ServiceManager;
import lilypad.server.connect.ConnectService;

public class Connect {

	public static final String build = Connect.class.getPackage().getImplementationVersion();

	public static void main(String[] args) {
		System.out.println("[LilyPad] build " + build + " of Connect");

		ConnectService connectService = new ConnectService();

		FileConfigGson<ConnectConfig> fileConfigJson;
		try {
			fileConfigJson = new FileConfigGson<ConnectConfig>("connect", ConnectConfig.class);
		} catch(Exception exception) {
			System.out.println("[LilyPad] fatal error: config");
			exception.printStackTrace();
			return;
		}
		fileConfigJson.load();
		fileConfigJson.save();
		Config config = fileConfigJson.getConfig();
		config.init(new ConnectPlayable(connectService));

		ServiceManager serviceManager = new ServiceManager();
		try {
			serviceManager.enableService(connectService, config);
			Scanner scanner = new Scanner(System.in);
			try {
				String scannerLine;
				while((scannerLine = scanner.next()) != null) {
					if(scannerLine.equals("halt") || scannerLine.equals("stop")) {
						break;
					}
				}
			} finally {
				scanner.close();
			}
		} catch(Exception exception) {
			System.out.println("[LilyPad] fatal error: services");
			exception.printStackTrace();
			return;
		} finally {
			serviceManager.disableAllServices();
		}
	}
	
}
