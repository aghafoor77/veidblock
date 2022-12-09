package org.acreo.common.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AuthenticationType {
	BASIC("Basic"), DIGEST("Digest"), BEARER("Bearer"), CLIENT_CERT("Client_Cert"), OTP("OTP"), OTHER("Other");
	
	String value; 
	AuthenticationType(String type){
		this.value = type;
	}
	
	@JsonValue
	public String value(){
		return this.value;
	}
}
