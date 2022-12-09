package org.acreo.init;

import org.acreo.common.Representation;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class InitSetup {

	final static Logger logger = LoggerFactory.getLogger(InitSetup.class);

	public static void build(final RestClient restIpvClient,
			LocalCredentialManager localCredentialManager) {
		try {
			//manageResources();
			init(restIpvClient, new VeidblockIO().getMyIdentity(), localCredentialManager);
		} catch (VeidblockException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void displayTag(SystemInfo systemInfo) {

		String title = "mService : " + systemInfo.getModule() + " [ " + systemInfo.getVersion() + " ]";

		char tag = '*';
		char pipe = '*';
		char inner = ' ';
		if (title.length() % 2 != 0) {
			title = title + inner;
		}
		int largestLine = 0;
		String temp = systemInfo.getOrgnization();
		if (temp.length() > title.length()) {
			largestLine = temp.length();
		} else {
			largestLine = title.length();
		}

		int dots = 60;
		if (largestLine + 8 > dots) {
			dots = largestLine + 8;
		}

		System.out.print("\t");
		for (int i = 0; i < dots + 1; i++)
			System.out.print(tag);
		System.out.println("");

		System.out.print("\t");
		System.out.print(pipe);
		for (int i = 0; i < dots - 1; i++)
			System.out.print(inner);
		System.out.println(pipe);

		System.out.print("\t");
		int spaces = dots - title.length();
		System.out.print(pipe);
		for (int i = 0; i < (spaces / 2); i++)
			System.out.print(inner);
		System.out.print(title);

		for (int i = 0; i < (spaces / 2)-1; i++)
			System.out.print(inner);
		System.out.println(pipe);

		System.out.print("\t");
		String org = systemInfo.getOrgnization();
		if (org.length() % 2 == 0) {
			org = org + " ";
		}
		spaces = dots - org.length();
		System.out.print(pipe);
		for (int i = 0; i < (spaces / 2); i++)
			System.out.print(inner);

		System.out.print(org);

		for (int i = 0; i < (spaces / 2); i++)
			System.out.print(inner);
		System.out.println(pipe);

		System.out.print("\t");
		System.out.print(pipe);
		for (int i = 0; i < dots - 1; i++)
			System.out.print(inner);
		System.out.println(pipe);

		System.out.print("\t");
		for (int i = 0; i < dots + 1; i++)
			System.out.print(tag);
		System.out.println("");
	}

	private static boolean init(final RestClient restIpvClient, final MyIdentity myIdentity, LocalCredentialManager localCredentialManager)
			throws VeidblockException {
		ObjectMapper objectMapper = new ObjectMapper();
		VeidblockIO veidblockIO = new VeidblockIO();
		if (!veidblockIO.isIdentityExisits()) {
			logger.info("Checking Authentication mService registration status !");
			Representation representation = myIdentity.register(restIpvClient, localCredentialManager.getPassword());

			if (representation.getCode() == -1) {
				logger.error("Registration error : " + representation.getBody().toString() + ", trying again !");
				if (representation.getBody().toString().equals("Read timed out")) {
					throw new VeidblockException("Registration error : " + representation.getBody().toString());
				} else {
					throw new VeidblockException("Registration error : " + representation.getBody().toString());
				}
			}

			if (!(representation.getCode() == 200 || representation.getCode() == 409)) {
				logger.error("Problems when registering Auth-mS. " + "Error: [" + representation.getCode() + "] !");
				logger.error(representation.getBody() + "!");
				System.exit(0);
			}

			logger.info("Authentication mService registration is OK !");
			
			ResourceCO resourceCO;
			try {
				resourceCO = new ObjectMapper().readValue(representation.getBody().toString(), ResourceCO.class);
				resourceCO.setPassword(localCredentialManager.getPassword()); 
				veidblockIO.saveResourceCO(new ObjectMapper().writeValueAsString(resourceCO) );
			} catch (Exception e) {
				logger.info("Problems when saving identity inforamtion locally !");
				return false;
			} 
			
		} else {
			logger.info("Authentication mService already register !");
		}
		return true;
	}
	/*private static void manageResources() {
		try {
			File f = new File("resources");
			if (!f.exists()) {
				f.mkdir();
			}

			f = new File(f.getAbsolutePath() + File.separator + "credentials");
			if (!f.exists()) {
				f.mkdir();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	/*public static String getCredentialsPath(){
		File f = new File("resources");
		return f.getAbsolutePath() + File.separator + "credentials";
	}
	
	public static String getResourcesPath(){
		File f = new File("resources");
		return f.getAbsolutePath();
	}*/
	
}
