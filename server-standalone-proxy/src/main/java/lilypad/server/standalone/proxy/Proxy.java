package lilypad.server.standalone.proxy;

import java.util.Scanner;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.lib.ConnectImpl;
import lilypad.server.common.config.FileConfigGson;
import lilypad.server.common.service.ServiceManager;
import lilypad.server.proxy.ProxyService;
import lilypad.server.proxy.util.CipherUtils;

public class Proxy {

	public static final String build = Proxy.class.getPackage().getImplementationVersion();

	public static void main(String[] args) {
		System.out.println("[LilyPad] build " + build + " of Proxy");

		ProxyService proxyService = new ProxyService();

		FileConfigGson<ProxyConfig> fileConfigJson;
		try {
			fileConfigJson = new FileConfigGson<ProxyConfig>("proxy", ProxyConfig.class);
		} catch(Exception exception) {
			System.out.println("[LilyPad] fatal error: config");
			exception.printStackTrace();
			return;
		}
		fileConfigJson.load();
		fileConfigJson.save();

		ProxyConfig config = fileConfigJson.getConfig();
		Connect connect = new ConnectImpl(config, config.proxy_getBindAddress().getHostName());
		ConnectServerSource connectServerSource = new ConnectServerSource(connect);
		ConnectPlayerCallback connectPlayerCallback = new ConnectPlayerCallback(connect);
		connect.registerEvents(connectServerSource);
		connect.registerEvents(new ConnectServerRedirect(connectServerSource, proxyService));
		config.init(connectServerSource, connectPlayerCallback, CipherUtils.generateRSAKeyPair(1024));

		ConnectThread connectThread = new ConnectThread(connect, connectServerSource, connectPlayerCallback, config);
		connectThread.start();

		ServiceManager serviceManager = new ServiceManager();
		try {
			serviceManager.enableService(proxyService, config);
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
			try {
				serviceManager.disableAllServices();
			} catch(Exception exception) {
				exception.printStackTrace();
			}
			try {
				connectThread.stop();
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
