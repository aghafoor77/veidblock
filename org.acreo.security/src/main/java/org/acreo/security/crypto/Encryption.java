package org.acreo.security.crypto;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.acreo.common.exceptions.VeidblockException;
import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.apache.commons.codec.binary.Hex;


public class Encryption {

	private CryptoPolicy cryptoPolicy =null;
	public Encryption(CryptoPolicy cryptoPolicy ){
		this.cryptoPolicy= cryptoPolicy;
	}
	public byte[] encrypDB(String password, String fieldValue) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecretKey secret = cryptoUtils.fetchDBSecretKey(password);
		return cryptoUtils.encryptDecrypt(secret, fieldValue, Cipher.ENCRYPT_MODE);
	}

	public byte[] decryptDB(String password, String encryptedFieldValue) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecretKey secret = cryptoUtils.fetchDBSecretKey(password);				
		return cryptoUtils.encryptDecrypt(secret, encryptedFieldValue, Cipher.DECRYPT_MODE);
	}

	public byte [] encrypDB(String password, byte[] fieldValue) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecretKey secret = cryptoUtils.fetchDBSecretKey(password);
		return cryptoUtils.encryptDecrypt(secret, fieldValue, Cipher.ENCRYPT_MODE);
	}

	public byte[] decryptDB(String password, byte[] encryptedFieldValue) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecretKey secret = cryptoUtils.fetchDBSecretKey(password);
		
		return cryptoUtils.encryptDecrypt(secret, encryptedFieldValue, Cipher.DECRYPT_MODE);
	}

	public byte[] encrypt(byte[] symmetricKey, byte data[], ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecurityProperties securityProperties = new SecurityProperties();
		SecretKey secret = securityProperties.generateKey(symmetricKey, cryptoPolicy.getEncAlgorithm());
		return cryptoUtils.encryptDecrypt(secret, data, Cipher.ENCRYPT_MODE);
	}

	public byte[] decrypt(byte[] symmetricKey, byte data[], ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		CryptoUtils cryptoUtils = new CryptoUtils(cryptoPolicy);
		SecurityProperties securityProperties = new SecurityProperties();
		SecretKey secret = securityProperties.generateKey(symmetricKey, cryptoPolicy.getEncAlgorithm());
		return cryptoUtils.encryptDecrypt(secret, data, Cipher.DECRYPT_MODE);
	}

	
	public byte [] encrypt(PublicKey publicKey, byte data[], ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedData = rsaCipher.doFinal(data);
			
			return performEncoding(encryptedData, encoding);
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	public byte[] encrypt(PrivateKey privateKey, byte data[], ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.ENCRYPT_MODE, privateKey);
			byte[] encryptedData = rsaCipher.doFinal(data);
			
			return performEncoding(encryptedData, encoding);
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	public byte[] encrypt(Certificate certificate, byte data[], ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());
			byte[] encryptedData = rsaCipher.doFinal(data);
			return performEncoding(encryptedData, encoding);
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	public String decrypt(String encryptedText, ENCODING_DECODING_SCHEME encoding) {
		return null;
	}

	public String decrypt(byte[] symmetricKey, String encryptedText, ENCODING_DECODING_SCHEME encoding) {
		return null;
	}

	public byte[] decrypt(PublicKey publicKey, byte[] encryptedText, ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.DECRYPT_MODE, publicKey);
			return rsaCipher.doFinal(performDecoding(encryptedText, encoding));			
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	public byte [] decrypt(PrivateKey privateKey, byte[] encryptedText, ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
			return rsaCipher.doFinal(performDecoding(encryptedText, encoding));
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	public byte[] decrypt(Certificate certificate, byte[] encryptedText, ENCODING_DECODING_SCHEME encoding) throws VeidblockException {
		try {
			// Create a cipher using that key to initialize it
			Cipher rsaCipher = Cipher.getInstance(cryptoPolicy.getrSAEncryptionScheme());
			rsaCipher.init(Cipher.DECRYPT_MODE, certificate.getPublicKey());
			return  rsaCipher.doFinal(performDecoding(encryptedText, encoding));
			
		} catch (Exception exp) {
			throw new VeidblockException(exp);
		}
	}

	private byte[] performEncoding(byte[] data, ENCODING_DECODING_SCHEME encoding) {
		switch (encoding) {
		case BASE64:
			return Base64.getEncoder().encode(data);
		case HEX:
			return new String(Hex.encodeHex(data)).getBytes();
		case NONE:
			return data;

		}
		return data;
	}

	private byte[] performDecoding(byte[] data, ENCODING_DECODING_SCHEME encoding) throws Exception {
		switch (encoding) {
		case BASE64:
			return Base64.getDecoder().decode(data);
		case HEX:
			return Hex.decodeHex(new String(data).toCharArray());
		case NONE:
			return data;

		}
		return data;
	}	
}