package org.acreo.ipv.resources;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acreo.common.entities.RoleCO;
import org.acreo.common.entities.RoleCOList;
import org.acreo.ip.entities.Role;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ip.service.ResourceService;
import org.acreo.ip.service.RoleService;
import org.acreo.ipv.utils.EnforceAuthorization;
import org.acreo.ipv.utils.IpvDelegate;
import org.acreo.ipv.utils.IpvUtils;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;

@Api
@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {
	private final RoleService roleService;
	private IpvUtils ipvUtils = new IpvUtils();

	private Role role = new Role();

	private EnforceAuthorization enforceAuthorization;
	
	public RoleResource(RoleService roleService) {
		this.roleService = roleService;
		enforceAuthorization =new EnforceAuthorization(roleService);
		
	}

	@GET
	@Timed
	@RolesAllowed({ "" })
	public RoleCOList getRoles() {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		return ipvUtils.toRoleCOList(roleService.getRoles());
				
	}

	@GET
	@Timed
	@Path("{id}")
	@RolesAllowed({ "" })
	public RoleCO getRole(@PathParam("id") final long id) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		return ipvUtils.toRoleCO(roleService.getRole(id));		
	}

	@POST
	@Timed
	@RolesAllowed({ "" })
	public RoleCO createRole(@NotNull @Valid final RoleCO roleCO) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		Role r = roleService.getRole(roleCO.getRole());
		if (r != null) {
			return ipvUtils.toRoleCO(r); 
		}
		Role newRole = ipvUtils.toRole(roleCO);
		newRole.setCreatedBy(adminId);
		return ipvUtils.toRoleCO(roleService.createRole(newRole));		
	}

	@PUT
	@Timed
	@Path("{id}")
	@RolesAllowed({ "" })
	public RoleCO editRole(@NotNull @Valid final RoleCO roleCO, @PathParam("id") final long id) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		roleCO.setRoleId(id);
		return ipvUtils.toRoleCO(roleService.editRole(ipvUtils.toRole(roleCO)));
	}

	@DELETE
	@Timed
	@Path("{id}")
	@RolesAllowed({ "" })
	public RoleCO deleteRole(@PathParam("id") final long id) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		// Check role is assigned to any one 
		if(roleService.isRoleAssigned(id)){
			throw new WebApplicationException("Role already assigned so cannot delete !", Response.status(Status.CONFLICT).build());
		}
		
		Role role = new Role();
		roleService.deleteRole(id);
		role.setRoleId(id);
		return ipvUtils.toRoleCO(role);
		
	}

	// Role Assignment
	@GET
	@Timed
	@Path("personid/{personId}")
	@RolesAllowed({ "" })
	public RoleCOList getRoles(@PathParam("personId") final Long personId) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if((enforceAuthorization.isUser(""+personId) && adminId.equals(personId+"")) || enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId)){
			return ipvUtils.toRoleCOList(roleService.getRoles(personId));			
		}
		throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		
		
	}

	@POST
	@Timed
	@Path("resourceid/{resourceId}/role/{role}")
	@RolesAllowed({ "" })
	public RoleCO assignRole(@PathParam("resourceId") final Long resourceId,
			@PathParam("role") final String role) {
		
		System.out.println(""+headers.getHeaderString("RID"));
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if(!(enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId))){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());	
		}
		
		Role newRole = roleService.assignRole(resourceId, role);
		if(newRole == null){
			throw new WebApplicationException(String.format("Role '%s' not found " , role), Status.NOT_FOUND);
		}
		return ipvUtils.toRoleCO(newRole);
	}

	@DELETE
	@Timed
	@Path("resourceid/{resourceId}/role/{role}")
	@RolesAllowed({ "" })
	public RoleCOList deleteResourceRole(@PathParam("resourceId") final Long resourceId,
			@PathParam("role") final String role) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if((enforceAuthorization.isUser(""+resourceId) && adminId.equals(resourceId+"")) || enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId)){
			List<Role> roles = roleService.deleteResourceRole(resourceId, role);
			return ipvUtils.toRoleCOList(roles);			
		}
		throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
	}
	
	@DELETE
	@Timed
	@Path("resourceid/{resourceId}")
	@RolesAllowed({ "" })
	public RoleCOList deleteResourceAllRoles(@PathParam("resourceId") final Long resourceId) {
		
		String adminId = headers.getHeaderString("RID");
		if (adminId == null ){
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if((enforceAuthorization.isUser(""+resourceId) && adminId.equals(resourceId+"")) || enforceAuthorization.isAdmin(adminId) || enforceAuthorization.isSupper(adminId)){
			List<Role> roles = roleService.deleteResourceAllRoles(resourceId);
			return ipvUtils.toRoleCOList(roles);			
		}
		throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
	}
	
	
	
	@Context
	protected HttpServletResponse response;
	@Context
	protected HttpHeaders headers;

	private void addAuthorizationHeader() {
		response.setHeader(HttpHeaders.AUTHORIZATION, headers.getHeaderString(HttpHeaders.AUTHORIZATION));
	}
}
