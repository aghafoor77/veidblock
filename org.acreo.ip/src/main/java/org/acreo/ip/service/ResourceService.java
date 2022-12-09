package org.acreo.ip.service;

import java.util.List;
import java.util.Objects;

import org.acreo.ip.database.ResourceDao;
import org.acreo.ip.entities.Resource;
import org.acreo.security.crypto.PwdBasedEncryption;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

public abstract class ResourceService {
	private static final String RESOURCE_NOT_FOUND = "Resource id %s not found.";
	private static final String DATABASE_REACH_ERROR = "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
	private static final String DATABASE_CONNECTION_ERROR = "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
	private static final String DATABASE_UNEXPECTED_ERROR = "Unexpected error occurred while attempting to reach the database. Details: ";
	private static final String SUCCESS = "Success...";
	private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting resource.";

	@CreateSqlObject
	abstract ResourceDao resourceDao();

	public List<Resource> getResources() {
		return resourceDao().getResources();
	}

	public List<Resource> getResources(int rtype) {
		return resourceDao().getResources(rtype);
	}

	public Resource getResource(long id, int rtype) {
		return resourceDao().getResource(id, rtype);
	}

	public Resource getResource(long id) {
		return resourceDao().getResource(id);
	}
	
	public  List<Resource>  getMyRegisteredResource(final Long identity){
		return resourceDao().getMyRegisteredResource(identity);
	}
	
	public Resource getResource(String username) {
		return resourceDao().getResource(username);
	}
	
	
	public Resource getResource(String username, int rtype) {
		return resourceDao().getResource(username, rtype);
	}

	public Resource createResource(Resource resource) {
		resourceDao().createResource(resource);		
		return resourceDao().getResource(resource.getUsername(),Integer.parseInt(resource.getRtype()));
	}

	public Resource editResource(Resource resource) {
		if (Objects.isNull(resourceDao().getResource(resource.getUsername(),Integer.parseInt(resource.getRtype())))) {
			return null;
		}
		resourceDao().editResource(resource);
		return resourceDao().getResource(resource.getResourceId(),Integer.parseInt(resource.getRtype()));
	}

	  public void activate(final long resourceId, final boolean activeStatus){
		  resourceDao().activate(resourceId, activeStatus);
	  }
	
	public String deleteResource(final String username) {
		int result = resourceDao().deleteResource(username);
		switch (result) {
		case 1:
			return SUCCESS;		
		default:
			return null;
		}
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
}