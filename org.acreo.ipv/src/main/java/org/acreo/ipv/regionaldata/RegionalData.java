package org.acreo.ipv.regionaldata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RegionalData {
	public static void main(String[] args) {
		String url = "https://restcountries.eu/rest/v2/all";
		try {
			RegionList rl = new RegionalData().handle(url);
			System.out.println(rl.size());
		} catch (Exception e2) {
			e2.printStackTrace();
			
			System.err.println("Problems when fetching countries related data so please check that a file, that contains countries information, exists at following location !");
			new RegionalData().isIdentityExisits();
			System.err.println(" or the url '"+url+"' is correct !");
			System.exit(0);
			return;
		}
		
	}

	public RegionList handle(String url) throws Exception {
		try {
			if (isIdentityExisits()) {
				return read();
			}
			RegionalCollector regionalCollector = new RegionalCollector();
			CountryList cl = regionalCollector.get(url);
			RegionList regionList = new RegionList();
			for (Country c : cl) {
				Region region = new Region();
				region.setName(c.getName());
				if (!c.getCallingCodes().isEmpty()) {
					region.setCallingCode(c.getCallingCodes().get(0));
				}
				region.setFlag(c.getFlag());
				regionList.add(region);
			}
			save(regionList);
			return regionList;
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public boolean save(RegionList regionList) throws Exception {
		File file = new File("countries.json");
		try {
			ObjectOutputStream fos = new ObjectOutputStream( new FileOutputStream(file));
			fos.writeObject(regionList);
		} catch (Exception e) {
			throw new Exception(e);
		}
		return true;
	}

	public RegionList read() throws Exception {
		File file = new File("countries.json");
		try {
			ObjectInputStream fos = new ObjectInputStream(new  FileInputStream(file));
			RegionList regionList = (RegionList)fos.readObject();
			return regionList;

		} catch (Exception e) {
			e.printStackTrace();
			file.delete(); 
			throw new Exception("Deleted existing files and now please try again !");
		}
	}

	public boolean isIdentityExisits() {
		File file = new File("countries.json");
		System.out.println("Countries file path is : " + file.getAbsolutePath());
		return file.exists();
	}

}