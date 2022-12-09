package org.acreo.common.entities;

import org.acreo.common.exceptions.VeidblockException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthorizedVeidblock {
	
	private long authorizedBy;
	private long authorizedTo;
	private String payload;
		
	public long getAuthorizedBy() {
		return authorizedBy;
	}
	public void setAuthorizedBy(long authorizedBy) {
		this.authorizedBy = authorizedBy;
	}
	
	public long getAuthorizedTo() {
		return authorizedTo;
	}
	public void setAuthorizedTo(long authorizedTo) {
		this.authorizedTo = authorizedTo;
	}
	
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	
	public String toJson() throws VeidblockException{
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
			AuthorizedVeidblock authorizedVeidblock = objectMapper.readValue(authorizedPayloadEncoded,
					AuthorizedVeidblock.class);
			this.setAuthorizedBy(authorizedVeidblock.getAuthorizedBy());
			this.setAuthorizedTo(authorizedVeidblock.getAuthorizedTo());
			this.setPayload(authorizedVeidblock.getPayload());			

		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}	
}
