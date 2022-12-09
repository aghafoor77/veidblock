package org.acreo.common.entities;

import java.util.ArrayList;
import java.util.List;

public class VeidblockConfig {
	private String validator;
	private String verifier;
	private String validatorList;
	private String validatorAddresses;
	//http://localhost:8000, http://localhost:9000
	
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	public String getVerifier() {
		return verifier;
	}
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	
	public String getValidatorList() {
		return validatorList;
	}
	public void setValidatorList(String validatorList) {
		this.validatorList = validatorList;
	}
	
	public List<String> getValidatorAddresses(){
		List<String> valList = new ArrayList<String>();
		String validators[] = validatorList.split(",");
		for(String val : validators){
			val  = val.trim();
			valList.add(val); 
		}
		return valList;
	}
}
