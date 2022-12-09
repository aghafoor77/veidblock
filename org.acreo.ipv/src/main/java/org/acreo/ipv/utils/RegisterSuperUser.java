package org.acreo.ipv.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import org.acreo.common.entities.ResourceCO;
import org.acreo.common.entities.RoleCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.IpUtils;
import org.acreo.init.MyIdentity;
import org.acreo.init.VeidblockIO;
import org.acreo.ip.entities.Resource;
import org.acreo.ip.service.OTPService;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ip.service.ResourceService;
import org.acreo.ip.service.RoleService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RegisterSuperUser {

	private RoleService roleService;
	private IpvDelegate ipvDelegate = null;

	public RegisterSuperUser(ResourceService resourceService, OrganizationService organizationService,
			RoleService roleService, OTPService otpService) {
		this.roleService = roleService;
		ipvDelegate = new IpvDelegate(resourceService, organizationService, roleService, otpService);
	}

	public void createRoles() {

		RoleCO supperRoleCO = new RoleCO();
		supperRoleCO.setRole("Super");
		supperRoleCO.setDescription("This is a Super user and can create Admin and User !");
		if (!Objects.isNull(roleService.getRole(supperRoleCO.getRole()))) {
			return;
		}

		RoleCO adminRoleCO = new RoleCO();
		adminRoleCO.setRole("Admin");
		adminRoleCO.setDescription("This is a Amin user and can create only User !");

		RoleCO userRoleCO = new RoleCO();
		userRoleCO.setRole("User");
		userRoleCO.setDescription("This is a normal user !");

		IpvUtils ipvUtils = new IpvUtils();
		ipvUtils.toRoleCO(roleService.createRole(ipvUtils.toRole(supperRoleCO)));
		ipvUtils.toRoleCO(roleService.createRole(ipvUtils.toRole(adminRoleCO)));
		ipvUtils.toRoleCO(roleService.createRole(ipvUtils.toRole(userRoleCO)));

	}

	public ResourceCO createSuperUser(String password) throws VeidblockException {
		VeidblockIO veidblockIO = new VeidblockIO();

		if (veidblockIO.isIdentityExisits()) {
			try {
				return new ObjectMapper().readValue(veidblockIO.readResourceCO(), ResourceCO.class);

			} catch (IOException e) {
				throw new VeidblockException(e);
				
			}
		}
		MyIdentity identity = veidblockIO.getMyIdentity();
		ResourceCO resourceDto = toDto(identity, password);
		boolean isAuthenticated = false;
		IpvUtils ipvUtils = new IpvUtils();
		Resource resource = ipvUtils.toResource(resourceDto);
		resource.setRtype("0");
		resource.setActiveStatus(true);
		IpUtils ipUtils = new IpUtils();
		resource.setResourceId(ipUtils.generateId());
		resource.setCreatedBy(resource.getResourceId());
		resource.setCreationDate(new Date());
		ResourceCO reg = ipvUtils.toResourceDTO(ipvDelegate.createResource(resource, isAuthenticated));
		if (!Objects.isNull(reg)) {
			roleService.assignRole(reg.getResourceId(), "Super");
			roleService.assignRole(reg.getResourceId(), "Admin");
		} else {
			System.err.println("Problems when creating super user !");
			System.exit(0);
		}

		try {
			veidblockIO.saveResourceCO(new ObjectMapper().writeValueAsString(reg));
		} catch (JsonProcessingException e) {
			throw new VeidblockException(e);
		}
		try {
			return new ObjectMapper().readValue(veidblockIO.readResourceCO(), ResourceCO.class);

		} catch (IOException e) {
			throw new VeidblockException(e);
		}
		
	}

	private ResourceCO toDto(MyIdentity identity, String password) {
		ResourceCO dto = new ResourceCO();
		dto.setName(identity.getName());
		dto.setEmail(identity.getEmail());
		dto.setUsername(identity.getUsername());
		dto.setOrganizationId(identity.getOrganizationId());
		
		dto.setAddress1(identity.getAddress1());
		dto.setAddress2(identity.getAddress2());
		dto.setPostCode(identity.getPostCode());
		dto.setLocation(identity.getLocation());
		
		dto.setCity(identity.getCity());
		dto.setState(identity.getState());
		dto.setCountry(identity.getCountry());
		dto.setMobile(identity.getMobile());
		dto.setPhone(identity.getPhone());
		dto.setBackupEmail(identity.getBackupEmail());
		dto.setUrl(identity.getUrl());
		dto.setPassword(password);
		return dto;
	}

}
