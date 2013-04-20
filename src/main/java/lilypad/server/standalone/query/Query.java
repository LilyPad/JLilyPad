package lilypad.server.standalone.query;

import java.util.Scanner;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.lib.ConnectImpl;
import lilypad.server.common.config.FileConfigGson;
import lilypad.server.common.service.ServiceManager;
import lilypad.server.query.tcp.QueryTcpService;
import lilypad.server.query.udp.QueryUdpService;

public class Query {

	public static final String build = Query.class.getPackage().getImplementationVersion();

	public static void main(String[] args) {
		System.out.println("[LilyPad] build " + build + " of Query");

		QueryTcpService queryTcpService = new QueryTcpService();
		QueryUdpService queryUdpService = new QueryUdpService();

		FileConfigGson<QueryConfig> fileConfigJson;
		try {
			fileConfigJson = new FileConfigGson<QueryConfig>("query", QueryConfig.class);
		} catch(Exception exception) {
			System.out.println("[LilyPad] fatal error: config");
			exception.printStackTrace();
			return;
		}
		fileConfigJson.load();
		fileConfigJson.save();

		Config config = fileConfigJson.getConfig();
		QueryCache cache = new QueryCache();
		config.init(cache);

		Connect connect = new ConnectImpl(config);
		QueryCacheUpdater updater = new QueryCacheUpdater(connect, cache);
		updater.start();

		ServiceManager serviceManager = new ServiceManager();
		try {
			serviceManager.enableService(queryTcpService, config);
			serviceManager.enableService(queryUdpService, config);
			Scanner scanner = new Scanner(System.in);
			try {
				String scannerLine;
				while((scannerLine = scanner.next()) != null) {
					if(scannerLine.equals("halt")) {
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
			try {
				serviceManager.disableAllServices();
			} catch(Exception exception) {
				exception.printStackTrace();
			}
			try {
				updater.stop();
			} catch(Exception exception) {
				exception.printStackTrace();
			}
			try {
				connect.close();
			} catch(Exception exception) {
				exception.printStackTrace();
			}
		}
	}

}
