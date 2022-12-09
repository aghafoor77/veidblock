package org.acreo.common.entities;

public class Endorsement {
	
	private String prefix;
	private String name;
	private String email;
	private long organizationId;
	private String address1;
	private String address2;
	private String postCode;
	private String location;
	private String city;
	private String state;
	private String country;
	private String mobile;
	private String phone;
	private String url;
	private String pubKeyVeriURL;
	private String encodedPubKey;
	private String signature;
	private String encodedPubKeyOfEndorser;
	
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
	
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
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
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPubKeyVeriURL() {
		return pubKeyVeriURL;
	}
	public void setPubKeyVeriURL(String pubKeyVeriURL) {
		this.pubKeyVeriURL = pubKeyVeriURL;
	}
	public String getEncodedPubKey() {
		return encodedPubKey;
	}
	public void setEncodedPubKey(String encodedPubKey) {
		this.encodedPubKey = encodedPubKey;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getEncodedPubKeyOfEndorser() {
		return encodedPubKeyOfEndorser;
	}
	public void setEncodedPubKeyOfEndorser(String encodedPubKeyOfEndorser) {
		this.encodedPubKeyOfEndorser = encodedPubKeyOfEndorser;
	}
	
	
}