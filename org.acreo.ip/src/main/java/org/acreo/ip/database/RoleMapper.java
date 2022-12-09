package org.acreo.ip.database;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.acreo.ip.entities.Organization;
import org.acreo.ip.entities.Role;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class RoleMapper implements ResultSetMapper<Role> {
	private static final String ROLE_ID = "roleId";	
	private static final String ROLE = "role";
	private static final String DESCRIPTION = "description";
	private static final String CREATED_BY = "createdBy";
	
	

	public Role map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
		return new Role(resultSet.getLong(ROLE_ID), resultSet.getString(DESCRIPTION), resultSet.getString(ROLE),resultSet.getString(CREATED_BY));
	}
}
