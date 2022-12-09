package org.acreo.ip.service;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acreo.ip.database.JWSTokenDao;
import org.acreo.ip.entities.JwsTokenDb;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;


public abstract class JWSTokenService {
  private static final String JWS_TOKEN_NOT_FOUND = "JWSToken id %s not found.";
  private static final String JWS_TOKEN_ALREADY_EXISTS = "JWSToken '%s' already exisits .";
  private static final String DATABASE_REACH_ERROR =
      "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
  private static final String DATABASE_CONNECTION_ERROR =
      "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
  private static final String DATABASE_UNEXPECTED_ERROR =
      "Unexpected error occurred while attempting to reach the database. Details: ";
  private static final String SUCCESS = "Success...";
  private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting JWSToken.";

  @CreateSqlObject
  abstract JWSTokenDao jwsTokenDao();

  public List<JwsTokenDb> getJwsTokens() {
    return jwsTokenDao().getJwsTokens();
  }

  public JwsTokenDb getJwsToken(long jti) {
    JwsTokenDb jwsTokenDb = jwsTokenDao().getJwsToken(jti);
    if (Objects.isNull(jwsTokenDb)) {
      throw new WebApplicationException(String.format(JWS_TOKEN_NOT_FOUND, jti), Status.NOT_FOUND);
    }
    return jwsTokenDb;
  }

  public JwsTokenDb createJwsToken(JwsTokenDb jwsTokenDb) {
	  jwsTokenDao().createJwsToken(jwsTokenDb);
	  return jwsTokenDb;
    
  }

  
  public JwsTokenDb isValidTokenAlreadyExisits(final String sub) {
	  return jwsTokenDao().getJwsToken(sub, new Date());
  }
  
  public String deleteJwsToken(final long jti) {
    int result = jwsTokenDao().deleteJwsToken(jti);
    switch (result) {
      case 1:
        return SUCCESS;
      case 0:
        throw new WebApplicationException(String.format(JWS_TOKEN_NOT_FOUND, jti), Status.NOT_FOUND);
      default:
        throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
    }
  }

  
 public String performHealthCheck() {
    try {
    	jwsTokenDao().getJwsTokens();
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