package org.acreo.ipv.resources;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.acreo.common.entities.OrganizationCO;
import org.acreo.common.entities.OrganizationCOList;
import org.acreo.ip.entities.Organization;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ipv.utils.IpvUtils;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;

@Api
@Path("/organization")
@Produces(MediaType.APPLICATION_JSON)
public class OrganizationResource {
	private final OrganizationService organizationService;;

	public OrganizationResource(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@GET
	@Timed
	public OrganizationCOList getOrganizations() {
		
		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toOrganizationCOList(organizationService.getOrganizations());
	}

	@GET
	@Timed
	@Path("{id}")
	public OrganizationCO getOrganization(@PathParam("id") final long id) {

		IpvUtils ipvUtils = new IpvUtils();
		return ipvUtils.toOrganizationCO(organizationService.getOrganization(id));
	}

	@POST
	@Timed
	public OrganizationCO createOrganization(@NotNull @Valid final OrganizationCO orgCO) {

		IpvUtils ipvUtils = new IpvUtils();
		Organization org = ipvUtils.toOrganization(orgCO);
		return ipvUtils.toOrganizationCO(organizationService.createOrganization(org));

	}

	@PUT
	@Timed
	@Path("{id}")
	public OrganizationCO editOrganization(@NotNull @Valid final OrganizationCO organizationCO,
			@PathParam("id") final long id) {
		
		organizationCO.setOrganizationId(id);
		IpvUtils ipvUtils = new IpvUtils();
		Organization org = ipvUtils.toOrganization(organizationCO);
		return ipvUtils.toOrganizationCO(organizationService.editOrganization(org));
	}

	@DELETE
	@Timed
	@Path("{id}")
	public OrganizationCO deleteOrganization(@PathParam("id") final long id) {
		
		OrganizationCO organizationCO = new OrganizationCO();
		organizationCO.setOrganizationId(organizationService.deleteOrganization(id));

		return organizationCO;
	}
}
