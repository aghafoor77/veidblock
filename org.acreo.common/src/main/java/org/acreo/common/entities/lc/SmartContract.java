package org.acreo.common.entities.lc;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = SmartContractFace.class)
public abstract class SmartContract {
	
	public enum SCOPE {OPEN,LOCAL};//,MULTIPLE,INDIVIDUAL};
	public enum SECURITY_LEVEL {NONE,DIGITAL_SIGNATURE,ENVELOPED,DIGITAL_SIGNATURE_ENVELOPED};
	private SCOPE scope;// Open, Local 
	private String[] payloadSupportingTypes; // prefer to specify classes
	private Date start;
	private Date end;
	private SECURITY_LEVEL securityLevel;
	public SCOPE getScope() {
		return scope;
	}
	public void setScope(SCOPE scope) {
		this.scope = scope;
	}
	public String[] getPayloadSupportingTypes() {
		return payloadSupportingTypes;
	}
	public void setPayloadSupportingTypes(String[] payloadSupportingTypes) {
		this.payloadSupportingTypes = payloadSupportingTypes;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public SECURITY_LEVEL getSecurityLevel() {
		return securityLevel;
	}
	public void setSecurityLevel(SECURITY_LEVEL securityLevel) {
		this.securityLevel = securityLevel;
	}
	
	public abstract void init (TransactionHeaderCO headerCO);
	
	public abstract void start(TransactionCO header);
	
	public abstract void close(Chain header);
	
}