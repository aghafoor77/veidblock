package org.acreo.ip.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Organization")
public class Organization {
	@Id
	@Column(name="organizationId")
	@NotNull
	private Long organizationId;
	private String name;
	private String organizationUnit;
	private String street;
	private String city;
	private String state;
	private String country;
	
	public Organization(){
		super();
	}
	
	public Organization (long organizationId, String name, String organizationUnit, String street, String city, String state, String country){
		this.organizationId = organizationId;
		this.name = name;
		this.organizationUnit = organizationUnit;
		this.street = street;
		this.city = city; 
		this.state = state;
		this.country = country;
		
	}
	
	
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganizationUnit() {
		return organizationUnit;
	}
	public void setOrganizationUnit(String organizationUnit) {
		this.organizationUnit = organizationUnit;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
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
}
