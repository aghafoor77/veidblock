package org.acreo.clientapi.utils;

import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.acreo.clientapi.Identity;
import org.acreo.clientapi.Ledger;
import org.acreo.clientapi.security.ClientCertificateHandler;
import org.acreo.common.entities.DistinguishNameCO;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.entities.VeidSignerInfo;
import org.acreo.common.entities.lc.SmartContract;
import org.acreo.common.entities.lc.SmartContract.SECURITY_LEVEL;
import org.acreo.common.entities.lc.TransactionBlockCO;
import org.acreo.common.entities.lc.TransactionCO;
import org.acreo.common.entities.lc.TransactionHeaderCO;
import org.acreo.common.entities.lc.Transactions;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.security.certificate.CertificateSuite;
import org.acreo.security.crypto.CryptoPolicy;
import org.acreo.security.pkcs.PKCS7Manager;
import org.acreo.security.utils.PEMStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionHandler {

	final static Logger logger = LoggerFactory.getLogger(TransactionHandler.class);

	public TransactionBlockCO createTransaction(ResourceCO resourceCO, TransactionHeaderCO transactionHeaderCO,
			String receivers[], Object payload,ClientAuthenticator authenticator) throws VeidblockException {
		logger.debug("Preparing payload based on the instructions specified in smart contract !");
		SmartContract smartContract = null;
		try {
			smartContract = new ObjectMapper().readValue(transactionHeaderCO.getSmartcontract(), SmartContract.class);
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		TransactionBlockCO transactionBlockCO = new TransactionBlockCO();
		String dateTime = simpleDateFormat.format(new Date());
		transactionBlockCO.setCreationTime(dateTime);
		logger.debug("Payload creation time : "+dateTime);

		logger.debug("Required crypto operation, specified in smart contract : "+smartContract.getSecurityLevel().toString());
		transactionBlockCO.setCryptoOperationsOnPayload(smartContract.getSecurityLevel().toString());

		CertificateSuite certificateSuite = new CertificateSuite(resourceCO.getResourceId() + "" + "", 3);
		CryptoPolicy cryptoPolicy = new CryptoPolicy();

		// Serialize Payload
		String serPayload = null;
		if (Objects.isNull(payload)) {
			throw new VeidblockException("Paylaod is null !");
		}
		try {
			serPayload = new ObjectMapper().writeValueAsString(payload);
		} catch (JsonProcessingException e) {
			throw new VeidblockException(e);
		}
		PKCS7Manager pkcs7Manager = new PKCS7Manager(cryptoPolicy, certificateSuite);

		if (smartContract.getSecurityLevel() == SECURITY_LEVEL.DIGITAL_SIGNATURE) {
			logger.debug("Creating PKCS7 Signed Data of payload !");
			ClientCertificateHandler certificateHandler = new ClientCertificateHandler(certificateSuite);
			X509Certificate cert = certificateHandler.fetchCertificate(resourceCO);
			List<VeidSignerInfo> veidSignerInfoList = new ArrayList<VeidSignerInfo>();
			VeidSignerInfo veidSignerInfo = new VeidSignerInfo();
			logger.debug("Fetching certificate chain !");
			veidSignerInfo.setChain(certificateHandler.fetchX509CertificateChain(resourceCO));
			logger.debug("Fetching private key !");
			DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
			distinguishNameCO.setName(resourceCO.getResourceId() + "");
			veidSignerInfo.setPrivateKey(certificateHandler.getPrivateKey(distinguishNameCO, resourceCO.getPassword()));
			veidSignerInfoList.add(veidSignerInfo);
			serPayload = pkcs7Manager.generatePKCS7Signed(veidSignerInfoList, serPayload.getBytes());
			logger.debug("Successfully created PKCS7 Signed Data of payload !");

		} else if (smartContract.getSecurityLevel() == SECURITY_LEVEL.ENVELOPED) {
			logger.debug("Creating PKCS7 Enveloped Data of payload !");
			X509Certificate[] recCerts = fetchRecipientsCertificate(resourceCO, receivers, authenticator);
			serPayload = pkcs7Manager.generateEnvelopedData(recCerts, serPayload.getBytes());
			logger.debug("Successfully created PKCS7 Enveloped Data of payload !");

		} else if (smartContract.getSecurityLevel() == SECURITY_LEVEL.DIGITAL_SIGNATURE_ENVELOPED) {
			logger.debug("Creating PKCS7 Signed and Enveloped Data of payload !");
			ClientCertificateHandler certificateHandler = new ClientCertificateHandler(certificateSuite);
			X509Certificate cert = certificateHandler.fetchCertificate(resourceCO);
			
			X509Certificate[] recCerts = fetchRecipientsCertificate(resourceCO, receivers, authenticator);
			DistinguishNameCO distinguishNameCO = new DistinguishNameCO();
			distinguishNameCO.setName(resourceCO.getResourceId() + "");
			serPayload = pkcs7Manager.generatePKCS7SignedAndEnveloped(
					certificateHandler.fetchX509CertificateChain(resourceCO),
					certificateHandler.getPrivateKey(distinguishNameCO, resourceCO.getPassword()),
					recCerts ,serPayload.getBytes());
			logger.debug("Successfully created PKCS7 Signed and Enveloped Data of payload !");
			
		} else if (smartContract.getSecurityLevel() == SECURITY_LEVEL.NONE) {
			logger.debug("Clear payload !");
		}

		transactionBlockCO.setPayload(serPayload);

		transactionBlockCO.setPayloadType(payload.getClass().getName());
		StringBuffer receiverBuff = new StringBuffer();
		if (!Objects.isNull(receivers)) {
			for (String rec : receivers) {
				receiverBuff.append(rec + "|");
			}
		} else {
			receiverBuff.append("");
		}
		transactionBlockCO.setReceiver(receiverBuff.toString());
		transactionBlockCO.setScope(smartContract.getScope().toString());
		transactionBlockCO.setSender(resourceCO.getResourceId() + "");
		dateTime = simpleDateFormat.format(new Date());
		transactionBlockCO.setSignedDate(dateTime );
		logger.debug("Signed transaction at "+dateTime );
		transactionBlockCO.setSignedBy("" + resourceCO.getResourceId());
		return transactionBlockCO;
	}

	private X509Certificate[] fetchRecipientsCertificate(ResourceCO resourceCO, String receivers[],ClientAuthenticator authenticator)
			throws VeidblockException {
		logger.debug("Fetching resipients certificates from chainblock !");
		X509Certificate recx509[] = new X509Certificate[receivers.length];
		Ledger ledger = Ledger.builder().resource(resourceCO).build(authenticator);
		TransactionHeaderCO transactionHeader = ledger.getTransactionHeaderByName(Identity.PUB_CERT);
		if (Objects.isNull(transactionHeader)) {
			logger.error("Certificate chain does not exisits, please try later !");
			throw new VeidblockException("Certificate chain does not exisits, please try later !");
		}

		int count = 0;
		for (String rec : receivers) {
			Transactions transactions = ledger.getTransactionBySenderInChain(transactionHeader.getRef(), rec);
			if (Objects.isNull(transactions) || transactions.size() == 0) {
				logger.error("Coluld not find recipient's certificate. The transaction is invalid !");
				throw new VeidblockException(
						"Coluld not find recipient's certificate. The transaction is invalid !");
			}
			TransactionCO certTrans = transactions.get(transactions.size() - 1);
			PEMStream pemStream = new PEMStream();
			X509Certificate x509Cert = null;
			String encodedIssuerCert;
			try {
				encodedIssuerCert = new ObjectMapper().readValue(certTrans.getPayload(), String.class);
				x509Cert = pemStream.pem2x509Cert(encodedIssuerCert);
				recx509[count] = x509Cert;
				count++;
			} catch (Exception e) {
				throw new VeidblockException(e);
			}
		}
		logger.debug("Successfully feteched resipients certificates from chainblock !");
		logger.debug("Total feteched certificates are : "+recx509.length);
		return recx509;
	}

}
