package org.acreo.common.utils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.http.client.HttpClient;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.setup.Environment;

public class PeerConnection extends HttpClientConfiguration{
	@NotEmpty
	private String host;

	@Min(1)
	@Max(65535)
	private int port;
	
	@JsonProperty
	public String getHost() {
		return host;
	}

	@JsonProperty
	public void setHost(String host) {
		this.host = host;
	}

	@JsonProperty
	public int getPort() {
		return port;
	}

	@JsonProperty
	public void setPort(int port) {
		this.port = port;
	}
	
	public RestClient build(Environment environment) {
		final HttpClient httpClient = new HttpClientBuilder(environment).using(this)
                .build("IpvClient");		
		RestClient client = new RestClient(httpClient, this.host+":"+this.port);
		return client;
	}
}