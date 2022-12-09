package org.acreo.common.entities;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignatureInfo {
	
	private byte signature[] = null;
	private byte[] publickey = null;
	// It can be user, resource id or any other identity to identify the owner of signature generator.
	private String owner = null;
	
	protected SignatureInfo() {

	}

	private SignatureInfo(String json) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			SignatureInfo signatureInfo = objectMapper.readValue(json, SignatureInfo.class);
			copy(signatureInfo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] getPublickey() {
		return publickey;
	}

	public void setPublickey(byte[] publickey) {
		this.publickey = publickey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte signature[]) {
		this.signature = signature;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String toEncoded() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public static SignatureInfo.Builder builder() {
		return new Builder();
	}

	public static class Builder {
		SignatureInfo signatureInfo = new SignatureInfo();

		protected Builder() {
			// Hide default constructor
		}

		public SignatureInfo.Builder signature(byte[] signature) {
			if (null == signature) {
				return this;
			}
			signatureInfo.setSignature(signature);
			return this;
		}

		public SignatureInfo.Builder publickey(byte []publickey) {
			if (null == publickey) {
				return this;
			}
			signatureInfo.setPublickey(publickey);
			return this;
		}
		public SignatureInfo.Builder owner(String owner) {
			if (null == owner) {
				return this;
			}
			signatureInfo.setOwner(owner);
			return this;
		}

		public SignatureInfo build() {

			return new SignatureInfo(signatureInfo.toEncoded());
		}

		public SignatureInfo build(String json) {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				this.signatureInfo= objectMapper.readValue(json, SignatureInfo.class);
			} catch (Exception exp) {
				exp.printStackTrace();
			}
			return build();
		}
	}

	public void copy(SignatureInfo signatureInfo) {
		this.setSignature(signatureInfo.getSignature());
		this.setPublickey(signatureInfo.getPublickey());
		this.setOwner(signatureInfo.getOwner());
	}

	@Override
	public String toString() {
		return this.toEncoded();
	}

	public static void main(String arg[]) throws IOException {
		System.out.println(SignatureInfo.builder()
				.build(SignatureInfo.builder().signature("1111".getBytes()).build().toString()).toString());
	}
}
