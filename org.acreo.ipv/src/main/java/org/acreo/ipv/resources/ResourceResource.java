package org.acreo.ipv.resources;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
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

import org.acreo.common.entities.ResourceCO;
import org.acreo.common.entities.ResourceCOList;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.IpUtils;
import org.acreo.ip.entities.Resource;
import org.acreo.ip.service.OTPService;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ip.service.ResourceService;
import org.acreo.ip.service.RoleService;
import org.acreo.ipv.IpvApplication;
import org.acreo.ipv.utils.EnforceAuthorization;
import org.acreo.ipv.utils.IpvDelegate;
import org.acreo.ipv.utils.IpvUtils;
import org.apache.http.entity.StringEntity;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;

@Api
@Path("/resource")
@Produces(MediaType.APPLICATION_JSON)
public class ResourceResource {

	private IpvDelegate ipvDelegate = null;

	private EnforceAuthorization enforceAuthorization;
	private RoleService roleService;
	public ResourceResource(ResourceService resourceService, OrganizationService organizationService,
			RoleService roleService, OTPService otpService) {
		ipvDelegate = new IpvDelegate(resourceService, organizationService, roleService, otpService);
		enforceAuthorization =new EnforceAuthorization(roleService);
		this.roleService = roleService;
	}

	@GET
	@Path("test")
	@Timed
	@RolesAllowed({ "" })
	public DataRes getTest() {
		String adminId = headers.getHeaderString("RID");
		System.out.println(adminId);
		List<String> li = new ArrayList<String>();
		li.add("First");
		li.add("Second");
		return new DataRes(li);
	}
	
	@GET
	@Path("countries")
	public DataRes getCountries() {
		return new DataRes(IpvApplication.rl);
	}
	
	@GET
	@Timed
	@RolesAllowed({ "" })
	public ResourceCOList getResources() {
		String adminId = headers.getHeaderString("RID");
		if (adminId != null && (!enforceAuthorization.isUser(adminId))) {
			IpvUtils ipvUtils = new IpvUtils();
			ResourceCOList resourceDTOs = ipvUtils.resourceToResourceDTO(ipvDelegate.getResources());
			addAuthorizationHeader();
			return resourceDTOs;
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}

	// =========================== Resource As A User ==========================
	@GET
	@Timed
	@Path("user")
	@RolesAllowed({ "" })
	public ResourceCOList getUserResources() {
		String adminId = headers.getHeaderString("RID");
		if (adminId != null && (!enforceAuthorization.isUser(adminId))) {
			IpvUtils ipvUtils = new IpvUtils();
			ResourceCOList  resourceCOList  = ipvUtils.resourceToResourceDTO(ipvDelegate.getResources(0)); 
			return resourceCOList;
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}
	
	
	

	@POST
	@Timed
	@Path("user")
	@RolesAllowed({ "" })
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ResourceCO createUserResource(@NotNull @Valid final ResourceCO resourceDto) {
		String adminId = headers.getHeaderString("RID");
		if (adminId == null) {
			boolean isAuthenticated = false;
			IpvUtils ipvUtils = new IpvUtils();
			Resource resource = ipvUtils.toResource(resourceDto);
			resource.setRtype("0");
			IpUtils ipUtils = new IpUtils();
			resource.setResourceId(ipUtils.generateId());
			resource.setCreatedBy(resource.getResourceId());
			resource.setCreationDate(new Date());
			return ipvUtils.toResourceDTO(ipvDelegate.createResource(resource, isAuthenticated));
		} else {
			if ((!enforceAuthorization.isUser(adminId))) {
				boolean isAuthenticated = true;
				IpvUtils ipvUtils = new IpvUtils();
				Resource resource = ipvUtils.toResource(resourceDto);
				resource.setRtype("0");
				IpUtils ipUtils = new IpUtils();
				resource.setResourceId(ipUtils.generateId());
				resource.setCreatedBy(Long.parseLong(adminId));
				resource.setCreationDate(new Date());
				return ipvUtils.toResourceDTO(ipvDelegate.createResource(resource, isAuthenticated));
			} else {
				throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
			}
		}
	}

	@POST
	@Timed
	@Path("user/admin")
	@RolesAllowed({ "" })
	public ResourceCO createUserResourceByAdmin(@NotNull @Valid final ResourceCO resourceDto) {
		String adminId = headers.getHeaderString("RID");
		if (adminId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		} else {
			if ((!enforceAuthorization.isUser(adminId))) {
				boolean isAuthenticated = true;
				IpvUtils ipvUtils = new IpvUtils();
				Resource resource = ipvUtils.toResource(resourceDto);
				resource.setRtype("0");
				resource.setActiveStatus(true);
				IpUtils ipUtils = new IpUtils();
				resource.setResourceId(ipUtils.generateId());
				resource.setCreatedBy(Long.parseLong(adminId));
				resource.setCreationDate(new Date());
				return ipvUtils.toResourceDTO(ipvDelegate.createResource(resource, isAuthenticated));
			} else {
				throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
			}
		}
	}
	
	
	@GET
	@Timed
	@Path("user/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO getUserResource(@PathParam("identity") final String identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		Resource resource = ipvDelegate.getResource(identity, 0);
		if (Objects.isNull(resource)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", identity), Status.NOT_FOUND);
		}
		if ((!enforceAuthorization.isUser(resourceId)) || resourceId.equalsIgnoreCase("" + resource.getResourceId())) {
			IpvUtils ipvUtils = new IpvUtils();
			resource.setPassword("NOT-ALLOWED");
			return ipvUtils.toResourceDTO(resource);
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}

	@GET
	@Timed
	@Path("user/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO getUserResource(@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if ((!enforceAuthorization.isUser(resourceId)) || resourceId.equalsIgnoreCase("" + identity)) {
			Resource resource = ipvDelegate.getResource(identity, 0);
			if (Objects.isNull(resource)) {
				throw new WebApplicationException(String.format("Resource '%s' not found !", identity),
						Status.NOT_FOUND);
			}
			IpvUtils ipvUtils = new IpvUtils();
			resource.setPassword("NOT-ALLOWED");
			return ipvUtils.toResourceDTO(resource);
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}

	
	
	@GET
	@Timed
	@Path("my/resources/{identity}")
//	@RolesAllowed({ "" })
	public ResourceCOList getMyRegisteredResource(@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
	/*	if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}*/
		List<Resource> resources = ipvDelegate.getMyRegisteredResource(identity);
		if (true/*(!enforceAuthorization.isUser(resourceId)) || resourceId.equalsIgnoreCase("" + identity)*/) {
			
			IpvUtils ipvUtils = new IpvUtils();
			ResourceCOList  resourceCOList  = ipvUtils.resourceToResourceDTO(resources); 
			return resourceCOList;
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}
	
	@PUT
	@Timed
	@Path("user/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO editUserResource(@NotNull @Valid final ResourceCO resourceCo,
			@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if (!resourceId.equalsIgnoreCase(""+ identity)) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		Resource resourceRet = ipvDelegate.getResource(identity, 0);
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", identity), Status.NOT_FOUND);
		}
		if (resourceCo.getPassword() != null) {
			try {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new StringEntity("Password cannot be changed in update profile process !")).build());
			} catch (UnsupportedEncodingException e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
			}
		}
		IpvUtils ipvUtils = new IpvUtils();

		Resource resource = ipvUtils.toUpdateResource(resourceCo, resourceRet);
		resource.setRtype("0");
		Resource resourceUpdated = ipvDelegate.editResource(resource, resourceRet.getUsername());
		resourceUpdated.setPassword("NOT-ALLOWED");
		return ipvUtils.toResourceDTO(resourceUpdated);
	}

	@PUT
	@Timed
	@Path("user/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO editUserResource(@NotNull @Valid final ResourceCO resourceCo,
			@PathParam("identity") final String username) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

		Resource resourceRet = ipvDelegate.getResource(username, 0);

		if (!resourceId.equalsIgnoreCase(""+ resourceRet.getResourceId())) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", username), Status.NOT_FOUND);
		}

		if (!resourceCo.getUsername().equals(resourceRet.getUsername())) {
			try {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new StringEntity("Username cannot be changed in update profile process !")).build());
			} catch (UnsupportedEncodingException e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
			}
		}
		if (resourceCo.getPassword() != null) {
			try {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR)
						.entity(new StringEntity("Password cannot be changed in update profile process !")).build());
			} catch (UnsupportedEncodingException e) {
				throw new WebApplicationException(Response.status(Status.INTERNAL_SERVER_ERROR).build());
			}
		}
		IpvUtils ipvUtils = new IpvUtils();

		Resource resource = ipvUtils.toUpdateResource(resourceCo, resourceRet);
		resource.setRtype("0");

		Resource resourceUpdated = ipvDelegate.editResource(resource, resourceRet.getUsername());
		resourceUpdated.setPassword("NOT-ALLOWED");
		return ipvUtils.toResourceDTO(resourceUpdated);
	}

	@DELETE
	@Timed
	@Path("user/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO deleteUserResource(@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if (!(resourceId.equalsIgnoreCase(""+ identity) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		
		Resource resourceRet = ipvDelegate.getResource(identity, 0);
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", identity), Status.NOT_FOUND);
		}
		ipvDelegate.deleteResource(resourceRet.getUsername(), resourceRet.getOrganizationId());
		resourceRet.setPassword("NOT-ALLOWED");
		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toResourceDTO(resourceRet);
	}

	@DELETE
	@Timed
	@Path("user/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO deleteUserResource(@PathParam("identity") final String username) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		Resource resourceRet = ipvDelegate.getResource(username, 0);
		
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", username), Status.NOT_FOUND);
		}
		if (!(resourceId.equalsIgnoreCase(""+ resourceRet.getResourceId()) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		ipvDelegate.deleteResource(resourceRet.getUsername(), resourceRet.getOrganizationId());
		resourceRet.setPassword("NOT-ALLOWED");
		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toResourceDTO(resourceRet);
	}

	// =========================== Resource As A Service
	// ==========================
	@GET
	@Timed
	@Path("server")
	@RolesAllowed({ "" })
	public ResourceCOList getServerResources() {
		String adminId = headers.getHeaderString("RID");
		if (adminId != null && (!enforceAuthorization.isUser(adminId))) {
			IpvUtils ipvUtils = new IpvUtils();
			return ipvUtils.resourceToResourceDTO(ipvDelegate.getResources(1));
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}

	@POST
	@Timed
	@Path("server")
	@RolesAllowed({ "" })
	public ResourceCO createServerResource(@NotNull @Valid final ResourceCO resourceDto) {
		String adminId = headers.getHeaderString("RID");
		if (adminId != null && (!enforceAuthorization.isUser(adminId))) {
			IpvUtils ipvUtils = new IpvUtils();
			Resource resource = ipvUtils.toResource(resourceDto);
			resource.setRtype("1");
			IpUtils ipUtils = new IpUtils();
			resource.setResourceId(ipUtils.generateId());
			resource.setCreatedBy(Long.parseLong(adminId));
			resource.setCreationDate(new Date());

			boolean isAuthenticated = false;
			return ipvUtils.toResourceDTO(ipvDelegate.createResource(resource, isAuthenticated));
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
	}
	
	@GET
	@Timed
	@Path("server/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO getServerResource(@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if ((resourceId != null && (!enforceAuthorization.isUser(resourceId))) || identity.equals(resourceId)) {
			Resource resource = ipvDelegate.getResource(identity, 1);
			if (Objects.isNull(resource)) {
				throw new WebApplicationException(String.format("Resource '%s' not found !", identity),
						Status.NOT_FOUND);
			}
			IpvUtils ipvUtils = new IpvUtils();
			resource.setPassword("NOT-ALLOWED");
			return ipvUtils.toResourceDTO(resource);
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

	}

	@GET
	@Timed
	@Path("server/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO getServerResource(@PathParam("identity") final String identity) {

		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if ((resourceId != null && (!enforceAuthorization.isUser(resourceId))) || identity.equals(resourceId)) {
			Resource resource = ipvDelegate.getResource(identity, 1);
			if (Objects.isNull(resource)) {
				throw new WebApplicationException(String.format("Resource '%s' not found !", identity),
						Status.NOT_FOUND);
			}
			IpvUtils ipvUtils = new IpvUtils();
			resource.setPassword("NOT-ALLOWED");
			return ipvUtils.toResourceDTO(resource);
		} else {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

	}

	@PUT
	@Timed
	@Path("server/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO editServerResource(@NotNull @Valid final ResourceCO resourceCo,
			@PathParam("identity") final Long identity) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if (!(resourceId.equalsIgnoreCase(""+ identity) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

		Resource resourceRet = ipvDelegate.getResource(identity, 1);
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", identity), Status.NOT_FOUND);
		}

		if (resourceCo.getUsername() != null) {

			throw new WebApplicationException(String.format("Username cannot be changed in update profile process !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		if (resourceCo.getPassword() != null) {

			throw new WebApplicationException(String.format("Password cannot be changed in update profile process !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		IpvUtils ipvUtils = new IpvUtils();

		Resource resource = ipvUtils.toUpdateResource(resourceCo, resourceRet);
		resource.setRtype("1");
		Resource resourceUpdated = ipvDelegate.editResource(resource, resourceRet.getUsername());
		resourceUpdated.setPassword("NOT-ALLOWED");
		return ipvUtils.toResourceDTO(resourceUpdated);
	}

	@PUT
	@Timed
	@Path("server/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO editServerResource(@NotNull @Valid final ResourceCO resourceCo,
			@PathParam("identity") final String username) {
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if (!(resourceId.equalsIgnoreCase(""+ username) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}

		Resource resourceRet = ipvDelegate.getResource(username, 1);
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", username), Status.NOT_FOUND);
		}

		if (resourceCo.getUsername() != null) {

			throw new WebApplicationException(String.format("Username cannot be changed in update profile process !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		if (resourceCo.getPassword() != null) {

			throw new WebApplicationException(String.format("Password cannot be changed in update profile process !"),
					Status.INTERNAL_SERVER_ERROR);
		}
		IpvUtils ipvUtils = new IpvUtils();

		Resource resource = ipvUtils.toUpdateResource(resourceCo, resourceRet);
		resource.setRtype("1");
		Resource resourceUpdated = ipvDelegate.editResource(resource, resourceRet.getUsername());
		resourceUpdated.setPassword("NOT-ALLOWED");
		return ipvUtils.toResourceDTO(resourceUpdated);
	}

	@DELETE
	@Timed
	@Path("server/resname/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO deleteServerResource(@PathParam("identity") final String username) {
		
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		Resource resourceRet = ipvDelegate.getResource(username, 1);
		
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", username), Status.NOT_FOUND);
		}
		if (!(resourceId.equalsIgnoreCase(""+ resourceRet.getResourceId()) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		ipvDelegate.deleteResource(resourceRet.getUsername(), resourceRet.getOrganizationId());
		resourceRet.setPassword("NOT-ALLOWED");
		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toResourceDTO(resourceRet);
	}

	@DELETE
	@Timed
	@Path("server/id/{identity}")
	@RolesAllowed({ "" })
	public ResourceCO deleteServerResource(@PathParam("identity") final Long identity) {
		
		String resourceId = headers.getHeaderString("RID");
		if (resourceId == null) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		if (!(resourceId.equalsIgnoreCase(""+ identity) || (!enforceAuthorization.isUser(resourceId)))) {
			throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
		}
		
		Resource resourceRet = ipvDelegate.getResource(identity, 1);
		if (Objects.isNull(resourceRet)) {
			throw new WebApplicationException(String.format("Resource '%s' not found !", identity), Status.NOT_FOUND);
		}
		ipvDelegate.deleteResource(resourceRet.getUsername(), resourceRet.getOrganizationId());
		// Deleting assigned roles 
		roleService.deleteResourceAllRoles(identity);
		resourceRet.setPassword("NOT-ALLOWED");
		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toResourceDTO(resourceRet);
	}
	
	@GET
	@Path("user/act/{actcode}")
	public String activation(@PathParam("actcode") final String actcode) {
		try {
			ipvDelegate.activateAccount(actcode);
		} catch (VeidblockException e) {
			throw new WebApplicationException(e);
		}
		return "Successfully activated !";
	}
	
	
	@Context
	protected HttpServletResponse response;
	@Context
	protected HttpHeaders headers;

	private void addAuthorizationHeader() {
		response.setHeader(HttpHeaders.AUTHORIZATION, headers.getHeaderString(HttpHeaders.AUTHORIZATION));
	}
}
