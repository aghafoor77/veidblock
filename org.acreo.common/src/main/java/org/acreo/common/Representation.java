package org.acreo.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Representation<T> {
	private long code;

	private T body;

	public Representation() {
		// Jackson deserialization
	}

	public Representation(String r) {
		ObjectMapper objectMapper = new ObjectMapper();
		Representation rr;
		try {
			rr = objectMapper.readValue(r, Representation.class);
			this.code = rr.getCode();
			this.body = (T) rr.getBody();
		} catch (Exception e) {
			System.out.println("-----------------------------------------------------");
			e.printStackTrace();
		}
	}

	public Representation(long code, T body) {
		this.code = code;
		this.body = body;
	}

	@JsonProperty
	public long getCode() {
		return code;
	}

	@JsonProperty
	public T getBody() {
		return body;
	}

	public String toJson() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			return "{\"code\":-1, \"body\":\"Problem when processing Representation objec !\"}";
		}
	}

}