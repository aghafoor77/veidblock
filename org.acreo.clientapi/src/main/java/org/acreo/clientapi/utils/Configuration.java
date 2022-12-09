package org.acreo.clientapi.utils;

import java.util.Objects;

import org.acreo.common.exceptions.VeidblockException;

public class Configuration {

	public static final String AUTH_SERVER = "authServer";
	public static final String IPV_SERVER = "ipvServer";
	public static final String LEDGER_SERVER = "ledgerServer";

	private static String authServer ="http://localhost:9000";///
											// verify
	private static String ipvServer = "http://localhost:8000";/// IPV
	private static String ledgerServer = "http://localhost:10000";/// vc

	public Configuration() {

	}

	public Configuration(String authServer_, String ipvServer_, String ledgerServer_) {
		authServer = authServer_;
		System.out.println("authServer  : "+authServer );
		ipvServer = ipvServer_;
		System.out.println("ipvServer   : "+ipvServer  );
		ledgerServer = ledgerServer_;
		System.out.println("ledgerServer : "+ledgerServer );
	}

	public String getAuthServerUrl() {
		if (Objects.isNull(authServer)) {
			try {
				throw new VeidblockException(
						"Autheticatin service end-point not exists. Please specify in config.props file !");
			} catch (VeidblockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return authServer;
	}

	public String getIPVServerUrl() {
		if (Objects.isNull(ipvServer)) {
			try {
				throw new VeidblockException("IPV service end-point not exists. Please specify in config.props file !");
			} catch (VeidblockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ipvServer;
	}

	public String getLedgerServer() {
		if (Objects.isNull(ledgerServer)) {
			try {
				throw new VeidblockException(
						"Ledger service end-point not exists. Please specify in config.props file !");
			} catch (VeidblockException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ledgerServer;
	}

}
