package org.acreo.ipv;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.sql.DataSource;

import org.acreo.common.entities.ResourceCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.database.DatabaseCretor;
import org.acreo.init.InitSetup;
import org.acreo.init.SystemInfo;
import org.acreo.ip.config.IpConfiguration;
import org.acreo.ip.service.OTPService;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ip.service.ResourceService;
import org.acreo.ip.service.RoleService;
import org.acreo.ipv.regionaldata.RegionList;
import org.acreo.ipv.regionaldata.RegionalData;
import org.acreo.ipv.resources.OrganizationResource;
import org.acreo.ipv.resources.ResourceResource;
import org.acreo.ipv.resources.RoleResource;
import org.acreo.ipv.utils.IpvOrganizationHealthCheck;
import org.acreo.ipv.utils.IpvPersonHealthCheck;
import org.acreo.ipv.utils.IpvRoleHealthCheck;
import org.acreo.ipv.utils.RegisterSuperUser;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.skife.jdbi.v2.DBI;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class IpvApplication extends Application<IpConfiguration> {
	private static final String SQL = "sql";
	private static final String ORGANIZATON_SERVICE = "Organization service";
	private static final String ROLE_SERVICE = "Role service";
	private static final String PERSON_SERVICE = "Person service";
	private String password = "";
	public static RegionList rl;
	public static void main(String[] args) throws Exception {

		new IpvApplication().run(args);
	}

	@Override
	public void run(IpConfiguration configuration, Environment environment) {

		enableCORS(environment);
		
		String url = "https://restcountries.eu/rest/v2/all";
		try {
			rl = new RegionalData().handle(url);			
		} catch (Exception e2) {
			e2.printStackTrace();
			System.err.println("Problems when fetching countries related data so please check that a file, that contains countries information, exists at following location !");
			new RegionalData().isIdentityExisits();
			System.err.println(" or the url '"+url+"' is correct !");
			System.exit(0);
			return;
		}
		// Datasource configuration
		final DataSource dataSource = configuration.getDataSourceFactory().build(environment.metrics(), SQL);
		try {
			createDB(configuration.getDataSourceFactory());
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}

		DBI dbi = new DBI(dataSource);

		// Register Health Check
		IpvOrganizationHealthCheck organizationHealthCheck = new IpvOrganizationHealthCheck(
				dbi.onDemand(OrganizationService.class));
		IpvRoleHealthCheck roleHealthCheck = new IpvRoleHealthCheck(dbi.onDemand(RoleService.class));

		IpvPersonHealthCheck personHealthCheck = new IpvPersonHealthCheck(dbi.onDemand(ResourceService.class));

		environment.healthChecks().register(ORGANIZATON_SERVICE, organizationHealthCheck);
		environment.healthChecks().register(ROLE_SERVICE, roleHealthCheck);
		environment.healthChecks().register(PERSON_SERVICE, personHealthCheck);

		URI uri;
		try {
			uri = new URI("http://localhost:9000/verify");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		IpvVerificationFilter verificationRedirection = new IpvVerificationFilter(uri,
				dbi.onDemand(ResourceService.class));

		environment.jersey().register(new AuthDynamicFeature(verificationRedirection));
		RegisterSuperUser registerSuperUser = new RegisterSuperUser(dbi.onDemand(ResourceService.class),
				dbi.onDemand(OrganizationService.class), dbi.onDemand(RoleService.class),
				dbi.onDemand(OTPService.class));
		registerSuperUser.createRoles();
		try {
			String password = "123456789";
			ResourceCO resource = registerSuperUser.createSuperUser(password );
			System.out.println("\t**************************************************************\n");
			System.err.print("\t\tSupper User Resource Id is : ");
			System.err.println("\t\t" + resource.getResourceId());
			System.err.print("\t\tSupper User username is    : ");
			System.err.println("\t\t" + resource.getUsername());
			System.err.print("\t\tSupper User password is    : ");
			System.err.println("\t\t" + password );
			//System.out.println("\t**************************************************************");
		} catch (VeidblockException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}

		// Register resources
		environment.jersey().register(new OrganizationResource(dbi.onDemand(OrganizationService.class)));
		environment.jersey().register(new RoleResource(dbi.onDemand(RoleService.class)));
		environment.jersey()
				.register(new ResourceResource(dbi.onDemand(ResourceService.class),
						dbi.onDemand(OrganizationService.class), dbi.onDemand(RoleService.class),
						dbi.onDemand(OTPService.class)));

		SystemInfo systemInfo = new SystemInfo("Identity Provider & Verification", "1.0.0",
				"RISE Acreo, AB, Kista, Sweden", "", new Date());
		InitSetup.displayTag(systemInfo);

	}

	private void enableCORS(Environment environment) {
		FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
		filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,
				"Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
		filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	}

	private void createDB(DataSourceFactory dataSourceFactory) throws Exception {
		String dbURLFull = dataSourceFactory.getUrl();
		String user = dataSourceFactory.getUser();
		String password = dataSourceFactory.getPassword();
		String databaseName = dbURLFull.substring(dbURLFull.lastIndexOf("/") + 1);
		String dbURL = dbURLFull.substring(0, dbURLFull.lastIndexOf("/"));
		new DatabaseCretor().createVeidblockDatabase(dbURL, user, password, databaseName);
	}
	@Override
	public void initialize(Bootstrap<IpConfiguration> bootstrap) {
	    bootstrap.addBundle(new SwaggerBundle<IpConfiguration>() {
	        @Override
	        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(IpConfiguration configuration) {
	            return configuration.swaggerBundleConfiguration;
	        }
	    });
	}
}
