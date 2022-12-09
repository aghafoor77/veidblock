package org.acreo.common.entities;

public class VeidblockIdentityCO {
	
	private long veid;
	private long counter;
	private String payload;
	private String creationTime;
	private String previousHash;

	public VeidblockIdentityCO() {

	}

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public long getVeid() {
		return veid;
	}

	public void setVeid(long veid) {
		this.veid = veid;
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
}