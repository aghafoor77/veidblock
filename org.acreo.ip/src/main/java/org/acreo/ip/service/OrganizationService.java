package org.acreo.ip.service;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acreo.common.utils.IpUtils;
import org.acreo.ip.database.OrganizationDao;
import org.acreo.ip.entities.Organization;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;

public abstract class OrganizationService {
	private static final String ORGANIZATION_NOT_FOUND = "Organization id %s not found.";
	private static final String ORGANIZATION_ALREADY_EXISTS = "Organization name '%s' already exisits .";
	private static final String DATABASE_REACH_ERROR = "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
	private static final String DATABASE_CONNECTION_ERROR = "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
	private static final String DATABASE_UNEXPECTED_ERROR = "Unexpected error occurred while attempting to reach the database. Details: ";
	private static final String SUCCESS = "Success...";
	private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting organization.";

	@CreateSqlObject
	abstract OrganizationDao organizationDao();

	public List<Organization> getOrganizations() {
		return organizationDao().getOrganizations();
	}

	public Organization getOrganization(long id) {
		Organization organization = organizationDao().getOrganization(id);
		if (Objects.isNull(organization)) {
			throw new WebApplicationException(String.format(ORGANIZATION_NOT_FOUND, id), Status.NOT_FOUND);
		}
		return organization;
	}

	public Organization createOrganization(Organization organization) {

		organizationDao().createOrganization(organization);
		return organizationDao().getOrganization(organization.getOrganizationId());
	}

	public Organization editOrganization(Organization organization) {
		organizationDao().editOrganization(organization);
		return organizationDao().getOrganization(organization.getOrganizationId());
	}

	public long deleteOrganization(final long id) {
		return organizationDao().deleteOrganization(id);
	/*	switch (result) {
		case 1:
			return id;
		case 0:
			throw new WebApplicationException(String.format(ORGANIZATION_NOT_FOUND, id), Status.NOT_FOUND);
		default:
			throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
		}*/
	}

	// Customized functions
	public Organization getOrganization(Organization organization) {
		if (Objects.isNull(organization)) {
			return null;
		}
		return organizationDao().getOrganization(organization.getCity(), organization.getCountry(),
				organization.getName(), organization.getState(), organization.getStreet(),
				organization.getOrganizationUnit());

	}

	public String performHealthCheck() {
		try {
			organizationDao().getOrganizations();
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