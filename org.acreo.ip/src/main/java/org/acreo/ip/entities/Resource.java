package org.acreo.ip.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "Resource")
public class Resource {

	private long resourceId;
	private String name;
	private String email;

	@Id
	@Column(name = "username")
	@NotEmpty
	private String username;
	private String password;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd,HH:mm:ss", timezone = "GMT")
	private Date creationDate;

	private Long organizationId;
	
	@Transient
	private List<Role> roles;
	
	private String mobile;
	private String phone;
	private String backupEmail;
	private boolean activeStatus;
	private String rtype;
	private String url;
	private long createdBy;
	
	// Newly added fields
	private String address1;
	private String address2;
	private String postCode;
	private String city;
	private String state;
	private String country;
	private String location;
	

	public Resource() {

	}

	public Resource(String username, String backupEmail, Date creationDate, String email, Boolean activeStatus, String mobile,
			String name, String password, Long resourceId, String phone, Long organizationId, String address1,
			String address2,
			String postCode,
			String city,
			String state,
			String country,
			String location,
			String rtype,String url, long createdBy) {
		this.username = username;
		this.backupEmail = backupEmail;
		this.creationDate = creationDate;
		this.email = email;
		this.activeStatus = activeStatus;
		this.mobile = mobile;
		this.name = name;
		this.password = password;
		this.resourceId = resourceId;
		this.phone = phone;
		this.organizationId = organizationId;
		this.rtype = rtype;
		this.url = url;
		this.createdBy = createdBy;
		
		this.address1 = address1;
		this.address2 = address2;
		this.postCode = postCode;
		this.city = city;
		this.state = state;
		this.country = country;
		this.location = location;
		
		
		
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	/*public void setOrganization(Organization organization) {
		this.setOrganizationId(organization.getOrganizationId());
		this.organization = organization;
	}*/

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (null == this.roles) {
			this.roles = new ArrayList<Role>();
		}
		this.roles.add(role);
	}

	public String getMobile() {
		return mobile;
	}
	
	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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
	
	public boolean isActiveStatus() {
		return activeStatus;
	}

	public void setActiveStatus(boolean activeStatus) {
		this.activeStatus = activeStatus;
	}
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(long createdBy) {
		this.createdBy = createdBy;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
