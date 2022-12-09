package org.acreo.database;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DatabaseCretor {

	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * String str = "jdbc:mysql://localhost:3306/veidblock";
	 * System.out.println(str.substring(str.lastIndexOf("/") + 1));
	 * System.out.println(str.substring(0, str.lastIndexOf("/")));
	 * System.out.println(str.substring(0, str.lastIndexOf("/"))); String
	 * queries =
	 * "/home/aghafoor/Desktop/Development/veidblockledger/deployment/veidblockDB.sql";
	 * new DatabaseCretor().createVeidblockDatabase("jdbc:mysql://localhost",
	 * "root", "1234", "veidblock"); new
	 * DatabaseCretor().createVeidblockLedgerDatabase("jdbc:mysql://localhost",
	 * "root", "1234", "veidblockledger");
	 * 
	 * }
	 */

	public void createVeidblockLedgerDatabase(String dbURL, String userName, String password, String dbName)
			throws Exception {
		String postFix = "";// "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		DataSource dataSource = getDatasource(dbURL + postFix, userName, password);
		createDB(dataSource, dbName);
		dataSource = getDatasource(dbURL + "/" + dbName + postFix, userName, password);

		createTables(dataSource, tablesVeidblockLedger);

	}

	public void createVeidblockDatabase(String dbURL, String userName, String password, String dbName)
			throws Exception {
		String postFix = "";// "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

		DataSource dataSource = getDatasource(dbURL + postFix, userName, password);
		createDB(dataSource, dbName);
		dataSource = getDatasource(dbURL + "/" + dbName + postFix, userName, password);
		createTables(dataSource, tablesVeidblock);
	}

	public DataSource getDatasource(String dbURL, String userName, String password) throws Exception {
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUrl(dbURL);
		dataSource.setUser(userName);
		dataSource.setPassword(password);
		return dataSource;
	}

	public void createDB(DataSource dataSource, String dbName) throws Exception {
		ResultSet executeQuery = dataSource.getConnection().createStatement().executeQuery("Show databases;");

		List<String> tables = new ArrayList<>();

		while (executeQuery.next()) {
			tables.add(executeQuery.getString(1));
		}

		if (!tables.contains(dbName)) {
			dataSource.getConnection().createStatement().execute("Create database " + dbName + ";");
		}
	}

	public void createTables(DataSource dataSource, String[] tables) throws Exception {

		for (String query : tables) {
			System.out.println(query);
			query = query.trim();
			dataSource.getConnection().createStatement().executeUpdate(query);
		}
	}

	private String[] loadFile(String queriedFile) throws Exception {

		File file = new File(queriedFile);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		String str = new String(data, "UTF-8");
		String quesries[] = str.split(";");

		return quesries;
	}

	private String[] tablesVeidblock = {

			"CREATE TABLE IF NOT EXISTS `JwsToken` (" + "`jti` bigint(20) NOT NULL, "
					+ "`alg` varchar(255) DEFAULT NULL," + "`type` varchar(255) DEFAULT NULL,"
					+ "`iss` varchar(255) DEFAULT NULL," + "`sub` varchar(255) DEFAULT NULL,"
					+ "`ver` varchar(255) DEFAULT NULL," + " `exp` timestamp NULL DEFAULT NULL,"
					+ " `pub` varchar(2048) DEFAULT NULL," + " `scp` varchar(255) DEFAULT NULL,"
					+ " `refreshToken` varchar(255) DEFAULT NULL," + " `signature` varchar(2048) DEFAULT NULL,"
					+ "  `signerData` varchar(2048) DEFAULT NULL," + " `signerDataType` smallint(6) DEFAULT NULL,"
					+ "  PRIMARY KEY (`jti`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `Organization` (" + "`organizationID` bigint(20) NOT NULL,"
					+ "`city` varchar(255) DEFAULT NULL," + "`country` varchar(255) DEFAULT NULL,"
					+ "`name` varchar(255) NOT NULL," + "`state` varchar(255) DEFAULT NULL,"
					+ "`street` varchar(255) DEFAULT NULL," + "`organizationUnit` varchar(255) DEFAULT NULL,"
					+ " PRIMARY KEY (`organizationID`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `Resource` (" + "`username` varchar(255) NOT NULL,"
					+ "`backupEmail` varchar(255) DEFAULT NULL," + "`creationDate` datetime DEFAULT NULL,"
					+ "`email` varchar(255) DEFAULT NULL," + " `activeStatus` bit(1) NOT NULL,"
					+ " `mobile` varchar(255) DEFAULT NULL," + " `name` varchar(255) DEFAULT NULL,"
					+ " `password` varchar(255) DEFAULT NULL," + " `resourceId` bigint(20) NOT NULL,"
					+ " `phone` varchar(255) DEFAULT NULL," + " `organizationId` bigint(20) DEFAULT NULL,"
					+ " `address1` varchar(255) DEFAULT NULL," + " `address2` varchar(255) DEFAULT NULL,"
					+ " `postCode` varchar(255) DEFAULT NULL," + " `city` varchar(255) DEFAULT NULL,"
					+ " `state` varchar(255) DEFAULT NULL," + " `country` varchar(255) DEFAULT NULL,"
					+ " `location` varchar(255) DEFAULT NULL," + " `rtype` varchar(25) DEFAULT NULL,"
					+ " `url` varchar(255) DEFAULT NULL," + " `createdBy` bigint(20) DEFAULT NULL,"
					+ " PRIMARY KEY (`username`)"
					/*
					 * + " KEY `FK8E48877525D44497` (`organizationId`)," +
					 * " CONSTRAINT `Resource_ibfk_1` FOREIGN KEY (`organizationId`) REFERENCES `Organization` (`organizationID`)"
					 */
					+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `Role` (" + " `roleId` bigint(20) NOT NULL,"
					+ " `description` varchar(255) DEFAULT NULL," + " `role` varchar(255) DEFAULT NULL,"
					+ " `createdBy` bigint(20) DEFAULT NULL," + "  PRIMARY KEY (`roleId`)"
					+ "	) ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `RoleAssignment` (" + " `resourceId` bigint(255) NOT NULL,"
					+ "  `roleId` bigint(20) NOT NULL," + "  PRIMARY KEY (`resourceId`,`roleId`)"
					+ ") ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `OTP` (`resourceId` varchar(128) NOT NULL, `otp` varchar(64) NOT NULL, `sentDate` datetime NOT NULL, `expStatus` int(11) NOT NULL,  PRIMARY KEY (`resourceId`,`otp`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE IF NOT EXISTS `PairDevice` (" + " `deviceId` bigint(255) NOT NULL,"
					+ "  `uid` bigint(20) NOT NULL," + "  `dpc` varchar(255) DEFAULT NULL,"
					+ "  `devicePairDateTime` datetime DEFAULT NULL," + "  `expiryPeriod` bigint(20) DEFAULT NULL,"
					+ "  `deviceNo` bigint(20) DEFAULT NULL," + "  `seqNo` bigint(20) DEFAULT NULL,"
					+ "  PRIMARY KEY (`deviceId`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;",
			"CREATE TABLE IF NOT EXISTS `PairedDevice` ( `deviceId` bigint(20) NOT NULL, "
					+ "`uid` bigint(20) DEFAULT NULL, " + "`devicePairDateTime` datetime DEFAULT NULL, "
					+ "`deviceNo` int(11) DEFAULT NULL, " + "`seqNo` int(11) DEFAULT NULL, "
					+ "`signature` varchar(2048) DEFAULT NULL, " + "`pubKey` varchar(2048) DEFAULT NULL,  "
					+ "PRIMARY KEY (`deviceId`));"

	};

	String tablesVeidblockLedger[] = { "CREATE TABLE `Transaction` (" + " `ref` varchar(255) NOT NULL,"
			+ " `depth` bigint(255) NOT NULL," + " `hashPrevBlock` varchar(64) DEFAULT NULL,"
			+ "	`creationTime` varchar(20) DEFAULT NULL," + " `scope` varchar(255) DEFAULT NULL,"
			+ " `sender` varchar(255) DEFAULT NULL," + " `receiver` longtext, " + " `payload` longtext,"
			+ " `payloadType` varchar(255) DEFAULT NULL," + " `cryptoOperationsOnPayload` varchar(255) DEFAULT NULL,"
			+ "`creatorSignature` longtext," + "`creatorURL` varchar(255) DEFAULT NULL,"
			+ " `signedBy` varchar(255) DEFAULT NULL," + " `signedDate` varchar(20) DEFAULT NULL,"
			+ " `signerUrl` varchar(255) DEFAULT NULL," + " `signature` longtext," + " PRIMARY KEY (`ref`,`depth`)"
			+ "	) ENGINE=InnoDB DEFAULT CHARSET=latin1;",

			"CREATE TABLE `TransactionHeader` (" + "  `ref` varchar(255) NOT NULL,"
					+ " `version` varchar(25) DEFAULT NULL," + "  `hashPrevBlock` varchar(64) DEFAULT NULL,"
					+ "  `creationTime` varchar(20) DEFAULT NULL," + " `hashMerkleRoot` varchar(64) DEFAULT NULL,"
					+ " `extbits` varchar(255) DEFAULT NULL," + " `nonce` varchar(64) DEFAULT NULL,"
					+ " `height` bigint(255) DEFAULT NULL," + " `creator` varchar(255) DEFAULT NULL,"
					+ " `chainName` varchar(255) DEFAULT NULL," + " `smartcontract` longtext,"
					+ " `creatorSignature` longtext," + " `creatorURL` varchar(255) DEFAULT NULL,"
					+ " `signedBy` varchar(255) DEFAULT NULL," + " `signerUrl` varchar(255) DEFAULT NULL,"
					+ " `signature` longtext," + "  PRIMARY KEY (`ref`)" + ") ENGINE=InnoDB DEFAULT CHARSET=latin1;"

	};

}
