package org.acreo.common.entities;

public class GenericVeidblockHeader {

	private long veid;
	private long senderId;
	private String version;
	private String hashPrevBlock;
	private String hashMerkleRoot;
	private String creationTime;

	public GenericVeidblockHeader() {

	}

	public GenericVeidblockHeader(long veid, long senderId, String version, String hashPrevBlock, String hashMerkleRoot,
			String creationTime) {
		this.veid = veid;
		this.senderId = senderId;
		this.version = version;
		this.hashPrevBlock = hashPrevBlock;
		this.hashMerkleRoot = hashMerkleRoot;
		this.creationTime = creationTime;
	}
	
	public long getVeid() {
		return veid;
	}

	public void setVeid(long veid) {
		this.veid = veid;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getHashPrevBlock() {
		return hashPrevBlock;
	}

	public void setHashPrevBlock(String hashPrevBlock) {
		this.hashPrevBlock = hashPrevBlock;
	}

	public String getHashMerkleRoot() {
		return hashMerkleRoot;
	}

	public void setHashMerkleRoot(String hashMerkleRoot) {
		this.hashMerkleRoot = hashMerkleRoot;
	}

	public String getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
}
