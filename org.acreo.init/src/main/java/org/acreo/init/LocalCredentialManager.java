package org.acreo.init;

public class LocalCredentialManager {
	
	private String password = "11111111";
	
	public LocalCredentialManager(String password){
		this.password= password ;
	}
	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
