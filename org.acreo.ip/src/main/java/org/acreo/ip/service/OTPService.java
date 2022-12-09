package org.acreo.ip.service;

import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acreo.ip.database.OTPDao;
import org.acreo.ip.entities.OTP;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

public abstract class OTPService {
	private static final String OTP_NOT_FOUND = "OTP id %s not found.";
	private static final String DATABASE_REACH_ERROR = "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
	private static final String DATABASE_CONNECTION_ERROR = "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
	private static final String DATABASE_UNEXPECTED_ERROR = "Unexpected error occurred while attempting to reach the database. Details: ";
	private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting OTP.";

	@CreateSqlObject
	abstract OTPDao OTPDao();

	public List<OTP> getAllOTPs() {
		return OTPDao().getAllOTPs();
	}

	public List<OTP> getAllOTPsByResource(final String resourceId) {
		return OTPDao().getAllOTPsByResource(resourceId);
	}

	public OTP getOTPStatus(final String resourceId, final String otp, final int expStatus) {
		return OTPDao().getOTPStatus(resourceId, otp, expStatus);
	}

	public void createOTP(final OTP otp) {
		OTPDao().createOTP(otp);
	}

	public void editOTP(final OTP otp) {
		OTPDao().editOTP(otp);
	}

	public int deleteResource(final String resourceId, final int expStatus) {
		int result = OTPDao().deleteResource(resourceId, expStatus);
		switch (result) {
		case 1:
			return result;
		case 0:
			throw new WebApplicationException(String.format(OTP_NOT_FOUND, resourceId), Status.NOT_FOUND);
		default:
			throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
		}
	}

	public String lastInsertId() {
		return OTPDao().lastInsertId();
	}

	public String performHealthCheck() {
		try {
			OTPDao().getAllOTPs();
		} catch (UnableToObtainConnectionException ex) {
			return checkUnableToObtainConnectionException(ex);
		} catch (UnableToExecuteStatementException ex) {
			return checkUnableToExecuteStatementException(ex);
		} catch (Exception ex) {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
		return null;
	}

	private String checkUnableToObtainConnectionException(UnableToObtainConnectionException ex) {
		if (ex.getCause() instanceof java.sql.SQLNonTransientConnectionException) {
			return DATABASE_REACH_ERROR + ex.getCause().getLocalizedMessage();
		} else if (ex.getCause() instanceof java.sql.SQLException) {
			return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
		} else {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
	}

	private String checkUnableToExecuteStatementException(UnableToExecuteStatementException ex) {
		if (ex.getCause() instanceof java.sql.SQLSyntaxErrorException) {
			return DATABASE_CONNECTION_ERROR + ex.getCause().getLocalizedMessage();
		} else {
			return DATABASE_UNEXPECTED_ERROR + ex.getCause().getLocalizedMessage();
		}
	}
}
