package org.acreo.ip.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.acreo.ip.entities.Organization;
import org.acreo.ip.entities.Resource;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class ResourceMapper implements ResultSetMapper<Resource> {

	private static final String USERNAME = "username";
	private static final String BACKUP_EMAIL = "backupEmail";
	private static final String CREATION_DATE = "creationDate";
	private static final String EMAIL = "email";
	private static final String IS_ACTIVE_STATUS = "activeStatus";
	private static final String MOBILE = "mobile";
	private static final String NAME = "name";
	private static final String PASSWORD = "password";
	private static final String RESOURCE_ID = "resourceId";
	private static final String PHONE = "phone";
	private static final String ORGANIZATION_ID = "organizationID";
	private static final String RTYPE = "rtype";
	private static final String URL = "url";
	private static final String CREATED_BY = "createdBy";
	private static final String ADDRESS1 = "address1";
	private static final String ADDRESS2 = "address2";
	private static final String POSTCODE = "postCode";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String COUNTRY = "country";
	private static final String LOCATION = "location";

	public Resource map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

		return new Resource(resultSet.getString(USERNAME), resultSet.getString(BACKUP_EMAIL),
				resultSet.getDate(CREATION_DATE), resultSet.getString(EMAIL), resultSet.getBoolean(IS_ACTIVE_STATUS),
				resultSet.getString(MOBILE), resultSet.getString(NAME), resultSet.getString(PASSWORD),
				resultSet.getLong(RESOURCE_ID), resultSet.getString(PHONE), resultSet.getLong(ORGANIZATION_ID),
				resultSet.getString(ADDRESS1), resultSet.getString(ADDRESS2), resultSet.getString(POSTCODE),
				resultSet.getString(CITY), resultSet.getString(STATE), resultSet.getString(COUNTRY),
				resultSet.getString(LOCATION), resultSet.getString(RTYPE), resultSet.getString(URL),
				resultSet.getLong(CREATED_BY));
	}
}