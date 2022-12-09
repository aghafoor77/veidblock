package org.acreo.ip.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.acreo.ip.entities.JwsTokenDb;
import org.acreo.ip.entities.Organization;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class JWSTokenMapper implements ResultSetMapper<JwsTokenDb> {
	private static final String JTI = "jti";
	private static final String ALG = "alg";
	private static final String TYPE = "type";
	private static final String ISS = "iss";
	private static final String SUB = "sub";
	private static final String VER = "ver";
	private static final String EXP = "exp";
	private static final String PUB = "pub";
	private static final String SCP = "scp";
	private static final String REFRESH_TOKEN = "refreshToken";
	private static final String SIGNATUE = "signature";
	private static final String SIGNER_DATA = "signerData";
	private static final String SIGNER_DATA_TYPE = "signerDataType";
	
	public JwsTokenDb map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new JwsTokenDb(resultSet.getLong(JTI), resultSet.getString(ALG), resultSet.getString(TYPE),
				resultSet.getString(ISS), resultSet.getString(SUB), resultSet.getString(VER), resultSet.getTimestamp(EXP),
				resultSet.getString(PUB),resultSet.getString(SCP), resultSet.getString(REFRESH_TOKEN), resultSet.getString(SIGNATUE),
				resultSet.getString(SIGNER_DATA), resultSet.getInt(SIGNER_DATA_TYPE));
	}
}
