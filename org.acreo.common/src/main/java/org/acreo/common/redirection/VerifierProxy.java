package org.acreo.common.redirection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.acreo.common.entities.VeidErrorMessage;
import org.acreo.common.exceptions.VeidblockException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.common.base.Optional;

public class VerifierProxy {

	URI verifier;

	public VerifierProxy(URI verifier_) {
		try {
			if (verifier_.toString().endsWith("/verify")) {
				this.verifier = new URI(verifier_.toString());
			} else {
				this.verifier = new URI(verifier_.toString() + "/verify");
			}
		} catch (URISyntaxException e) {
			try {
				this.verifier = new URI(verifier_.toString() + "verify");
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public Object process(ContainerRequestContext requestContext) throws WebApplicationException {

		Optional authenticatedUser;
		String encodedAuth;
		encodedAuth = extractCredentials(requestContext);
		if (encodedAuth == null) {
			return encodedAuth;
		}
		return processAuthorizationHeader(encodedAuth, requestContext);

	}

	private String extractCredentials(ContainerRequestContext requestContext) {

		try {
			String rawToken = requestContext.getCookies().get("auth_token").getValue();
			String rawUserId = requestContext.getCookies().get("auth_user").getValue();
			return rawToken;
		} catch (Exception e) {

			String aaSecurityContextEncoded = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			if (aaSecurityContextEncoded == null) {
				try {
					if (isSpecialUrl(requestContext.getUriInfo().getPath(), requestContext.getRequest().getMethod())) {
						return null;
					}
					aaSecurityContextEncoded = readRequestBody(requestContext);
				} catch (IOException exp) {
					return null;
					// throw new
					// WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
				}
				if (aaSecurityContextEncoded == null || aaSecurityContextEncoded.equals("")) {
					return null;
					// throw new
					// WebApplicationException(Response.status(Status.UNAUTHORIZED).build());
				}
			}
			return aaSecurityContextEncoded;
		}
	}

	private boolean isSpecialUrl(String path, String method) {

		String relaxUrl = "resource/user";
		String relaxmethod = "POST";
		if (method.equalsIgnoreCase(relaxmethod) && path.equalsIgnoreCase(relaxUrl)) {
			return true;
		}

		return false;
	}

	private String readRequestBody(ContainerRequestContext request) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getEntityStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] readChars = new char[1024];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(readChars)) > 0) {
					stringBuilder.append(readChars, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		String body = stringBuilder.toString();
		return body;
	}

	private Object processAuthorizationHeader(String encodedHeader, ContainerRequestContext requestContext) throws WebApplicationException {

		String securityContextEncoded = encodedHeader.toLowerCase();
		if (securityContextEncoded.startsWith("bearer") || securityContextEncoded.startsWith("oauth")
				|| securityContextEncoded.startsWith("oauth2")) {
			try {
				return parser(encodedHeader);
			} catch (VeidblockException e) {
				return null;
			}

		} else {

			try {
				// Send to the Verification Service;
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost request = new HttpPost(this.verifier.toString());
				request.addHeader(HttpHeaders.AUTHORIZATION, encodedHeader);
				HttpResponse response = httpClient.execute(request);
				if (response.getStatusLine().getStatusCode() == 200) {
					Header header[] = response.getHeaders(HttpHeaders.AUTHORIZATION);
					for (Header h : header) {
						if (h.getName().equals(HttpHeaders.AUTHORIZATION)) {
							try {
								return parser(h.getValue());
							} catch (VeidblockException e) {
								return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Problems when processing credentials !");
								/*throw new WebApplicationException(Response.status(Status.UNAUTHORIZED)
										.entity("Problems when processing credentials !").build());*/
							}
						}
					}
					return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Problems when processing credentials !");
				} else if (response.getStatusLine().getStatusCode() == 401) {
					// 670467219
					return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Invalid credentials !");					

				} else {
					return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Problems when processing credentials !");
				}

			} catch (ClientProtocolException e) {
				return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Problems when processing credentials !");
			} catch (IOException e) {
				return new VeidErrorMessage(Status.UNAUTHORIZED.getStatusCode(), "Problems when processing credentials !");
			}
		}
	}

	private String parser(String encodedSecurityContext) throws VeidblockException {

		String temp = encodedSecurityContext;

		temp = temp.toLowerCase();
		String jwnTokenencoded = null;

		int headerLength = 0;
		if (temp.startsWith("oauth2 bearer")) {
			headerLength = "oauth2 bearer".length();
			jwnTokenencoded = encodedSecurityContext.substring(headerLength, encodedSecurityContext.length());

		} else if (temp.startsWith("oauth bearer")) {
			headerLength = "oauth bearer".length();
			jwnTokenencoded = encodedSecurityContext.substring(headerLength, encodedSecurityContext.length());

		} else if (temp.startsWith("oauth2")) {

			headerLength = "oauth2".length();
			jwnTokenencoded = encodedSecurityContext.substring(headerLength, encodedSecurityContext.length());

		} else if (temp.startsWith("oauth")) {
			headerLength = "oauth".length();
			jwnTokenencoded = encodedSecurityContext.substring(headerLength, encodedSecurityContext.length());

		} else {
			headerLength = "bearer".length();
			jwnTokenencoded = encodedSecurityContext.substring(headerLength, encodedSecurityContext.length());
		}

		jwnTokenencoded = jwnTokenencoded.trim();
		return jwnTokenencoded;
	}
}