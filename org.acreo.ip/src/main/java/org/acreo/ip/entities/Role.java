package org.acreo.ip.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "Role")
public class Role {

	@Id
	@Column(name = "roleId")
	@NotNull
	private Long roleId;
	private String role;
	private String description;
	private String createdBy;

	public Role() {

	}

	public Role(Long roleId, String description, String role, String createdBy) {
		this.roleId = roleId;
		this.role = role;
		this.description = description;
		this.createdBy= createdBy;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	
	
}