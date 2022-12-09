package org.acreo.common.entities;

import org.acreo.common.exceptions.VeidblockException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizedPayload {
	// Payload can be token
	private String payload;
	// This is the secret used to verify user and to protect against
	// masquerading attack
	private String secretInfo;

	public AuthorizedPayload() {
		
	}
	
	public AuthorizedPayload(String payload, String secretInfo) {
		this.payload = payload;
		this.secretInfo = secretInfo;
	}

	public String getPayload() {
		return payload;
	}

	public String getSecretInfo() {
		return secretInfo;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public void setSecretInfo(String secretInfo) {
		this.secretInfo = secretInfo;
	}

	public String toJson() throws VeidblockException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			throw new VeidblockException(e);
		}

	}

	public void fromJson(String authorizedPayloadEncoded) throws VeidblockException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			AuthorizedPayload authorizedPayload = objectMapper.readValue(authorizedPayloadEncoded,
					AuthorizedPayload.class);
			
			this.setPayload(authorizedPayload.getPayload());
			this.setSecretInfo(authorizedPayload.getSecretInfo());
			
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}
}
