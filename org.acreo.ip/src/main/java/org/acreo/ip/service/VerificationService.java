package org.acreo.ip.service;

import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acreo.common.exceptions.VeidblockException;
import org.acreo.ip.database.ResourceDao;
import org.acreo.ip.entities.Resource;
import org.acreo.security.crypto.CryptoPolicy;
import org.acreo.security.crypto.PwdBasedEncryption;
import org.eclipse.jetty.http.HttpStatus;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

public abstract class VerificationService {

	private static final String DATABASE_REACH_ERROR = "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
	private static final String DATABASE_CONNECTION_ERROR = "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
	private static final String DATABASE_UNEXPECTED_ERROR = "Unexpected error occurred while attempting to reach the database. Details: ";
	private static final String UNEXPECTED_ERROR = "An unexpected error occurred while %s.";

	private CryptoPolicy cryptoPolicy;
	private String password;

	@CreateSqlObject
	abstract ResourceDao resourceDao();

	public Resource verifyPassword(String username, String password) {

		Resource resource = resourceDao().getResource(username);
		if (Objects.isNull(resource)) {
			throw new WebApplicationException(String.format("'%s' not registered !", username), Status.NOT_FOUND);
		}
		
		try {
			resource = decryptPassowrd(resource);
		} catch (VeidblockException e) {
			throw new WebApplicationException(String.format("Internal error [Crypto Problems] !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		
		if (resource.getPassword().equals(password)) {
			return resource;
		} else {
			throw new WebApplicationException(String.format("Valid credentials are required to access resource !"),
					Status.UNAUTHORIZED);
		}

	}

	public Resource verifyPassword(Long resourceId, String password) {

		Resource resource = resourceDao().getResource(resourceId);
		if (Objects.isNull(resource)) {
			throw new WebApplicationException(String.format("'%s' not registered !", resourceId), Status.NOT_FOUND);
		}
		
		try {
			resource = decryptPassowrd(resource);
		} catch (VeidblockException e) {
			throw new WebApplicationException(String.format("Internal error [Crypto Problems] !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		
		if (resource.getPassword().equals(password)) {
			return resource;
		} else {
			throw new WebApplicationException(String.format("Valid credentials are required to access resource !"),
					Status.UNAUTHORIZED);
		}
	}

	public Resource getResourceWithPassword(String username) {

		Resource resource = resourceDao().getResource(username);
		if (Objects.isNull(resource)) {
			throw new WebApplicationException(String.format("'%s' not registered !", username), Status.NOT_FOUND);
		}
		
		try {
			resource = decryptPassowrd(resource);
		} catch (VeidblockException e) {
			throw new WebApplicationException(String.format("Internal error [Crypto Problems] !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		
		return resource;
	}

	public Resource getResourceWithPassword(Long resourceId) {

		Resource resource = resourceDao().getResource(resourceId);
		if (Objects.isNull(resource)) {
			throw new WebApplicationException(String.format("'%s' not registered !", resourceId), Status.NOT_FOUND);
		}
		
		try {
			resource = decryptPassowrd(resource);
		} catch (VeidblockException e) {
			throw new WebApplicationException(String.format("Internal error [Crypto Problems] !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		
		return resource;
	}

	public String performHealthCheck() {
		try {
			resourceDao().getResources();
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

	private Resource decryptPassowrd(Resource resource) throws VeidblockException {

		PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
		resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
		return resource;
	}
}
