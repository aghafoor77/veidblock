package org.acreo.ip.database;

import java.util.List;

import org.acreo.ip.entities.Resource;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;


@RegisterMapper(ResourceMapper.class)
public interface ResourceDao {
	
  @SqlQuery("select * from Resource;")
  public List<Resource> getResources();

  @SqlQuery("select * from Resource where rtype = :rtype;")
  public List<Resource> getResources(@Bind("rtype") final int rtype);
  
  @SqlQuery("select * from Resource where resourceId = :resourceId AND rtype = :rtype")
  public Resource getResource(@Bind("resourceId") final long resourceId, @Bind("rtype") final int rtype);

  @SqlQuery("select * from Resource where username = :username AND rtype = :rtype")
  public Resource getResource(@Bind("username") final String username, @Bind("rtype") final int rtype );

  @SqlQuery("select * from Resource where username = :username ")
  public Resource getResource(@Bind("username") final String username);
  
  @SqlQuery("select * from Resource where resourceId = :resourceId")
  public Resource getResource(@Bind("resourceId") final long resourceId);
  
  @SqlQuery("select * from Resource where createdBy = :createdBy")
  public  List<Resource>  getMyRegisteredResource(@Bind("createdBy") final long createdBy);
  
  
  @SqlUpdate("insert into "
  		+ "Resource (username, backupEmail, creationDate, email, activeStatus, mobile, name, password, resourceId, phone, organizationId, address1, address2,postCode, city, state, country,location, rtype,url,createdBy) "
  		+ "values(:username, :backupEmail, :creationDate, :email, :activeStatus, :mobile, :name, :password, :resourceId, :phone, :organizationId, :address1, :address2, :postCode, :city, :state, :country,:location,:rtype,:url,:createdBy)")
  void createResource(@BindBean final Resource resource);
  
  @SqlUpdate("update Resource set "
  		+ "username = coalesce(:username, username), "
  		+ "backupEmail = coalesce(:backupEmail, backupEmail), "
  		+ "creationDate = coalesce(:creationDate, creationDate), "
  		+ "email = coalesce(:email, email), "
  		+ "activeStatus = coalesce(:activeStatus, activeStatus), "
  		+ "mobile = coalesce(:mobile, mobile), "
  		+ "name = coalesce(:name, name), "
  		+ "password = coalesce(:password, password), "
  		+ "resourceId = coalesce(:resourceId, resourceId), "
  		+ "phone = coalesce(:phone, phone), "
  		+ "organizationId = coalesce(:organizationId, organizationId), "
  		+ "address1 = coalesce(:address1, address1), "
  		+ "address2 = coalesce(:address2, address2), "
  		+ "postCode = coalesce(:postCode, postCode), "
  		+ "city = coalesce(:city, city), "
  		+ "state = coalesce(:state, state), "
  		+ "country = coalesce(:country, country), "
  		+ "location = coalesce(:location, location), "
  		+ "url = coalesce(:url, url) "  		
  		+ "where username = :username")
  void editResource(@BindBean final Resource resource);

  
  @SqlUpdate("update Resource set "
	  		+ "activeStatus = coalesce(:activeStatus, activeStatus) "
	  		+ "where resourceId = :resourceId")
	  void activate(@Bind("resourceId") final long resourceId, @Bind("activeStatus") final boolean activeStatus);
  
  
  @SqlUpdate("delete from Resource where username = :username")
  int deleteResource(@Bind("username") final String username);
  
  @SqlQuery("select last_insert_id();")
  public String lastInsertId();
}