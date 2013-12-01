package lilypad.server.standalone.allinone;

import java.util.Scanner;

import lilypad.server.common.config.FileConfigGson;
import lilypad.server.common.service.ServiceManager;
import lilypad.server.connect.ConnectService;
import lilypad.server.proxy.ProxyService;
import lilypad.server.proxy.util.CipherUtils;
import lilypad.server.query.tcp.QueryTcpService;
import lilypad.server.query.udp.QueryUdpService;

public class AllInOne {

	public static final String build = AllInOne.class.getPackage().getImplementationVersion();

	public static void main(String[] args) {
		System.out.println("[LilyPad] build " + build + " of AllInOne");

		ConnectService connectService = new ConnectService();
		QueryTcpService queryTcpService = new QueryTcpService();
		QueryUdpService queryUdpService = new QueryUdpService();
		ProxyService proxyService = new ProxyService();

		FileConfigGson<AllInOneConfig> fileConfigJson;
		try {
			fileConfigJson = new FileConfigGson<AllInOneConfig>("allinone", AllInOneConfig.class);
		} catch(Exception exception) {
			System.out.println("[LilyPad] fatal error: config");
			exception.printStackTrace();
			return;
		}
		fileConfigJson.load();
		fileConfigJson.save();
		Config config = fileConfigJson.getConfig();
		config.init(proxyService, connectService, CipherUtils.generateRSAKeyPair(1024));

		ServiceManager serviceManager = new ServiceManager();
		try {
			serviceManager.enableService(connectService, config);
			serviceManager.enableService(queryTcpService, config);
			serviceManager.enableService(queryUdpService, config);
			serviceManager.enableService(proxyService, config);
			Scanner scanner = new Scanner(System.in);
			try {
				String scannerLine;
				while((scannerLine = scanner.next()) != null) {
					if(scannerLine.equals("stop") || scannerLine.equals("halt")) {
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
