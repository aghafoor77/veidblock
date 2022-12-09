package org.acreo.ip.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.acreo.ip.entities.OTP;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class OTPMapper implements ResultSetMapper<OTP> {
	
	private static final String otp = "otp";
	private static final String resourceId = "resourceId";
	private static final String sentDate = "sentDate";
	private static final String expStatus = "expStatus";
	
	public OTP map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new OTP(resultSet.getString(otp), resultSet.getString(resourceId), resultSet.getDate(sentDate), resultSet.getInt(expStatus));
	}
}
