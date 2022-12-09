package org.acreo.ip.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "OTP")
public class OTP implements Serializable {

	@Id
	@Column(name = "otp")
	@NotEmpty
	private String otp;

	@Id
	@Column(name = "resourceId")
	@NotEmpty
	private String resourceId;

	@Column(name = "sentDate")
	@NotEmpty
	private Date sentDate;

	@Column(name = "expStatus")
	@NotEmpty
	private int expStatus;

	public OTP() {

	}

	public OTP(String otp, String resourceId, Date sentDate, int expStatus) {
		this.otp = otp;
		this.resourceId = resourceId;
		this.sentDate = sentDate;
		this.expStatus = expStatus;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public Date getSentDate() {
		return sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public int getExpStatus() {
		return expStatus;
	}

	public void setExpStatus(int expStatus) {
		this.expStatus = expStatus;
	}
}

