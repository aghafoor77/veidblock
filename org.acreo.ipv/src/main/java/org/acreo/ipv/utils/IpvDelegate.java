package org.acreo.ipv.utils;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.acreo.activation.ActivationEmail;
import org.acreo.common.entities.ResourceCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.IpUtils;
import org.acreo.ip.entities.OTP;
import org.acreo.ip.entities.Organization;
import org.acreo.ip.entities.Resource;
import org.acreo.ip.service.OTPService;
import org.acreo.ip.service.OrganizationService;
import org.acreo.ip.service.ResourceService;
import org.acreo.ip.service.RoleService;
import org.acreo.security.crypto.PwdBasedEncryption;
import org.acreo.security.utils.SGen;
import org.eclipse.jetty.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class IpvDelegate {

	private final ResourceService resourceService;
	private final OrganizationService organizationService;
	private final RoleService roleService;
	private OTPService otpService = null;
	// private CryptoPolicy cryptoPolicy;
	// private String password;

	public IpvDelegate(ResourceService resourceService, OrganizationService organizationService,
			RoleService roleService, OTPService otpService) {
		this.resourceService = resourceService;
		this.organizationService = organizationService;
		this.roleService = roleService;
		this.otpService = otpService;
		// String filename= "users.txt";
		// System.out.println("Data is in : "+new
		// File(filename).getAbsolutePath());
		// this.cryptoPolicy = cryptoPolicy;
		// this.password = password;
	}

	public List<Resource> getResources(int rtype) {
		System.out.println("");
		List<Resource> resources = resourceService.getResources(rtype);
		List<Resource> resourceWithOrganization = new ArrayList<Resource>();
		for (Resource resource : resources) {
			resource.setRoles(roleService.getRoles(resource.getResourceId()));
			try {
				PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
				resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
			} catch (VeidblockException e) {
				throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
						HttpStatus.INTERNAL_SERVER_ERROR_500);
			}
			resourceWithOrganization.add(resource);

		}
		return resourceWithOrganization;
	}

	public List<Resource> getResources() {
		List<Resource> resources = resourceService.getResources();
		List<Resource> resourceWithOrganization = new ArrayList<Resource>();
		for (Resource resource : resources) {

			resource.setRoles(roleService.getRoles(resource.getResourceId()));
			try {
				PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
				resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
			} catch (VeidblockException e) {
				throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
						HttpStatus.INTERNAL_SERVER_ERROR_500);
			}
			resourceWithOrganization.add(resource);
		}
		return resourceWithOrganization;
	}

	public Resource getResource(final Long identity, int rtype) {

		Resource resource = null;

		resource = resourceService.getResource(identity, rtype);

		if (Objects.isNull(resource)) {
			return null;
		}

		resource.setRoles(roleService.getRoles(resource.getResourceId()));
		try {
			PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
			resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
		} catch (VeidblockException e) {
			throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
					HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		return resource;
	}

	public List<Resource> getMyRegisteredResource(final Long identity) {

		List<Resource> resources = resourceService.getMyRegisteredResource(identity);
		List<Resource> resourceWithOrganization = new ArrayList<Resource>();
		if (Objects.isNull(resources)) {
			return null;
		}

		for (Resource resource : resources) {

			resource.setRoles(roleService.getRoles(resource.getResourceId()));
			try {
				PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
				resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
			} catch (VeidblockException e) {
				throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
						HttpStatus.INTERNAL_SERVER_ERROR_500);
			}
			resourceWithOrganization.add(resource);
		}
		return resourceWithOrganization;
	}

	public Resource getResource(final String identity, int rtype) {

		Resource resource = null;
		try {
			resource = resourceService.getResource(Long.parseLong(identity), rtype);

		} catch (Exception exp) {

			resource = resourceService.getResource(identity, rtype);
		}

		if (Objects.isNull(resource)) {
			return null;
		}

		resource.setRoles(roleService.getRoles(resource.getResourceId()));
		try {
			PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
			resource.setPassword(new String(pwdBasedEncryption.decryptDB(resource.getPassword())));
		} catch (VeidblockException e) {
			throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
					HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		return resource;
	}

	public Resource createResource(final Resource resource, final boolean isAutheticated) {
		IpUtils ipUtils = new IpUtils();
		Resource fetchedResource = resourceService.getResource(resource.getUsername(),
				Integer.parseInt(resource.getRtype()));

		if (fetchedResource != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				fetchedResource.setPassword("");
				IpvUtils ipvUtils = new IpvUtils();
				ResourceCO dto = ipvUtils.toResourceDTO(fetchedResource);
				Response response = Response.status(HttpStatus.CONFLICT_409)
						.entity(objectMapper.writeValueAsString(dto)).build();
				throw new WebApplicationException(response);
			} catch (JsonProcessingException e) {
				throw new WebApplicationException("Internal server error when creating resource !",
						HttpStatus.INTERNAL_SERVER_ERROR_500);
			}
		}
		// If user is not registered then this mean that user himself is trying
		// to register otherwise admin is trying to register

		if (isAutheticated) {
			String password = new SGen().nextString(12);
			resource.setPassword(password);
		}

		String newPassword = resource.getPassword();
		try {
			PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
			resource.setPassword(new String(pwdBasedEncryption.encrypDB(resource.getPassword())));
		} catch (VeidblockException e) {
			throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
					HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		Resource result = resourceService.createResource(resource);
		result.setPassword("NOT-ALLOWED");
		if (isAutheticated) {
			// Send newPassword in am email to the resource
			String otp = new SGen().nextString(12);
			String resourceId = result.getResourceId() + "";
			Date sentDate = new Date();
			int expStatus = 0;
			OTP otpEntity = new OTP(otp, resourceId, sentDate, expStatus);
			String actCode = Base64.getEncoder().encodeToString((resourceId + ":" + otp).getBytes());
			otpService.createOTP(otpEntity);
			String urlBase = "http://localhost:8000/";
			String temp = "Your user id is "+resourceId+" and password is "+ newPassword+"\nFor activation please click on the folloiwng link or copy and paste it in the browser. \n";
			String url = temp + urlBase + "resource/user/act/" + actCode;

			String to = result.getEmail();
			final String from = "veidblock@gmail.com";
			final String password = "Agha1234_";
			try {
				new ActivationEmail().sendEmail(from, to, password, url);
			} catch (Exception e) {
				System.out.println(
						"Currently we are experiencing problems in email pushing therefore you may activate user's account by pasting foloiwng URL in the browser !");
				System.out.println(url);
				System.out.println("Thank you !!!");
				/*
				 * e.printStackTrace(); throw new WebApplicationException(
				 * "Internal server error when sending activation email. Temporary password is "
				 * + newPassword +
				 * " and please forward folloiwng URL to user for activation : "
				 * + url + ") !", HttpStatus.INTERNAL_SERVER_ERROR_500);
				 */
			}
		} else {
			// Don't need to send password
			String otp = new SGen().nextString(12);
			String resourceId = result.getResourceId() + "";
			Date sentDate = new Date();
			int expStatus = 0;
			OTP otpEntity = new OTP(otp, resourceId, sentDate, expStatus);
			String actCode = Base64.getEncoder().encodeToString((resourceId + ":" + otp).getBytes());
			otpService.createOTP(otpEntity);
			String urlBase = "http://localhost:8000/";
			String url = urlBase + "resource/user/act/" + actCode;
			System.out.println(url);

			String to = result.getEmail();
			final String from = "veidblock@gmail.com";
			final String password = "Agha1234_";
			try {
				new ActivationEmail().sendEmail(from, to, password, url);
			} catch (Exception e) {
				System.out.println(
						"Currently we are experiencing problems in email pushing therefore you may activate user's account by pasting foloiwng URL in the browser !");
				System.out.println(url);
				System.out.println("Thank you !!!");
				/*
				 * e.printStackTrace(); throw new WebApplicationException(
				 * "Internal server error when sending activation email. Temporary password is "
				 * + newPassword +
				 * " and please forward folloiwng URL to user for activation : "
				 * + url + ") !", HttpStatus.INTERNAL_SERVER_ERROR_500);
				 */
			}

		}
		return result;
	}

	/*
	 * private void createFile(String username, String resourceId, String
	 * password){ try { String filename= "users.txt"; FileWriter fw = new
	 * FileWriter(filename,true); //the true will append the new data
	 * fw.write(username+" = "+resourceId+" = "+password+"\n");//appends the
	 * string to the file fw.close(); } catch(IOException ioe) {
	 * System.err.println("IOException: " + ioe.getMessage()); } }
	 */

	public Resource editResource(final Resource resource, final String username) {

		/*
		 * Organization org =
		 * organizationService.editOrganization(resource.getOrganization());
		 * resource.setOrganizationId(org.getOrganizationId());
		 * resource.setOrganization(org);
		 */
		resource.setUsername(username);
		try {
			PwdBasedEncryption pwdBasedEncryption = new PwdBasedEncryption();
			resource.setPassword(new String(pwdBasedEncryption.encrypDB(resource.getPassword())));
		} catch (VeidblockException e) {
			throw new WebApplicationException("Internal server error when creating resource (crypto error)!",
					HttpStatus.INTERNAL_SERVER_ERROR_500);
		}
		Resource temp = resourceService.editResource(resource);
		// temp.setOrganization(org);
		return temp;
	}

	public String deleteResource(final String identity, long orgId) {
		String temp = resourceService.deleteResource(identity);
		organizationService.deleteOrganization(orgId);
		return temp;
	}

	public boolean activateAccount(String code) throws VeidblockException {
		String actCode = new String(Base64.getDecoder().decode(code));
		if (Objects.isNull(actCode)) {
			throw new VeidblockException("Invalid activation code !");
		}
		if (!actCode.contains(":")) {
			throw new VeidblockException("Invalid activation code format !");
		}
		String tokens[] = actCode.split(":");
		OTP otp = otpService.getOTPStatus(tokens[0], tokens[1], 0);
		if (Objects.isNull(otp)) {
			throw new VeidblockException("Invalid activation code (OTP)!");
		}
		otp.setExpStatus(1);
		otpService.editOTP(otp);
		resourceService.activate(Long.parseLong(tokens[0]), true);
		return true;
	}
}