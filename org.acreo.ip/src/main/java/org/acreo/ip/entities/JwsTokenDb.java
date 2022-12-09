package org.acreo.ip.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "JwsToken")
@XmlRootElement
public class JwsTokenDb {
	
	@Id
	@Column(name = "jti")
	private long jti; // Unique id
	
	private String alg;
	private String type;
	
	private String iss;// URL of our idms
	private String sub; // Subject
	private String ver;// URL of our certificate for verification
	private Date exp; // Exp time
	private String pub;
	private String scp; // scope separated by comma
	private String refreshToken;
		
	private String signature;
	
	private String signerData;
	private int signerDataType;
	
	public JwsTokenDb(){
		
	}
	public JwsTokenDb(long jti, String alg, String type, String iss, String sub, String ver, Date exp, String pub, String scp, String refreshToken, String signature, String signerData,
			int signerDataType){
		this.jti = jti;
		this.alg = alg;
		this.type = type;
		this.iss = iss;
		this.sub = sub;
		this.ver = ver;
		this.exp = exp;
		this.pub = pub;
		this.scp = scp;
		this.refreshToken = refreshToken;
		this.signature = signature;
		this.signerData = signerData;
		this.signerDataType = signerDataType;				
	}
	
	public long getJti() {
		return jti;
	}
	public void setJti(long jti) {
		this.jti = jti;
	}
	public String getAlg() {
		return alg;
	}
	public void setAlg(String alg) {
		this.alg = alg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public Date getExp() {
		return exp;
	}
	public void setExp(Date exp) {
		this.exp = exp;
	}
	public String getScp() {
		return scp;
	}
	public void setScp(String scp) {
		this.scp = scp;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSignerData() {
		return signerData;
	}
	public void setSignerData(String signerData) {
		this.signerData = signerData;
	}
	public int getSignerDataType() {
		return signerDataType;
	}
	public void setSignerDataType(int signerDataType) {
		this.signerDataType = signerDataType;
	}
	public String getPub() {
		return pub;
	}
	public void setPub(String pub) {
		this.pub = pub;
	}
}
