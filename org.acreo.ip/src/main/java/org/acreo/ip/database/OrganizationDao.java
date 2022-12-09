package org.acreo.ip.database;

import java.util.List;

import org.acreo.ip.entities.Organization;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(OrganizationMapper.class)
public interface OrganizationDao {
	
  @SqlQuery("select * from Organization;")
  public List<Organization> getOrganizations();

  @SqlQuery("select * from Organization where organizationId = :organizationId")
  public Organization getOrganization(@Bind("organizationId") final long organizationId);

  @SqlUpdate("insert into Organization(organizationId, name, organizationUnit, street, city, state, country) values(:organizationId, :name, :organizationUnit, :street, :city, :state, :country)")
  void createOrganization(@BindBean final Organization organization);

  @SqlUpdate("update Organization set name = coalesce(:name, name), organizationUnit = coalesce(:organizationUnit, organizationUnit), street = coalesce(:street, street), city = coalesce(:city, city), state = coalesce(:state, state), country = coalesce(:country, country) where organizationId = :organizationId")
  void editOrganization(@BindBean final Organization organization);

  @SqlUpdate("delete from Organization where organizationId = :organizationId")
  int deleteOrganization(@Bind("organizationId") final long organizationId);
  
  @SqlQuery("select last_insert_id();")
  public int lastInsertId();
  
  @SqlQuery("select * from Organization where city = :city AND country = :country AND name = :name AND state = :state AND street = :street AND organizationUnit = :organizationUnit")
  public Organization getOrganization(
		  @Bind("city")final String city, 
		  @Bind("country")final String country,
		  @Bind("name") final String name,
		  @Bind("state") final String state,
		  @Bind("street") final String street,
		  @Bind("organizationUnit")  final String organizationUnit);
}