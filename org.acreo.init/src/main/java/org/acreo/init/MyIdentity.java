package org.acreo.init;

import java.util.Properties;

import org.acreo.common.Representation;
import org.acreo.common.entities.DistinguishNameCO;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MyIdentity {
	private String name;
	private String email;
	private String username;
	private long organizationId;
	private String address1;
	private String address2;
	private String postCode;
	private String city;
	private String state;
	private String country;
	private String location;
	private String mobile;
	private String phone;
	private String backupEmail;
	private String url;
	private long id;

	public MyIdentity() {

	}

	public MyIdentity(ResourceCO dto) {

		this.setName(dto.getName());
		this.setEmail(dto.getEmail());
		this.setUsername(dto.getUsername());
		this.setOrganizationId(dto.getOrganizationId());
		
		this.setAddress1(dto.getAddress1());
		this.setAddress2(dto.getAddress2());
		this.setPostCode(dto.getPostCode());
		this.setLocation(dto.getLocation());
		
		this.setCity(dto.getCity());
		this.setState(dto.getState());
		this.setCountry(dto.getCountry());
		this.setMobile(dto.getMobile());
		this.setPhone(dto.getPhone());
		this.setBackupEmail(dto.getBackupEmail());
		this.setUrl(dto.getUrl());
		this.setId(dto.getResourceId());
	}

	public MyIdentity(Properties properties) {

		this.setName(properties.getProperty("name"));
		this.setEmail(properties.getProperty("email"));
		this.setUsername(properties.getProperty("username"));
		
		this.setAddress1(properties.getProperty("Address1"));
		this.setAddress2(properties.getProperty("Address2"));
		this.setPostCode(properties.getProperty("PostCode"));
		this.setLocation(properties.getProperty("Location"));
		
		this.setCity(properties.getProperty("city"));
		this.setState(properties.getProperty("state"));
		this.setCountry(properties.getProperty("country"));
		this.setMobile(properties.getProperty("mobile"));
		this.setPhone(properties.getProperty("phone"));
		this.setBackupEmail(properties.getProperty("backupEmail"));
		this.setUrl(properties.getProperty("url"));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBackupEmail() {
		return backupEmail;
	}

	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 
	 * @param restClient
	 * @return
	 */
	public Representation register(RestClient restClient, String password) {
		try {
			Representation response = restClient.post("/resource/server", toDto(password), null);
			return response;
		} catch (VeidblockException e) {
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				return objectMapper.readValue(e.getMessage(), Representation.class);
			} catch (Exception e1) {
				return new Representation(-1, e1.getMessage());
			}
		}
	}

	public ResourceCO toDto(String password) {
		ResourceCO dto = new ResourceCO();
		dto.setName(name);
		dto.setEmail(email);
		dto.setUsername(username);
		dto.setOrganizationId(organizationId);
		
		dto.setAddress1(getAddress1());
		dto.setAddress2(getAddress2());
		dto.setPostCode(getPostCode());
		dto.setLocation(getLocation());
		
		
		dto.setCity(city);
		dto.setState(state);
		dto.setCountry(country);
		dto.setMobile(mobile);
		dto.setPhone(phone);
		dto.setBackupEmail(backupEmail);
		dto.setUrl(url);
		dto.setPassword(password);
		return dto;
	}

	public DistinguishNameCO toDistinguishNameCO() {
		DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
		distinguishNameCO.setName("" + getId());
		distinguishNameCO.setEmail(email);
		distinguishNameCO.setOrganization(""+getOrganizationId());
		distinguishNameCO.setLocality(city);
		distinguishNameCO.setState(state);
		distinguishNameCO.setCountry(country);
		distinguishNameCO.setUrl(url);
		return distinguishNameCO;
	}

	public DistinguishNameCO toAnonymousDistinguishNameCO() {
		DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
		distinguishNameCO.setName("" + getId());
		distinguishNameCO.setUrl(url);
		return distinguishNameCO;
	}
}
