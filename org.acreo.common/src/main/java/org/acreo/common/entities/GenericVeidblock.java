package org.acreo.common.entities;

public class GenericVeidblock {
	
	private long senderId;
	private long seqNo;
	private long receiverId;
	private String payload;
	private String creationTime;
	private String previousHash;
	private String url;	
	private String status;
	private String validatorSignature;
	private String validatorPublicKey;
	
	public GenericVeidblock(){
		
	}
			
	public GenericVeidblock(long senderId,long seqNo, long receiverId, 
			String payload, String creationTime, String previousHash, String url, String status, String validatorSignature, String validatorPublicKey) {
		this.senderId = senderId; 
		this.receiverId = receiverId;
		this.payload = payload;
		this.creationTime = creationTime;
		this.previousHash = previousHash;		
		this.status = status;
		this.validatorSignature = validatorSignature;
		this.seqNo= seqNo;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(long seqNo) {
		this.seqNo = seqNo;
	}

	public long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(long receiverId) {
		this.receiverId = receiverId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	public String getValidatorPublicKey() {
		return validatorPublicKey;
	}

	public void setValidatorPublicKey(String validatorPublicKey) {
		this.validatorPublicKey = validatorPublicKey;
	}

	public String getValidatorSignature() {
		return validatorSignature;
	}

	public void setValidatorSignature(String validatorSignature) {
		this.validatorSignature = validatorSignature;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
