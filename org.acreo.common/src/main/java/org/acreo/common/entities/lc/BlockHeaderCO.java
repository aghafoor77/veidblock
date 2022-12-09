package org.acreo.common.entities.lc;

public class BlockHeaderCO {
	
	private String creator;
	private String chainName;
	private SmartContract smartcontract;
	private String signedBy;
	private String signedDate;
	private String signerUrl;
	private String signature;
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getChainName() {
		return chainName;
	}
	public void setChainName(String chainName) {
		this.chainName = chainName;
	}
	
	public SmartContract getSmartcontract() {
		return smartcontract;
	}
	public void setSmartcontract(SmartContract smartcontract) {
		this.smartcontract = smartcontract;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSignerUrl() {
		return signerUrl;
	}
	public void setSignerUrl(String signerUrl) {
		this.signerUrl = signerUrl;
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
}
