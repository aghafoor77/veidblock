package org.acreo.ip.database;

import java.util.List;

import org.acreo.ip.entities.RoleAssignment;
import org.acreo.ip.entities.Role;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(RoleMapper.class)
public interface RoleDao {
	
  @SqlQuery("select * from Role;")
  public List<Role> getRoles();

  @SqlQuery("select * from Role where roleId = :roleId")
  public Role getRole(@Bind("roleId") final Long roleId);
  
  @SqlQuery("select * from Role where role = :role")
  public Role getRole(@Bind("role") final String role);
  

  @SqlUpdate("insert into Role(roleId, role, description, createdBy) values(:roleId, :role, :description, :createdBy)")
  void createRole(@BindBean final Role role);

  @SqlUpdate("update Role set roleId = coalesce(:roleId, roleId), role = coaexit"
  		+ "lesce(:role, role), description = coalesce(:description, description) where roleId = :roleId")
  void editRole(@BindBean final Role role);

  @SqlUpdate("delete from Role where roleId = :roleId")
  int deleteRole(@Bind("roleId") final Long roleId);
  
  @SqlQuery("select last_insert_id();")
  public int lastInsertId();
  // Customized queries
  
  @SqlQuery("select r.roleId, r.description, r.role, r.createdBy from Role r, RoleAssignment pr where pr.resourceId = :resourceId AND pr.roleId = r.roleId")
  public List<Role> getRoles(@Bind("resourceId") final long resourceId);
  
  @SqlQuery("select r.roleId, r.description, r.role, r.createdBy from Role r, RoleAssignment pr where pr.roleId = :roleId")
  public List<Role> isRoleAssigned(@Bind("roleId") final long roleId);
  
  
  @SqlUpdate("insert into RoleAssignment(resourceId, roleId) values(:resourceId, :roleId)")
  public long assignRole(@BindBean final RoleAssignment roleAssignment);
  
  @SqlUpdate("delete from RoleAssignment where resourceId = :resourceId AND roleId = :roleId")
  int deletePersonRole(@Bind("resourceId") final long resourceId, @Bind("roleId") final Long roleId);
    
  @SqlUpdate("delete from RoleAssignment where resourceId = :resourceId")
  int deletePersonAllRoles(@Bind("resourceId") final long resourceId);
  
}