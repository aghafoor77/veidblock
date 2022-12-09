package org.acreo.security.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.acreo.common.entities.PersonCredentials;
import org.acreo.common.exceptions.VeidblockException;

public class CryptoUtils {
	
	private CryptoPolicy cryptoPolicy = null;
	public CryptoUtils (CryptoPolicy cryptoPolicy){
		this.cryptoPolicy = cryptoPolicy ;
	}
	
	public SecretKey fetchDBSecretKey(String password) throws VeidblockException{
		SecurityProperties securityProperties = new SecurityProperties();
		PersonCredentials personCredentials = new PersonCredentials();
		personCredentials.setPassword(password);
		
		byte [] key = securityProperties.getDbKey(personCredentials);
		return securityProperties.generateKey(key, cryptoPolicy.getEncAlgorithm());				
	}

	@SuppressWarnings("restriction")
	public byte[] encryptDecrypt(SecretKey secret, byte [] fieldValue, int mode) throws VeidblockException {
		try {
			Cipher cipher = Cipher.getInstance(cryptoPolicy.getCipherInstanceType());
			SecurityProperties securityProperties = new SecurityProperties();
			cipher.init(mode, secret, new IvParameterSpec(cryptoPolicy.getIv()));
			byte[] toCrypto = null;
			String strCiphertext = null;
			if (Cipher.DECRYPT_MODE == mode) {
				toCrypto = Base64.getDecoder().decode(new String (fieldValue, "UTF-8"));
				//toCrypto = new BASE64Decoder().decodeBuffer(new String (fieldValue, "UTF-8"));
			} else {
				toCrypto = fieldValue;
			}
			byte[] ciphertext = cipher.doFinal(toCrypto);
			if (Cipher.ENCRYPT_MODE == mode) {
				strCiphertext = new String(Base64.getEncoder().encode(ciphertext), "UTF-8");
				//strCiphertext = new BASE64Encoder().encode(ciphertext);
			} else {
				strCiphertext = new String(ciphertext);
			}
			
			return strCiphertext.getBytes();
			

		} catch (Exception e) {
			throw new VeidblockException(e);
		}

	}

	public byte[] encryptDecrypt(SecretKey secret, String fieldValue, int mode) throws VeidblockException {
		try {
			Cipher cipher = Cipher.getInstance(cryptoPolicy.getCipherInstanceType());
			cipher.init(mode, secret, new IvParameterSpec(cryptoPolicy.getIv()));
			byte[] toCrypto = null;
			String strCiphertext = null;
			if (Cipher.DECRYPT_MODE == mode) {
				toCrypto = Base64.getDecoder().decode(fieldValue);
				//toCrypto = new BASE64Decoder().decodeBuffer(fieldValue);
			} else {
				toCrypto = fieldValue.getBytes();
			}
			byte[] ciphertext = cipher.doFinal(toCrypto);
			if (Cipher.ENCRYPT_MODE == mode) {
				strCiphertext  = new String(Base64.getEncoder().encode(ciphertext), "UTF-8");
				//strCiphertext = new BASE64Encoder().encode(ciphertext);
				ciphertext = strCiphertext.getBytes("UTF-8"); 
			} 
			return ciphertext;

		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}
}
