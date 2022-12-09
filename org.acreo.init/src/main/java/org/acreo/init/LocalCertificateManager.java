package org.acreo.init;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import org.acreo.common.entities.DistinguishNameCO;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.entities.VeidblockConfig;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.security.certificate.CMSDelegator;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.utils.DistinguishName;
import org.acreo.security.utils.StoreHandling;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LocalCertificateManager {

	private CertificateSuite certificateSuite; 
	private String password;
	private MyIdentity owner = null;
	private VeidblockConfig veidblockConfig; 
	
	public LocalCertificateManager(CertificateSuite certificateSuite, String password, VeidblockConfig veidblockConfig ){
		this.certificateSuite = certificateSuite ;
		this.password= password;
		this.veidblockConfig = veidblockConfig ;
	}
	public void sendCertRequest(MyIdentity myIdentity) {
		
	}

	public X509Certificate fetchCertificate() throws VeidblockException {
		if(owner == null){
			owner = owner();
		}
		DistinguishNameCO distinguishNameCO = owner.toAnonymousDistinguishNameCO();
		CMSDelegator pkiDelegator = new CMSDelegator(password, certificateSuite);
		DistinguishName as = null;
		try {
			as = DistinguishName.builder().build(distinguishNameCO);
			return pkiDelegator.fetchCACert(as);
		} catch (VeidblockException e) {
			throw new VeidblockException(e);
		}
	}

	public X509Certificate[] fetchCertificateChain() throws VeidblockException {
		if(owner == null){
			owner = owner();
		}
		DistinguishNameCO distinguishNameCO = owner.toAnonymousDistinguishNameCO();

		CMSDelegator pkiDelegator = new CMSDelegator(password, certificateSuite);
		DistinguishName as = null;
		try {
			as = DistinguishName.builder().build(distinguishNameCO);
			return pkiDelegator.fetchCertChain(as);
		} catch (VeidblockException e) {
			throw new VeidblockException(e);
		}
	}

	public void generateSelfSignedCert() throws VeidblockException {
		if(owner == null){
			owner = owner();
		}
		DistinguishNameCO distinguishNameCO = owner.toAnonymousDistinguishNameCO();

		CMSDelegator pkiDelegator = new CMSDelegator(password, certificateSuite);
		DistinguishName as = null;
		try {
			as = DistinguishName.builder().build(distinguishNameCO);
			pkiDelegator.generateTopCACert(as);
		} catch (VeidblockException e) {
			// TODO Auto-generated catch block
			throw new VeidblockException(e);
		}
	}

	public PrivateKey getAuthServerPrivateKey() throws VeidblockException {
		if(owner == null){
			owner = owner();
		}
		DistinguishNameCO distinguishNameCO = owner.toAnonymousDistinguishNameCO();
		StoreHandling storeHandling = new StoreHandling();
		PrivateKey privateKey = storeHandling.fetchPrivateKey(certificateSuite, password,
				DistinguishName.builder().build(distinguishNameCO));
		if (privateKey == null) {
			throw new VeidblockException("Could not find Private key of "+distinguishNameCO.getName()+" from store ["+certificateSuite.getStorePath()+"]!");
		}
		return privateKey;

	}

	public MyIdentity owner() throws VeidblockException{
		VeidblockIO veidblockIO = new VeidblockIO();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return new MyIdentity(objectMapper.readValue(veidblockIO.readResourceCO(), ResourceCO.class));
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}
	
	// Fetch Registered resources certificates
	public X509Certificate fetchRegisteredResourcesCert(String resourceId) throws VeidblockException {
		DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
		distinguishNameCO.setName("" + resourceId);
		CMSDelegator pkiDelegator = new CMSDelegator(password, certificateSuite);
		DistinguishName as = null;
		try {
			as = DistinguishName.builder().build(distinguishNameCO);
			return pkiDelegator.fetchCACert(as);
		} catch (VeidblockException e) {
			throw new VeidblockException(e);
		}
	}
	
	
	public X509Certificate[] fetchRegisteredResourcesCertChain (String resourceId) throws VeidblockException {
		DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
		distinguishNameCO.setName("" + resourceId);

		CMSDelegator pkiDelegator = new CMSDelegator(password, certificateSuite);
		DistinguishName as = null;
		try {
			as = DistinguishName.builder().build(distinguishNameCO);
			return pkiDelegator.fetchCertChain(as);
		} catch (VeidblockException e) {
			throw new VeidblockException(e);
		}
	}
	
	public VeidblockConfig getVeidblockConfig() {
		return veidblockConfig;
	}
	
	public void setVeidblockConfig(VeidblockConfig veidblockConfig) {
		this.veidblockConfig = veidblockConfig;
	}
	
	public CertificateSuite getCertificateSuite() {
		return certificateSuite;
	}
	
	public void setCertificateSuite(CertificateSuite certificateSuite) {
		this.certificateSuite = certificateSuite;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}