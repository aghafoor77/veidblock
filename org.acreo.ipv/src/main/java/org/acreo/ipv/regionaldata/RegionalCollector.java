package org.acreo.ipv.regionaldata;

import java.io.IOException;
import java.util.Objects;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import sun.misc.BASE64Encoder;

		 
		public class RegionalCollector {

			public CountryList get(String url) {
				try {
					CloseableHttpClient httpClient = HttpClients.createDefault();
					HttpGet request = new HttpGet(url);
					request.addHeader("charset", "UTF-8");
					request.addHeader("content-type", "application/json;charset=UTF-8");
					CloseableHttpResponse httpResponse = httpClient.execute(request);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						String body = EntityUtils.toString(httpResponse.getEntity());
						if(Objects.isNull(body)){
							System.err.println("Problem when fetching resource data !");
							return null;					
						}
						try {
							CountryList list = new ObjectMapper().readValue(body, CountryList.class);
							return list;
						} catch (IOException e) {
							System.err.println("URL : "+url);
							System.err.println("Problem when fetching data !"+e.getMessage());
							return null;
						}
						
						
					} else {
						System.err.println("URL : "+url);
						System.err.println("Problem when fetching data ! Code is "+httpResponse.getStatusLine().getStatusCode());
						return null;
					}
				} catch (IOException e) {
					System.err.println("URL : "+url);
					System.err.println("Problem when fetching data !"+e.getMessage());
					return null;
				}
			}

			
		}
