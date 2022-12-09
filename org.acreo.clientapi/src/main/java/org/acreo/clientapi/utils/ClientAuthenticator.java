package org.acreo.clientapi.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.security.sasl.AuthenticationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.acreo.common.Representation;
import org.acreo.security.utils.SGen;
import org.apache.http.HttpHeaders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientAuthenticator {

	public enum AuthType {
		BASIC("basic"), DIGEST("digest"), ABASIC("abasic"), ADIGEST("adigest"), TOKEN("bearer"), CLIENT_CERT(
				"client_cert"), OTP("otp"), OTHER("other");

		String type;

		AuthType(String type) {
			this.type = type;
		}

		public String value() {
			return type;
		}
	};

	private String application = null;
	private String uniqueIdentifier = null;
	private String password = null;
	private AuthType authType = null;
	private String token = null;
	private String verfier = null;

	private ClientAuthenticator() {

	}

	private ClientAuthenticator(String uniqueIdentifier, String password, String token, String verfier) {
		this.uniqueIdentifier = uniqueIdentifier;
		this.password = password;
		this.token = token;
		this.verfier = verfier;
		// this.application = application;
	}

	public static ClientAuthenticator.Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String uniqueIdentifier;
		private String password;
		private AuthType authType;
		private String token;
		private String verifier;
		private String application;

		protected Builder() {
			// Hide default constructor
		}

		public ClientAuthenticator.Builder uniqueIdentifier(String uniqueIdentifier) {
			if (null == uniqueIdentifier) {
				return this;
			}
			this.uniqueIdentifier = uniqueIdentifier;
			return this;
		}

		public ClientAuthenticator.Builder password(String password) {
			if (null == password) {
				return this;
			}
			this.password = password;
			return this;
		}

		public ClientAuthenticator.Builder token(String token) {
			if (null == token) {
				return this;
			}
			this.token = token;
			return this;
		}

		public ClientAuthenticator.Builder verifier(String verifier) {
			if (null == verifier) {
				return this;
			}
			this.verifier = verifier;
			return this;
		}

		
		  public ClientAuthenticator.Builder application(String application) {
		  if (null == application) { return this; }
		  
		  return this; }
		 

		public ClientAuthenticator build() throws IOException {
			return new ClientAuthenticator(uniqueIdentifier, password, token, verifier);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public boolean autheticate() {
		try {
			loginClient(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private Representation<String> loginClient(int i) throws Exception {
		verfier += "/verify";
		ObjectMapper objectMapper = new ObjectMapper();
		MultivaluedMap<String, Object> headers;
		String storedToken = retrieveToken();
		this.token = storedToken;
		Response response = null;
		if (null == verfier) {
			throw new NullPointerException("Base URL of Endpoint is null !");
		}
		try {
			Client client = ClientBuilder.newClient();
			// headers.get()
			WebTarget target = client.target(verfier);
			if (storedToken != null) {
				authType = AuthType.TOKEN;
				response = target.request(MediaType.APPLICATION_JSON_TYPE)
						.header(HttpHeaders.AUTHORIZATION, storedToken)
						.post(Entity.entity("no-need", MediaType.APPLICATION_JSON_TYPE));

				if (response.getStatus() == 401) {
					String body = response.readEntity(String.class);
					File f = new File(getTokenPath());
					f.delete();
					throw new AuthenticationException(body);

				}
				Representation<String> rep = new Representation(200, storedToken);
				return rep;

			} else {

				if (uniqueIdentifier == null || password == null) {
					throw new AuthenticationException("HTTP/1.1 401 Unauthorized, uid or password is not specified !");
				}
				String authHdrStr = sendDummyRequest();

				String authCredentials = performAuthentication(authHdrStr);
				if (authCredentials == null) {
					throw new AuthenticationException("HTTP/1.1 401 Unauthorized");
				}
				client = ClientBuilder.newClient();
				target = client.target(verfier);
				response = target.request(MediaType.APPLICATION_JSON_TYPE)
						.header(HttpHeaders.AUTHORIZATION, authCredentials)
						.post(Entity.entity("no-need", MediaType.APPLICATION_JSON_TYPE));
			}

			headers = response.getHeaders();

			List<Object> authHeader = headers.get(HttpHeaders.AUTHORIZATION);
			if (null != authHeader && authHeader.size() > 0) {
				String token = authHeader.get(0).toString();
				String tokenHeader = token.substring(0, 6);
				if (tokenHeader.equalsIgnoreCase(AuthType.TOKEN.value())) {
					this.token = token;
					authType = AuthType.TOKEN;
					saveToken(token);
					Representation<String> rep = new Representation(200, token);
					return rep;
				}
			} else {
				authHeader = headers.get(HttpHeaders.WWW_AUTHENTICATE);
				if (null != authHeader && authHeader.size() > 0) {
					String type = authHeader.get(0).toString();
					String authHdrStr = sendDummyRequest();
					String authCredentials = performAuthentication(authHdrStr);
					if (authCredentials == null) {
						throw new AuthenticationException("HTTP/1.1 401 Unauthorized");
					}
					client = ClientBuilder.newClient();
					target = client.target(verfier);
					response = target.request(MediaType.APPLICATION_JSON_TYPE)
							.header(HttpHeaders.AUTHORIZATION, authCredentials)
							.post(Entity.entity("no-need", MediaType.APPLICATION_JSON_TYPE));
				} else {
					throw new AuthenticationException("HTTP/1.1 401 Unauthorized");
				}
				headers = response.getHeaders();
				authHeader = headers.get(HttpHeaders.AUTHORIZATION);
				if (null != authHeader && authHeader.size() > 0) {
					String token = authHeader.get(0).toString();
					String tokenHeader = token.substring(0, 6);
					if (tokenHeader.equalsIgnoreCase(AuthType.TOKEN.value())) {
						this.token = token;
						authType = AuthType.TOKEN;
						saveToken(token);
						Representation<String> rep = new Representation(200, token);
						return rep;
					}
				}
			}
			String body = response.readEntity(String.class);
			throw new AuthenticationException(body);
		} catch (IOException e1) {
			e1.printStackTrace();
			throw new AuthenticationException("HTTP/1.1 401 Unauthorized\n" + e1.getMessage());
		}
	}

	private String sendDummyRequest() {
		Client client = ClientBuilder.newClient();
		// headers.get()
		WebTarget target = client.target(verfier);
		Response response = target.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity("no-need", MediaType.APPLICATION_JSON_TYPE));
		MultivaluedMap<String, Object> headers;
		headers = response.getHeaders();
		List<Object> authHeader = headers.get(HttpHeaders.WWW_AUTHENTICATE);
		if (!Objects.isNull(authHeader)) {
			for (Object obj : authHeader)
				if (!Objects.isNull(obj))
					return obj.toString();
		}
		return null;
	}

	/*
	 * public MultivaluedMap<String, Object> getHeasers() { return headers; }
	 */

	private String performAuthentication(String headerValOrg) {
		String headerVal = "";
		if (headerValOrg.contains(" ")) {
			String tokens[] = headerValOrg.split(" ");
			headerVal = tokens[0];
		} else {
			headerVal = headerValOrg;
		}

		if (headerVal.toLowerCase().startsWith(AuthType.BASIC.value())) {
			authType = AuthType.BASIC;
			String credentials = headerVal + " "
					+ Base64.getMimeEncoder().encodeToString((uniqueIdentifier + ":" + password).getBytes());
			return credentials;
		} else if (headerVal.toLowerCase().startsWith(AuthType.DIGEST.value())) {
			authType = AuthType.DIGEST;
			String digestStr = createDigestResponse(headerValOrg);
			String credentials = digestStr;
			return credentials;
		}

		if (headerVal.toLowerCase().startsWith(AuthType.TOKEN.value())) {
			authType = AuthType.TOKEN;
			return AuthType.TOKEN.value() + " " + token;
		}
		return null;
	}

	private boolean saveToken(String token) throws Exception {

		File tokenFile = new File(getTokenDir() + File.separator + this.uniqueIdentifier + "_token.json");
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(tokenFile);
			bw = new BufferedWriter(fw);
			bw.write(token);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				throw ex;
			}
		}
		return true;
	}

	private String getTokenDir() {
		File f = new File("accessveidblock");
		if (f.exists()) {
			return f.getAbsolutePath();
		} else {
			f.mkdir();
			return f.getAbsolutePath();
		}
	}

	public String tokenFilePath(){
		File tokenFile = new File(getTokenDir() + File.separator + this.uniqueIdentifier + "_token.json");
		return tokenFile.getAbsolutePath();
	}
	
	public String retrieveToken() {
		File tokenFile = new File(getTokenDir() + File.separator + this.uniqueIdentifier + "_token.json");
		try {
			if (!tokenFile.exists()) {
				throw new Exception("Token File does not exists !");
			}
			FileInputStream fis = new FileInputStream(tokenFile);
			byte[] data = new byte[(int) tokenFile.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");
		} catch (Exception exp) {
			return null;
		}
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public String getPassword() {
		return password;
	}

	public AuthType getAuthType() {
		return authType;
	}

	public String getToken() {
		return token;
	}

	public String getVerfierURL() {
		return verfier;
	}

	public String getTokenPath() {
		File tokenFile = new File(getTokenDir() + File.separator + this.uniqueIdentifier + "_token.json");
		if (tokenFile.exists()) {
			return tokenFile.getAbsolutePath();
		}
		System.err.println("Access token does not exisits at [" + tokenFile.getAbsolutePath() + "]");
		return null;
	}
	

	private void downloadDigest(URL url, String user, String password) throws IOException {
	}

	private byte[] calculateA1(String uid, String realm, String password) {
		byte[] A1 = generateHash((uid + ":" + realm + ":" + password).getBytes());
		return A1;
	}

	private byte[] generateHash(byte[] value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] hashedBytes = digest.digest(value);
			return hashedBytes;
		} catch (NoSuchAlgorithmException exp) {
			return null;
		}
	}

	private byte[] calculateA2(String method, String uri, String password) {
		byte[] A1 = generateHash((method + ":" + uri).getBytes());
		return A1;
	}

	public static void main(String arg[]) {
		new ClientAuthenticator().createDigestResponse("");
	}

	public String createDigestResponse(String encodedSecurityContext) {

		StringBuffer stringBuffer = new StringBuffer();
		HashMap<String, String> attributes = parser(encodedSecurityContext);
		stringBuffer.append(encodedSecurityContext);

		byte A1[] = calculateA1(uniqueIdentifier, attributes.get(REALM), password);
		String a1 = bytesToHex(A1);

		byte A2[] = calculateA2("POST", "/verify", password);

		String a2 = bytesToHex(A2);

		stringBuffer.append(", ");
		stringBuffer.append(USERNAME + "=\"" + uniqueIdentifier + "\"");

		String nc = "0000001";
		stringBuffer.append(", ");
		stringBuffer.append(NC + "=" + nc);
		String cnonce = new SGen().nextHexString(20);
		stringBuffer.append(", ");
		stringBuffer.append(CNONCE + "=\"" + cnonce + "\"");

		stringBuffer.append(", ");
		stringBuffer.append(URI + "=\"/verify\"");

		String response = calculateResponse(a1.toLowerCase().getBytes(), a2.toLowerCase().getBytes(),
				attributes.get(QOP), attributes.get(NONCE), nc, cnonce);
		stringBuffer.append(", ");
		stringBuffer.append(RESPONSE + "=\"" + response + "\"");
		return stringBuffer.toString();
	}

	private String calculateResponse(byte[] A1, byte[] A2, String qop, String nonce, String nc, String cnonce) {

		if (qop == null || qop == "") {
			if (nonce != null) {
				throw new IllegalArgumentException("Invalid Nonce value '" + nonce + "' !");
			}
			byte[] temp = concat(A1, ":".getBytes());
			temp = concat(temp, nonce.getBytes());
			temp = concat(temp, ":".getBytes());
			temp = concat(temp, A2);
			byte[] responseUpdated = generateHash(temp);
			return bytesToHex(responseUpdated);
		}

		String formReplayProtectionString = ":";
		if (nonce != null) {
			formReplayProtectionString = formReplayProtectionString + nonce + ":";
		} else {
			throw new IllegalArgumentException("Invalid Nonce value '" + nonce + "' !");
		}

		if (nc != null) {
			formReplayProtectionString = formReplayProtectionString + nc + ":";
		} else {
			throw new IllegalArgumentException("Invalid Nc value '" + nc + "' !");
		}

		if (cnonce != null) {
			formReplayProtectionString = formReplayProtectionString + cnonce + ":";
		} else {
			throw new IllegalArgumentException("Invalid Cnonce value '" + cnonce + "' !");
		}

		if (qop != null) {
			formReplayProtectionString = formReplayProtectionString + qop;
		} else {
			throw new IllegalArgumentException("Invalid Qop value '" + qop + "' !");
		}
		if (formReplayProtectionString.length() > 0) {
			formReplayProtectionString = formReplayProtectionString + ":";
		} else {
			throw new IllegalArgumentException("Invalid value !");
		}
		byte[] temp = concat(A1, (formReplayProtectionString).getBytes());
		byte[] finalForVer = concat(temp, A2);
		byte[] responseUpdated = generateHash(finalForVer);
		return bytesToHex(responseUpdated);
	}

	final protected static char[] hexArray = "0123456789abcdef".toCharArray();

	public String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public byte[] concat(byte[] a, byte[] b) {
		int aLen = a.length;
		int bLen = b.length;
		byte[] c = new byte[aLen + bLen];
		System.arraycopy(a, 0, c, 0, aLen);
		System.arraycopy(b, 0, c, aLen, bLen);
		return c;
	}

	private final String USERNAME = "username";
	private final String REALM = "realm";
	private final String NONCE = "nonce";
	private final String URI = "uri";
	private final String RESPONSE = "response";
	private final String OPAQUE = "opaque";
	private final String QOP = "qop";
	private final String NC = "nc";
	private final String CNONCE = "cnonce";

	private HashMap parser(String encodedSecurityContext) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {

			String esc = encodedSecurityContext.substring("Digest".length() + 1, encodedSecurityContext.length());

			String tokenz[] = esc.split(", ");
			for (String token : tokenz) {
				String fields[] = token.split("=");
				if (fields[0].equalsIgnoreCase(REALM)) {
					hashMap.put(REALM, fields[1].substring(1, fields[1].length() - 1));
				} else if (fields[0].equalsIgnoreCase(NONCE)) {
					hashMap.put(NONCE, fields[1].substring(1, fields[1].length() - 1));
				} else if (fields[0].equalsIgnoreCase(OPAQUE)) {
					hashMap.put(OPAQUE, fields[1].substring(1, fields[1].length() - 1));
				} else if (fields[0].equalsIgnoreCase(QOP)) {
					hashMap.put(QOP, fields[1]);
				}
			}
			return hashMap;
		} catch (Exception exp) {
			return null;
		}
	}
}