package org.acreo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VeidblockLogger {

	
	public static void main(String[] args) throws ParseException {
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date date = simpleDateFormat.parse("2017-11-11 05:59:54");
		Date now =  simpleDateFormat.parse("2017-11-11 05:59:54");
		
		System.out.println("Token expiry date : "+simpleDateFormat.format(date));
		System.out.println("Current date      : "+simpleDateFormat.format(now));
		
		System.out.println(now.after(date));
		
	}

	public static void displayTag(String serviceName, String version) {

		String title = "mService : " + serviceName + " <" + version + ">";
		char tag = '*';
		char pipe = '*';
		char inner = ' ';
		if(title.length()% 2 == 0){
			title = title +inner;
		}
		int dots = 61;
		if (title.length()+8 > dots) {
			dots = title.length() + 8;
		} 

		System.out.print("\t");
		for (int i = 0; i < dots + 1; i++)
			System.out.print(tag);
		System.out.println("");

		System.out.print("\t");
		System.out.print(pipe);
		for (int i = 0; i < dots-1; i ++)
			System.out.print(inner);
		System.out.println(pipe);

		
		System.out.print("\t");
		int spaces = dots-title.length();
		System.out.print(pipe);
		for (int i = 0; i < (spaces/2) ; i++)
			System.out.print(inner);
		System.out.print(title);
		
		for (int i = 0; i < (spaces/2)-1 ; i++)
			System.out.print(inner);
		System.out.println(pipe);
		
		
		System.out.print("\t");
		System.out.print(pipe);
		for (int i = 0; i < dots-1; i ++)
			System.out.print(inner);
		System.out.println(pipe);

		System.out.print("\t");
		for (int i = 0; i < dots + 1; i++)
			System.out.print(tag);
		System.out.println("");


	}	
}
