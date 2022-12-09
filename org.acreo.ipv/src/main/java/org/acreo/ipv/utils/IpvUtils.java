package org.acreo.ipv.utils;

import java.util.ArrayList;
import java.util.List;

import org.acreo.common.entities.OrganizationCO;
import org.acreo.common.entities.OrganizationCOList;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.entities.ResourceCOList;
import org.acreo.common.entities.RoleCO;
import org.acreo.common.entities.RoleCOList;
import org.acreo.ip.entities.Organization;
import org.acreo.ip.entities.Resource;
import org.acreo.ip.entities.Role;

public class IpvUtils {

	public Resource toResource(ResourceCO resourceDTO) {
		Resource resource = new Resource();
		resource.setResourceId(resourceDTO.getResourceId());
		
		resource.setOrganizationId(resourceDTO.getOrganizationId());
		
		resource.setAddress1(resourceDTO.getAddress1()); 
		resource.setAddress2(resourceDTO.getAddress2());
		resource.setPostCode(resourceDTO.getPostCode());
		resource.setCity(resourceDTO.getCity());
		resource.setState(resourceDTO.getState());
		resource.setCountry(resourceDTO.getCountry());
		resource.setLocation(resourceDTO.getLocation());
		
		resource.setName(resourceDTO.getName());
		resource.setEmail(resourceDTO.getEmail());
		resource.setUsername(resourceDTO.getUsername());
		resource.setPassword(resourceDTO.getPassword());

		resource.setCreationDate(resourceDTO.getCreationDate());
		
		List<String> roles = resourceDTO.getRoles();
		List<Role> copyRoles = new ArrayList<Role>();
		if (roles != null) {
			for (String role : roles) {
				Role r = new Role();
				r.setRole(role);
				copyRoles.add(r);
			}
		}
		resource.setRoles(copyRoles);
		resource.setMobile(resourceDTO.getMobile());
		resource.setPhone(resourceDTO.getPhone());
		resource.setBackupEmail(resourceDTO.getBackupEmail());
		resource.setActiveStatus(resourceDTO.isActiveStatus());
		resource.setUrl(resourceDTO.getUrl());
		return resource;
	}
	public Resource toUpdateResource(ResourceCO resourceDTO, Resource resourceRet) {
		Resource resource = new Resource();
		
		resource.setOrganizationId(resourceRet.getOrganizationId());
		
		
		if(resourceDTO.getAddress1() != null){
			resource.setAddress1(resourceDTO.getAddress1());
		} else{
			resource.setAddress1(resourceRet.getAddress1());
		}
		
		if(resourceDTO.getAddress2() != null){
			resource.setAddress2(resourceDTO.getAddress2());
		} else{
			resource.setAddress2(resourceRet.getAddress2());
		}
		
		
		if(resourceDTO.getCity() != null){
			resource.setCity(resourceDTO.getCity());
		} else{
			resource.setCity(resourceRet.getCity());
		}
		
		if(resourceDTO.getState() != null){
			resource.setState(resourceDTO.getState());
		} else{
			resource.setState(resourceRet.getState());
		}
		
		if(resourceDTO.getCountry() != null){
			resource.setCountry(resourceDTO.getCountry());
		} else{
			resource.setCountry(resourceRet.getCountry());
		}
		
		if(resourceDTO.getPostCode() != null){
			resource.setPostCode(resourceDTO.getPostCode());
		} else{
			resource.setPostCode(resourceRet.getPostCode());
		}
		
		if(resourceDTO.getLocation() != null){
			resource.setLocation(resourceDTO.getLocation());
		} else{
			resource.setLocation(resourceRet.getLocation());
		}
		
		if(resourceDTO.getResourceId() != 0){
			resource.setResourceId(resourceDTO.getResourceId());
		} else{
			resource.setResourceId(resourceRet.getResourceId());
		}
		
		if(resourceDTO.getName() != null){
			resource.setName(resourceDTO.getName());
		} else{
			resource.setName(resourceRet.getName());
		}
		
		if(resourceDTO.getEmail() != null){
			resource.setEmail(resourceDTO.getEmail());
		} else{
			resource.setEmail(resourceRet.getEmail());
		}
		
		if(resourceDTO.getUsername() != null){
			resource.setUsername(resourceDTO.getUsername());
		} else{
			resource.setUsername(resourceRet.getUsername());
		}

		if(resourceDTO.getPassword() != null){
			resource.setPassword(resourceDTO.getPassword());
		} else{
			resource.setPassword(resourceRet.getPassword());
		}
		
		if(resourceDTO.getCreationDate() != null){
			resource.setCreationDate(resourceDTO.getCreationDate());
		} else{
			resource.setCreationDate(resourceRet.getCreationDate());
		}
		
		
		resource.setRoles(resourceRet.getRoles());
		
		if(resourceDTO.getMobile() != null){
			resource.setMobile(resourceDTO.getMobile());
		} else{
			resource.setMobile(resourceRet.getMobile());
		}
		
		if(resourceDTO.getPhone() != null){
			resource.setPhone(resourceDTO.getPhone());
		} else{
			resource.setPhone(resourceRet.getPhone());
		}
		
		if(resourceDTO.getBackupEmail() != null){
			resource.setBackupEmail(resourceDTO.getBackupEmail());
		} else{
			resource.setBackupEmail(resourceRet.getBackupEmail());
		}
		
		resource.setActiveStatus(resourceRet.isActiveStatus());
		
		if(resourceDTO.getUrl() != null){
			resource.setUrl(resourceDTO.getUrl());
		} else{
			resource.setUrl(resourceRet.getUrl());
		}
		resource.setCreatedBy(resourceRet.getCreatedBy());
		return resource;
	}

	public ResourceCO toResourceDTO(Resource resource ) {
		ResourceCO dto = new ResourceCO();
		dto.setOrganizationId(resource.getOrganizationId());
		
		dto.setAddress1(resource.getAddress1());
		dto.setAddress2(resource.getAddress2());
		dto.setPostCode(resource.getPostCode());
		dto.setLocation(resource.getLocation());
		
		dto.setCity(resource.getCity());
		dto.setState(resource.getState());
		dto.setCountry(resource.getCountry());
		

		dto.setResourceId(resource.getResourceId());
		dto.setName(resource.getName());
		dto.setEmail(resource.getEmail());

		dto.setUsername(resource.getUsername());
		dto.setPassword(resource.getPassword());

		dto.setCreationDate(resource.getCreationDate());
		dto.setOrganizationId(resource.getOrganizationId());

		List<Role> roles = resource.getRoles();
		List<String> copyRoles = new ArrayList<String>();
		if (roles != null) {
			for (Role role : roles) {				
				copyRoles.add(role.getRole());
			}
		} else {
			dto.setRoles(null);
		}
		dto.setRoles(copyRoles);
		dto.setMobile(resource.getMobile());
		dto.setPhone(resource.getPhone());
		dto.setBackupEmail(resource.getBackupEmail());
		dto.setActiveStatus(resource.isActiveStatus());
		dto.setUrl(resource.getUrl());
		dto.setCreatedBy(""+resource.getCreatedBy());
		dto.setUser(resource.getRtype().equals("0") ? true : false);
		return dto;
	}

	public ResourceCOList resourceToResourceDTO(List<Resource> resources) {

		ResourceCOList dtos = new ResourceCOList();

		for (Resource resource : resources) {
			resource.setPassword("NOT-ALLOWED");
			dtos.add(toResourceDTO(resource));
		}
		return dtos;
	}

	public Organization toOrganization(OrganizationCO organizationCO) {

		Organization organization = new Organization();
		organization.setCity(organizationCO.getCity());
		organization.setCountry(organizationCO.getCountry());
		organization.setName(organizationCO.getName());
		organization.setOrganizationId(organizationCO.getOrganizationId());
		organization.setOrganizationUnit(organizationCO.getOrganizationUnit());
		organization.setState(organizationCO.getState());
		organization.setStreet(organizationCO.getStreet());
		return organization;
	}

	public OrganizationCO toOrganizationCO(Organization organization) {

		OrganizationCO organizationCO = new OrganizationCO();
		organizationCO.setCity(organization.getCity());
		organizationCO.setCountry(organization.getCountry());
		organizationCO.setName(organization.getName());
		organizationCO.setOrganizationId(organization.getOrganizationId());
		organizationCO.setOrganizationUnit(organization.getOrganizationUnit());
		organizationCO.setState(organization.getState());
		organizationCO.setStreet(organization.getStreet());

		return organizationCO;
	}

	public OrganizationCOList toOrganizationCOList(List<Organization> organizations) {

		OrganizationCOList organizationCOList = new OrganizationCOList();
		for (Organization organization : organizations) {
			organizationCOList.add(toOrganizationCO(organization));
		}
		return organizationCOList;
	}

	public RoleCO toRoleCO(Role role) {
		RoleCO roleCO = new RoleCO();
		roleCO.setRole(role.getRole());
		roleCO.setRoleId(role.getRoleId());
		roleCO.setDescription(role.getDescription());
		roleCO.setCreatedBy(role.getCreatedBy());
		return roleCO;
	}

	public Role toRole(RoleCO roleCO) {
		Role role = new Role();
		role.setRole(roleCO.getRole());
		role.setRoleId(roleCO.getRoleId());
		role.setDescription(roleCO.getDescription());
		return role;
	}

	public RoleCOList toRoleCOList(List<Role> roles) {
		RoleCOList roleCOList = new RoleCOList();
		for (Role role: roles) {
			roleCOList.add(toRoleCO(role));
		}
		return roleCOList;
	}
	// RoleCOList
}
