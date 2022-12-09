package org.acreo.security.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.acreo.common.exceptions.VeidblockException;
import org.acreo.security.crypto.CryptoStructure.ENCODING_DECODING_SCHEME;
import org.acreo.security.crypto.CryptoStructure.HASH_ALGO;

public class Hashing {
	
	private CryptoPolicy cryptoPolicy = new CryptoPolicy();
	private Hashing (){
		
	}
	public Hashing(CryptoPolicy cryptoPolicy ){
		this.cryptoPolicy = cryptoPolicy;
	}
		
	public String generateEncodedHash(String data){
		return null;
	}
	
	public boolean verifyEncodedHash(String encodedHash, String value[]){
		return false;
	}
	
	public byte [] generateHash(byte value[]) throws VeidblockException{
		try {
	        MessageDigest digest = MessageDigest.getInstance(cryptoPolicy.getHashAlgorithm().value());
	        return digest.digest(value);	        
	    } catch (NoSuchAlgorithmException exp) {
	    	throw new VeidblockException(exp);
	    }
	}
	
	public boolean verifyHash(byte [] oldhash, byte value[]) throws VeidblockException{
		byte hashed [] = generateHash(value);
		return MessageDigest.isEqual(oldhash, hashed);
	}
	
	public String calculateHash(byte data[], HASH_ALGO algo, ENCODING_DECODING_SCHEME scheme ){
		return null;
	}
	
	public boolean verifyHash(String hash, HASH_ALGO algo, ENCODING_DECODING_SCHEME scheme ){
		return false;
	}
}
