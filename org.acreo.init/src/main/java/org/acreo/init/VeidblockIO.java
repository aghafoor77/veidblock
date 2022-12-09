package org.acreo.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.acreo.common.exceptions.VeidblockException;

public class VeidblockIO {

	public boolean saveResourceCO(String resourceCOinJson) throws VeidblockException {
		File file = new File("myidentity.env");
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(resourceCOinJson.getBytes());
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
		return true;
	}

	public String readResourceCO() throws VeidblockException {
		File file = new File("myidentity.env");
		try {
			FileInputStream fos = new FileInputStream(file);
			byte array[] = new byte[(int) file.length()];
			fos.read(array);
			return new String(array);
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}

	public boolean isIdentityExisits() {
		File file = new File("myidentity.env");
		return file.exists();
	}

	public String readMyIdentity() throws VeidblockException {

		File file = new File("identity.props");
		
		if (!file.exists()) {
			throw new VeidblockException("Identity property file does not exisit ! It should be on path :"+file.getAbsolutePath());
		}
		try {
			FileInputStream fos = new FileInputStream(file);
			byte array[] = new byte[(int) file.length()];
			fos.read(array);
			return new String(array);
		} catch (Exception e) {
			throw new VeidblockException(e);
		}
	}

	public boolean isMyIdentityExisits() {
		File file = new File("identity.props");
		return file.exists();
	}

	public MyIdentity getMyIdentity() throws VeidblockException {

		File file = new File("identity.props");
		if (!file.exists()) {
			throw new VeidblockException("Identity property file does not exisit ! It should be on path :"+file.getAbsolutePath());
		}

		FileInputStream fos;
		try {
			fos = new FileInputStream(file);
			Properties props = new Properties();
			props.load(fos);
			MyIdentity myIdentity = new MyIdentity(props);
			return myIdentity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
