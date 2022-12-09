package org.acreo.common.entities;

public class VeidblockHeaderCO {
	
	private byte version[] = null; 
	private byte hashPrevBlock[] = null; 
	private byte hashMerkleRoot[] = null;
	private byte time[] = null;
	private byte bits[] = null;
	private byte nonce[] = null;
	public byte[] getVersion() {
		return version;
	}
	public void setVersion(byte[] version) {
		this.version = version;
	}
	public byte[] getHashPrevBlock() {
		return hashPrevBlock;
	}
	public void setHashPrevBlock(byte[] hashPrevBlock) {
		this.hashPrevBlock = hashPrevBlock;
	}
	public byte[] getHashMerkleRoot() {
		return hashMerkleRoot;
	}
	public void setHashMerkleRoot(byte[] hashMerkleRoot) {
		this.hashMerkleRoot = hashMerkleRoot;
	}
	public byte[] getTime() {
		return time;
	}
	public void setTime(byte[] time) {
		this.time = time;
	}
	public byte[] getBits() {
		return bits;
	}
	public void setBits(byte[] bits) {
		this.bits = bits;
	}
	public byte[] getNonce() {
		return nonce;
	}
	public void setNonce(byte[] nonce) {
		this.nonce = nonce;
	}
}
