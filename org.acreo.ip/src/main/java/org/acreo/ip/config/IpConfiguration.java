package org.acreo.ip.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class IpConfiguration extends Configuration {
	private static final String DATABASE = "database";
	private static final String VERIFICATION = "verification";

	@Valid
	@NotNull
	private DataSourceFactory dataSourceFactory = new DataSourceFactory();

	@Valid
	@NotNull
	private Verification verification = new Verification ();
	
	
	@JsonProperty(DATABASE)
	public DataSourceFactory getDataSourceFactory() {
		return dataSourceFactory;
	}

	@JsonProperty(DATABASE)
	public void setDataSourceFactory(final DataSourceFactory dataSourceFactory) {
		this.dataSourceFactory = dataSourceFactory;
	}
	
	@JsonProperty(VERIFICATION)
	public Verification getVerification() {
		return verification;
	}

	@JsonProperty(VERIFICATION)
	public void setverification(final Verification verification) {
		this.verification = verification;
	}

	@JsonProperty("swagger")
	public SwaggerBundleConfiguration swaggerBundleConfiguration;
	
}