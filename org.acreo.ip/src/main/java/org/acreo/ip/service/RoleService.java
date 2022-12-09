package org.acreo.ip.service;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.acreo.common.Representation;
import org.acreo.common.utils.IpUtils;
import org.acreo.ip.database.RoleDao;
import org.acreo.ip.entities.RoleAssignment;
import org.acreo.ip.entities.Role;
import org.eclipse.jetty.http.HttpStatus;
import org.skife.jdbi.v2.exceptions.UnableToExecuteStatementException;
import org.skife.jdbi.v2.exceptions.UnableToObtainConnectionException;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.CreateSqlObject;


public abstract class RoleService {
  private static final String ROLE_NOT_FOUND = "Role id %s not found.";
  private static final String DATABASE_REACH_ERROR =
      "Could not reach the MySQL database. The database may be down or there may be network connectivity issues. Details: ";
  private static final String DATABASE_CONNECTION_ERROR =
      "Could not create a connection to the MySQL database. The database configurations are likely incorrect. Details: ";
  private static final String DATABASE_UNEXPECTED_ERROR =
      "Unexpected error occurred while attempting to reach the database. Details: ";
  private static final String SUCCESS = "Success...";
  private static final String UNEXPECTED_ERROR = "An unexpected error occurred while deleting role.";
  

  @CreateSqlObject
  abstract RoleDao roleDao();

  public List<Role> getRoles() {
    return roleDao().getRoles();
  }

  public boolean isRoleAssigned(final long roleId){
	  List<Role> tem = roleDao().isRoleAssigned(roleId);
	  if(Objects.isNull(tem)){
		  return false;
	  }
	  if(tem.size() == 0){
		  return false;
	  }
	  return true;
  }
  
  
  
  public List<Role> getRoles(Long resourceId) {
	 // List<Role> roles = roleDao().getRoles(resourceId);
	  return roleDao().getRoles(resourceId);
	  }
  //Select r.roleId,r.description,r.role from Role r, PersonRole pr where pr.username='idmsuser'
  public Role getRole(long id) {
    Role role = roleDao().getRole(id);
    if (Objects.isNull(role)) {
      throw new WebApplicationException(String.format(ROLE_NOT_FOUND , id), Status.NOT_FOUND);
    }
    return role;
  }

  public Role createRole(Role role) {
	  role.setRoleId(new IpUtils().generateId());
	  roleDao().createRole(role);
    return roleDao().getRole(role.getRoleId());
  }

  public Role editRole(Role role) {
    if (Objects.isNull(roleDao().getRole(role.getRoleId()))) {
      throw new WebApplicationException(String.format(ROLE_NOT_FOUND, role.getRoleId()),
          Status.NOT_FOUND);
    }
    roleDao().editRole(role);
    return roleDao().getRole(role.getRoleId());
  }

  public long deleteRole(final long id) {
    int result = roleDao().deleteRole(id);
    switch (result) {
      case 1:
        return id;
      case 0:
        throw new WebApplicationException(String.format(ROLE_NOT_FOUND , id), Status.NOT_FOUND);
      default:
        throw new WebApplicationException(UNEXPECTED_ERROR, Status.INTERNAL_SERVER_ERROR);
    }
  }
  
  // Role Assignment
  public Role getRole(final String role){
	  return roleDao().getRole(role);
  }
  
  public Role assignRole(final Long resourceId, final String role){
	  List<Role> roles = getRoles(resourceId);
	  for (Role roleRet: roles ){
		  if(roleRet.getRole().equalsIgnoreCase(role)){
			  return null;
		  }
	  }
	  Role r = getRole(role);
	  RoleAssignment personRole = new RoleAssignment ();
	  personRole.setResourceId(resourceId); 
	  personRole.setRoleId(r.getRoleId());
	  roleDao().assignRole(personRole);
	  return r;
  }
  
  public List<Role> deleteResourceRole(final long resourceId, final String role){
	  
	  Role roleRet = getRole(role);
	  if(roleRet== null){
		  throw new WebApplicationException(String.format(ROLE_NOT_FOUND , role), Status.NOT_FOUND);  
	  }
	  
	  roleDao().deletePersonRole(resourceId, roleRet.getRoleId());
	  
	  List<Role> roles = getRoles(resourceId);
	  
	  return roles;	  
  }
  
 public List<Role> deleteResourceAllRoles(final long resourceId){
	  
	  roleDao().deletePersonAllRoles(resourceId);
	  
	  List<Role> roles = getRoles(resourceId);
	  
	  return roles;	  
  }
  
  
  
  public String performHealthCheck() {
    try {
    	roleDao().getRoles();
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
