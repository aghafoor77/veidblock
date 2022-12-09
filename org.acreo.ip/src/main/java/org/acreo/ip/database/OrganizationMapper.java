package org.acreo.ip.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.acreo.ip.entities.Organization;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class OrganizationMapper implements ResultSetMapper<Organization> {
	private static final String ORGANIZATION_ID = "organizationID";
	private static final String NAME = "name";
	private static final String ORGANIZATION_UNIT = "organizationUnit";
	private static final String STREET = "street";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String COUNTRY = "country";

	public Organization map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Organization(resultSet.getLong(ORGANIZATION_ID), resultSet.getString(NAME), resultSet.getString(ORGANIZATION_UNIT),
				resultSet.getString(STREET), resultSet.getString(CITY),resultSet.getString(STATE),resultSet.getString(COUNTRY));
	}
}