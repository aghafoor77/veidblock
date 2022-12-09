package org.acreo.ip.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "RoleAssignment")
public class RoleAssignment implements Serializable{
	
	@Id
	@Column(name="roleId")
	@NotEmpty
	private long roleId;

	@Id
	@Column(name="resourceId")
	private long resourceId;
	
	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	public long getResourceId() {
		return resourceId;
	}

	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	
	
}
