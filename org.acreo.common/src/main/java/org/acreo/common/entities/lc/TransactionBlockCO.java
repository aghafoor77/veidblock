package org.acreo.common.entities.lc;

public class TransactionBlockCO {

	private String creationTime;
	private String scope; // It should be the same as smart contract
	private String sender;
	private String receiver;

	private String payload;
	private String payloadType; // Should be one of the defined in the smart
								// contract
	private String cryptoOperationsOnPayload;

	private String signedBy;
	private String signedDate;
	private String signerUrl;
	private String signature;

	public TransactionBlockCO() {

	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getPayloadType() {
		return payloadType;
	}

	public void setPayloadType(String payloadType) {
		this.payloadType = payloadType;
	}

	public String getCryptoOperationsOnPayload() {
		return cryptoOperationsOnPayload;
	}

	public void setCryptoOperationsOnPayload(String cryptoOperationsOnPayload) {
		this.cryptoOperationsOnPayload = cryptoOperationsOnPayload;
	}

	public String getSignedBy() {
		return signedBy;
	}

	public void setSignedBy(String signedBy) {
		this.signedBy = signedBy;
	}

	public String getSignedDate() {
		return signedDate;
	}

	public void setSignedDate(String signedDate) {
		this.signedDate = signedDate;
	}

	public String getSignerUrl() {
		return signerUrl;
	}

	public void setSignerUrl(String signerUrl) {
		this.signerUrl = signerUrl;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}	
	
	
}