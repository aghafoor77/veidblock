package org.acreo.init;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import org.acreo.common.entities.CommonEnums;
import org.acreo.common.entities.DistinguishNameCO;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.security.bc.CertificateHandlingBC;
import org.acreo.security.certificate.CMSDelegator;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.utils.DistinguishName;
import org.acreo.security.utils.PEMStream;
import org.acreo.security.utils.StoreHandling;
import org.bouncycastle.asn1.x509.KeyUsage;

public class UserCertificateManager {

	private CertificateSuite certificateSuite;
	public UserCertificateManager(CertificateSuite certificateSuite) {
		this.certificateSuite = certificateSuite;

	}	
	public String createCertRequest(ResourceCO resourceCO) throws VeidblockException {

		if (resourceCO.getPassword() == null || resourceCO.getPassword().equals("")) {
			throw new VeidblockException("Password not specified !");
		}

		DistinguishName dn = DistinguishName.builder().build(toAnonymousDistinguishNameCO(resourceCO));
		int keyUsage = KeyUsage.digitalSignature | KeyUsage.keyEncipherment | KeyUsage.dataEncipherment
				| KeyUsage.nonRepudiation;
		
		CertificateHandlingBC certificateHandlingBC = new CertificateHandlingBC(this.certificateSuite,resourceCO.getPassword());
		byte [] encidedCSR = certificateHandlingBC.createCSR(dn, keyUsage );
		
		PEMStream pemStream = new PEMStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (pemStream.csr2pem(encidedCSR, baos)) {
			return new String(baos.toByteArray());
		}
		throw new VeidblockException("Problems when creating Certificate Request (PKCS10) !");		
	}

	public X509Certificate fetchCertificate(ResourceCO resourceCO) throws VeidblockException {

		StoreHandling storeHandling = new StoreHandling();
		DistinguishName dn = DistinguishName.builder().build(toAnonymousDistinguishNameCO(resourceCO));
		X509Certificate certificate = storeHandling.fetchCertificate(certificateSuite, dn);
		if (null == certificate) {
			throw new VeidblockException("Certificate not found !");
		} else {
			return certificate;
		}
	}

	public String fetchCertificateChain(ResourceCO resourceCO) throws VeidblockException {

		if (resourceCO.getPassword() == null || resourceCO.getPassword().equals("")) {
			throw new VeidblockException("Password not specified !");
		}

		CMSDelegator pkiDelegator = new CMSDelegator(resourceCO.getPassword(), certificateSuite);
		try {
			DistinguishName dn = DistinguishName.builder().build(toAnonymousDistinguishNameCO(resourceCO));
			return pkiDelegator.fetchPkiPemChain(dn);

		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}

	public X509Certificate[] fetchX509CertificateChain(ResourceCO resourceCO) throws VeidblockException {

		if (resourceCO.getPassword() == null || resourceCO.getPassword().equals("")) {
			throw new VeidblockException("Password not specified !");
		}

		CMSDelegator pkiDelegator = new CMSDelegator(resourceCO.getPassword(), certificateSuite);
		try {
			DistinguishName dn = DistinguishName.builder().build(toAnonymousDistinguishNameCO(resourceCO));
			return pkiDelegator.fetchCertChain(dn);

		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}

	public String signCert(LocalCertificateManager localCertificateManager, String pkcs10EncodedReq,
			long keyUsage) throws VeidblockException {

		CertificateHandlingBC certificateHandlingBC = new CertificateHandlingBC(this.certificateSuite,localCertificateManager.getPassword());
		
		PEMStream pemStream = new PEMStream();
		byte pkcs10 [] = pemStream.pem2csr(pkcs10EncodedReq);
		
		X509Certificate x509Certificate = certificateHandlingBC.signCert(localCertificateManager.getAuthServerPrivateKey(), localCertificateManager.fetchCertificate(), pkcs10 ); 
		try {
			
			
			StoreHandling storeHandling = new StoreHandling();
			if (null != x509Certificate) {
				boolean result = storeHandling.storeCertificate(localCertificateManager.getCertificateSuite(), x509Certificate);
				if (!result) {
					throw new VeidblockException("Problems when storing newly signed certificate !");
				} 
			}
			
			CMSDelegator pkiDelegator = new CMSDelegator(localCertificateManager.getPassword(),
					localCertificateManager.getCertificateSuite());
			DistinguishName subject = DistinguishName.builder().build(x509Certificate.getSubjectDN().toString());
			return pkiDelegator.fetchCApemChain(subject);
		} catch (VeidblockException e) {
			throw new VeidblockException(e);
		}
	}

	public X509Certificate [] processCertResponse(ResourceCO resourceCO, String certResponse) throws VeidblockException {
		PEMStream pemStream = new PEMStream();

		X509Certificate chain[] = null;
		try {
			chain = pemStream.extractCertChain(certResponse);
		} catch (Exception e) {
			throw new VeidblockException("Certificate respone is empty : " + e.getMessage());
		}
		if (chain.length == 0) {
			throw new VeidblockException("Certificate respone is empty !");
		}
		StoreHandling storeHandling = new StoreHandling();
		DistinguishName subject = DistinguishName.builder().build(toAnonymousDistinguishNameCO(resourceCO));
		PrivateKey privateKey = storeHandling.fetchPrivateKey(certificateSuite, resourceCO.getPassword(), subject);
		storeHandling.deleteCertificateStoredWithPrivateKey(certificateSuite, resourceCO.getPassword(), subject);
		storeHandling.deleteCertificate(certificateSuite, subject);
		if (!storeHandling.stroePrivateKeywithCertificate(certificateSuite, resourceCO.getPassword(), chain,
				privateKey)) {
			throw new VeidblockException("Problems when processing certificate respone !");
		}
		return chain;
	}

	public PrivateKey getPrivateKey(DistinguishNameCO distinguishNameCO, String password) throws VeidblockException {

		StoreHandling storeHandling = new StoreHandling();
		PrivateKey privateKey = storeHandling.fetchPrivateKey(certificateSuite, password,
				DistinguishName.builder().build(distinguishNameCO));
		if (privateKey == null) {
			throw new VeidblockException("Could not find Private key of "+distinguishNameCO+" from store ["+certificateSuite.getStorePath()+"]!");
		}
		return privateKey;
	}

	public DistinguishNameCO toAnonymousDistinguishNameCO(ResourceCO resourceCO) {
		DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
		distinguishNameCO.setName("" + resourceCO.getResourceId());
		return distinguishNameCO;
	}

	public PublicKey getUserPublicKey(ResourceCO resourceCO,LocalCertificateManager localCertificateManager) throws VeidblockException {
		try {
			X509Certificate resourceCert = fetchCertificate(resourceCO);
			return resourceCert.getPublicKey();
		} catch (VeidblockException veidblockException) {

		}
		
		String certReq = createCertRequest(resourceCO);
		
		
		long keyUsage = CommonEnums.KEY_USAGE.DIGITAL_SIGNATURE.value() | CommonEnums.KEY_USAGE.KEY_ENCIPHERMENT.value() | CommonEnums.KEY_USAGE.DATA_ENCIPHERMENT.value();
		
		String certResponse = signCert(localCertificateManager, certReq,	keyUsage);
		X509Certificate chain[] = processCertResponse(resourceCO, certResponse);
		return chain[0].getPublicKey();		
	}
}
