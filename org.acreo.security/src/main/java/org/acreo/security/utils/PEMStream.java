package org.acreo.security.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.acreo.common.exceptions.VeidblockException;

import com.fasterxml.jackson.databind.ObjectMapper;

import sun.security.pkcs10.PKCS10;
import sun.security.provider.X509Factory;

public class PEMStream {

	private static final String CSR_START_JAVA_TAG = "-----BEGIN NEW CERTIFICATE REQUEST-----";
	private static final String CSR_END_JAVA_TAG = "-----END NEW CERTIFICATE REQUEST-----";

	private static final String CSR_START_OPENSSL_TAG = "-----BEGIN CERTIFICATE REQUEST-----";
	private static final String CSR_END_OPENSSL_TAG = "-----END CERTIFICATE REQUEST-----";

	public boolean csr2pem(byte[] encodedCSR, ByteArrayOutputStream outStream) {
		if (null == outStream || null == encodedCSR) {
			return false;
		}
		PrintStream printStream = new PrintStream(outStream);
		printStream.println(CSR_START_JAVA_TAG);
		printStream.println(Base64.getMimeEncoder().encodeToString(encodedCSR));
		printStream.println(CSR_END_JAVA_TAG);
		printStream.close();
		return true;
	}

	public boolean pem2csr(String pemCSR, ByteArrayOutputStream outStream) throws IOException {
		if (null == outStream || null == pemCSR || pemCSR.length() == 0) {
			return false;
		}
		pemCSR = pemCSR.replaceAll(CSR_START_JAVA_TAG, "").replaceAll(CSR_END_JAVA_TAG, "").trim();
		// Handle OpenSSL PKCS10 request
		pemCSR = pemCSR.replaceAll(CSR_START_OPENSSL_TAG, "").replaceAll(CSR_END_OPENSSL_TAG, "").trim();
		byte[] encodedPKCS10 = Base64.getMimeDecoder().decode(pemCSR);
		outStream.write(encodedPKCS10);
		outStream.flush();
		return true;
	}

	public PKCS10 pem2PKCS10(String pemCSR) throws VeidblockException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try{
		if (pem2csr(pemCSR, outStream)) {
			return new PKCS10(outStream.toByteArray());
		}
		return null;
		}
		catch(Exception exp){
			throw new VeidblockException(exp);
		}
	}
	
	public byte[] pem2csr(String pemCSR) throws VeidblockException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		try{
		if (pem2csr(pemCSR, outStream)) {
			return outStream.toByteArray();
		}
		return null;
		}
		catch(Exception exp){
			throw new VeidblockException(exp);
		}
	}
	
	

	public boolean x509Cert2pem(X509Certificate cert, ByteArrayOutputStream byteArrayOutputStream)
			throws CertificateEncodingException {
		PrintStream out = new PrintStream(byteArrayOutputStream);
		out.println(X509Factory.BEGIN_CERT);
		out.println(Base64.getMimeEncoder().encodeToString(cert.getEncoded()));
		out.println(X509Factory.END_CERT);
		out.close();
		return true;
	}

	public boolean pem2x509Certificate(String pemX509Cert, ByteArrayOutputStream outStream) throws IOException, Exception {
		if (null == outStream || null == pemX509Cert || pemX509Cert.length() == 0) {
			return false;
		}
		byte[] encodedX509Cert = Base64.getMimeDecoder()
				.decode(pemX509Cert.replaceAll(X509Factory.BEGIN_CERT, "").replaceAll(X509Factory.END_CERT, ""));
		outStream.write(encodedX509Cert);
		outStream.flush();
		return true;
	}

	public X509Certificate pem2x509Cert(String pemX509Cert) throws CertificateException, IOException, Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		if (pem2x509Certificate(pemX509Cert, outStream)) {
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
			InputStream in = new ByteArrayInputStream(outStream.toByteArray());
			return (X509Certificate) certFactory.generateCertificate(in);
		}
		return null;
	}

	public String toPem(X509Certificate x509Certificate) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			if (x509Cert2pem(x509Certificate, baos)) {
				String aCertPem = new String(baos.toByteArray());
				return aCertPem;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
	}
	public String toBase64String(PublicKey publicKey){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(baos );
		out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
		out.close();
		return new String(baos.toByteArray());
	}
	
	public PublicKey fromBase64StringToPublicKey(String publicKeyEncoded) throws VeidblockException{
		byte []pKey = Base64.getDecoder().decode(publicKeyEncoded);
		try {
			PublicKey publicKey = 
				    KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pKey));
			return publicKey;
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			throw new VeidblockException(e);
		}
	}
	
	
	
	
	public String toPemCertChain(X509Certificate x509Certificate[]) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String allCertInPem = "";
		try {
			PEMStream pemStream = new PEMStream();
			for (X509Certificate tempX509 : x509Certificate) {
				if (pemStream.x509Cert2pem(tempX509, baos)) {
					String aCertPem = new String(baos.toByteArray());
					allCertInPem = allCertInPem +"Serical No:"+ tempX509.getSerialNumber().toString() + "\n";
					allCertInPem = allCertInPem + aCertPem.toString() + "\n";
					baos = new ByteArrayOutputStream();
				} else {
					return null;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return allCertInPem;
	}
	
	public X509Certificate [] extractCertChain(String pemChain) throws Exception {
		String temp = new String(pemChain);
		boolean bool = temp .contains(X509Factory.BEGIN_CERT);
		List<X509Certificate> chain = new ArrayList<X509Certificate>();
		while(bool){
			// Remove Serial No
			temp = temp.substring(temp.indexOf(X509Factory.BEGIN_CERT));
			String aCert = temp.substring(temp.indexOf(X509Factory.BEGIN_CERT),temp.indexOf(X509Factory.END_CERT)+X509Factory.END_CERT.length());
			X509Certificate certificate = pem2x509Cert(aCert);
			chain.add(certificate); 
			temp = temp.substring(temp.indexOf(X509Factory.END_CERT)+X509Factory.END_CERT.length());
			bool = temp .contains(X509Factory.BEGIN_CERT);
		}
		X509Certificate x509Certificate[] = new X509Certificate[chain.size()];
		int i=0;
		for(X509Certificate certificate : chain){
			x509Certificate[i] = certificate ;
			i++;
		}
		return x509Certificate;
	}
	
	public static void main(String arg[]){
		String data = "\"Serical No:30219296\\n-----BEGIN CERTIFICATE-----\\nMIIDnzCCAoegAwIBAgIEAc0cIDANBgkqhkiG9w0BAQsFADCBqzEqMCgGCisGAQQBKgILAgETGmh0\\r\\ndHA6Ly8xMjcuMC4wLjE6ODA4MC9yb290MR8wHQYJKoZIhvcNAQkBFhBzZXJ2ZXJAZ21haWwuY29t\\r\\nMQswCQYDVQQGEwJTRTEMMAoGA1UECBMDU1RNMQ4wDAYDVQQHEwVLaXN0YTEPMA0GA1UECxMGTmV0\\r\\nbGFiMRMwEQYDVQQKEwpSSVNFLUFDUkVPMQswCQYDVQQDEwJDQTAeFw0xNzA3MTQxNTIzMzNaFw0x\\r\\nNzA3MTUxNTIzMzNaMBQxEjAQBgNVBAMTCTU1NjE0MTU3MjCCASIwDQYJKoZIhvcNAQEBBQADggEP\\r\\nADCCAQoCggEBALO/VIexTaoNKZXha1amRIdNIxhJ94lh51Y0wruJ2ghQZ1XQHbe6nysJukONb628\\r\\nUgli6+Rp0kT8Z84wTYt42rr63FhbOrnLiv3r30u6TjWUpCKVzaCj0ioIw9mkBub/qTwQ7SaI4Wo5\\r\\nK93zwWOwAa+ElyBGcu3bu7i9WMxMKQsJq6d3cZ2qzM97/4PqPjX/1b9CDR1x8g1aH94Mhdf1xb0+\\r\\nq7lyX7TCjeVgewKVEGMvymhHb9iPrxBSLoUaeYypd0xbGKJOUgkKD932g/cz8EoSFXzRSyzkY/SV\\r\\nCfa9HCXhxDD4kL+m4VA5F2Udf9aPzGemAOiC0Dqn7F/FvmfK02kCAwEAAaNhMF8wHwYDVR0jBBgw\\r\\nFoAUhaDzgk5vYeevq64pz7OlWCVwOk0wDAYDVR0TAQH/BAIwADAPBgNVHQ8BAf8EBQMDB/+AMB0G\\r\\nA1UdDgQWBBSFoPOCTm9h56+rrinPs6VYJXA6TTANBgkqhkiG9w0BAQsFAAOCAQEAbvLw+Y9hp9qh\\r\\nv0ne9n6YiuyuornPCB87XDvfCazhUUS0XPdRToFfasooGo87xQL1JDn48zuGMqKs1pYpAklek+qH\\r\\nwhvVmUpwC75n9DRu21WQKu8pjUTnva5NbD73kjtUAnrKqvNzZDKJAPwmkaY3uu8N/SEmFfET7bf2\\r\\nSjXbyYUMe58jXc/ZSaTCXv5JIMN0ZyAYJdZ3tj0vy+Y5xQvkEopuXva8wLdcHqlVv7YizOADTCsp\\r\\nvMzPrt1KFOb1+0iPgLqfqpuMYe4aM4azEiwib2AIxfmOn/Xt1Ne10Zu36u3zyNZktGVFgSXrGODo\\r\\nPknvPITDqeaHlIOkuJ4CQLNBJA==\\n-----END CERTIFICATE-----\\n\\nSerical No:1923486270\\n-----BEGIN CERTIFICATE-----\\nMIIERTCCAy2gAwIBAgIEcqYSPjANBgkqhkiG9w0BAQsFADCBtDEsMCoGCisGAQQBKgILAgETHGh0\\r\\ndHA6Ly8xMjcuMC4wLjE6ODA4MC9wb2xpY3kxHzAdBgkqhkiG9w0BCQEWEHNlcnZlckBnbWFpbC5j\\r\\nb20xCzAJBgNVBAYTAlNFMQwwCgYDVQQIEwNTVE0xDjAMBgNVBAcTBUtpc3RhMQ8wDQYDVQQLEwZO\\r\\nZXRsYWIxEzARBgNVBAoTClJJU0UtQUNSRU8xEjAQBgNVBAMTCVBvbGljeSBDQTAeFw0xNzA1MDgw\\r\\nODM4MjZaFw0xNzA1MDkwODM4MjZaMIGrMSowKAYKKwYBBAEqAgsCARMaaHR0cDovLzEyNy4wLjAu\\r\\nMTo4MDgwL3Jvb3QxHzAdBgkqhkiG9w0BCQEWEHNlcnZlckBnbWFpbC5jb20xCzAJBgNVBAYTAlNF\\r\\nMQwwCgYDVQQIEwNTVE0xDjAMBgNVBAcTBUtpc3RhMQ8wDQYDVQQLEwZOZXRsYWIxEzARBgNVBAoT\\r\\nClJJU0UtQUNSRU8xCzAJBgNVBAMTAkNBMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA\\r\\n33ILSF/s1ZZwb5aP77RyUAEvJoKLhSFcT4sasbFAjk/dDU6E2WoXQFNhOe5ZAPmP2N56NVmd/iBA\\r\\nihT/7UwA1Y7THqlqNr2CBJuj77dcapXZM51KMPMEw1lQrzqcyOyTM5Jo8btMoLW/dNK+wwmIMZTy\\r\\nBpoiPzDL7tBNL/6ex72br0QaE17wtP5WGTfyknpHzvf6mJPYFA87WI7XfnxEp5u8LSSQqOnslqiI\\r\\nidFEoz3AF+T+jF4dM0v3ufaB9VhuW9cT3s/aITSU8gtY0BlSDzFJQL1DsPQCCYG6pEAK2BeS8U4k\\r\\n4h2KB1kaRJCCXj56TDiqTQLJ8I6k31vDXKkhOwIDAQABo2YwZDAfBgNVHSMEGDAWgBQAQOLzUMhJ\\r\\nJBWyugFrYq7woNH0zTASBgNVHRMBAf8ECDAGAQH/AgEBMA4GA1UdDwEB/wQEAwIBBjAdBgNVHQ4E\\r\\nFgQUAEDi81DISSQVsroBa2Ku8KDR9M0wDQYJKoZIhvcNAQELBQADggEBAHdMYrPxArsVBvQDNNwM\\r\\nYpkkByC4LntabPClfrwK1+Ci+mvUihtzxpiwtqZdBKzi0HQQ5tO0ybd+RQ/r6NFa6pGq9mLRreeb\\r\\nI8t4kulc573ZbTFf0ZmAYRE4D05q9RQglhWdULwDVS40qGFAIBQWKmZj7oB3D8gesPvcF0tltRpH\\r\\nmPGWAVsjvK0Jr5rrXXIkwMNNWaONu5OHDist/yFEa+aEqqgGuO7mrupaGIt8G2iP3GKh4aah4qFI\\r\\nIts7K53AbqNR233OFnaXtAj1hlK0v9bpbaVwszBZH880UF65nCela7t2TQ4v6PjfFx6EmEqnx+mr\\r\\nZhexEuiw2Axaln4ud28=\\n-----END CERTIFICATE-----\\n\\nSerical No:1530002333\\n-----BEGIN CERTIFICATE-----\\nMIIESjCCAzKgAwIBAgIEWzH7nTANBgkqhkiG9w0BAQsFADCBsDEqMCgGCisGAQQBKgILAgETGmh0\\r\\ndHA6Ly8xMjcuMC4wLjE6ODA4MC9yb290MR8wHQYJKoZIhvcNAQkBFhBzZXJ2ZXJAZ21haWwuY29t\\r\\nMQswCQYDVQQGEwJTRTEMMAoGA1UECBMDU1RNMQ4wDAYDVQQHEwVLaXN0YTEPMA0GA1UECxMGTmV0\\r\\nbGFiMRMwEQYDVQQKEwpSSVNFLUFDUkVPMRAwDgYDVQQDEwdSb290IENBMB4XDTE3MDUwNDE1Mzg1\\r\\nN1oXDTIyMDUwMzE1Mzg1N1owgbQxLDAqBgorBgEEASoCCwIBExxodHRwOi8vMTI3LjAuMC4xOjgw\\r\\nODAvcG9saWN5MR8wHQYJKoZIhvcNAQkBFhBzZXJ2ZXJAZ21haWwuY29tMQswCQYDVQQGEwJTRTEM\\r\\nMAoGA1UECBMDU1RNMQ4wDAYDVQQHEwVLaXN0YTEPMA0GA1UECxMGTmV0bGFiMRMwEQYDVQQKEwpS\\r\\nSVNFLUFDUkVPMRIwEAYDVQQDEwlQb2xpY3kgQ0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEK\\r\\nAoIBAQCs6t/HysiRlSWDFOIWa0NILdQW89z+jBiGxPi71KVxuebuUJ3ncvkt5kYs0L8pm6Lq7NCP\\r\\nHQnLL/0lOlFVFbdDkO/UZUVN8CXSnFGlw09PI86XeIi/b6vCFagrFmewfTzxXBVFnbAPPyJ9wIqS\\r\\nlpX848Ol4ET074JuD2rHhodSIA1UL8bWXjbiySu7cfG8I3y0pAq6I0bHfXQNEsmG7MrWSb8Znq+t\\r\\nf/0BVmyBD6sejeEi/y+6c+pxGHjuyzDdkzbOOaVp7cAQHy0wxO9gN2jAcxHs6kBRf3MEi2b5sVtl\\r\\noJi0e7HrSs0veeIusA2OaPk6z1e2uJJzCWoCkUabSWI5AgMBAAGjZjBkMB8GA1UdIwQYMBaAFBY9\\r\\nXjEEeGaqSQ6vw/sp4oBXozTmMBIGA1UdEwEB/wQIMAYBAf8CAQMwDgYDVR0PAQH/BAQDAgEGMB0G\\r\\nA1UdDgQWBBQWPV4xBHhmqkkOr8P7KeKAV6M05jANBgkqhkiG9w0BAQsFAAOCAQEAFxiDwUwcbL1h\\r\\n7LGKu63/0D3NukYwNF33en9Gh+2VfLEiYnT4SrE8RM2a8aB0uZKuWDgnRlJuf8Bz+3Yl22ZrRIh0\\r\\nUDosPSTn8zWhrt8atNdtKV42GbL1ptkddn8AV0QASKAbP+VbmsMVWJscxH8Vn9HCBehLBkjR/EVb\\r\\nw9PEPNC+MI3mkxHvddYKfdoG6LiIdgA6juz4UXLBSaWeeaNxjDau3BvLg69m0n55EyNFWBK/+Boz\\r\\nvu1YCVe5xaIhSdrBbkApgY0FLdEsB1ibhymEnmsweAULNP6qR8DQhk4+cBgqxSpyBP8hTZFFnal9\\r\\nJq97S0IdzPreHSzlN3f+QXMg3Q==\\n-----END CERTIFICATE-----\\n\\nSerical No:838526587\\n-----BEGIN CERTIFICATE-----\\nMIIERjCCAy6gAwIBAgIEMfrmezANBgkqhkiG9w0BAQsFADCBsDEqMCgGCisGAQQBKgILAgETGmh0\\r\\ndHA6Ly8xMjcuMC4wLjE6ODA4MC9yb290MR8wHQYJKoZIhvcNAQkBFhBzZXJ2ZXJAZ21haWwuY29t\\r\\nMQswCQYDVQQGEwJTRTEMMAoGA1UECBMDU1RNMQ4wDAYDVQQHEwVLaXN0YTEPMA0GA1UECxMGTmV0\\r\\nbGFiMRMwEQYDVQQKEwpSSVNFLUFDUkVPMRAwDgYDVQQDEwdSb290IENBMB4XDTE3MDUwNDE1Mzc0\\r\\nOFoXDTIyMDUwMzE1Mzc0OFowgbAxKjAoBgorBgEEASoCCwIBExpodHRwOi8vMTI3LjAuMC4xOjgw\\r\\nODAvcm9vdDEfMB0GCSqGSIb3DQEJARYQc2VydmVyQGdtYWlsLmNvbTELMAkGA1UEBhMCU0UxDDAK\\r\\nBgNVBAgTA1NUTTEOMAwGA1UEBxMFS2lzdGExDzANBgNVBAsTBk5ldGxhYjETMBEGA1UEChMKUklT\\r\\nRS1BQ1JFTzEQMA4GA1UEAxMHUm9vdCBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\\r\\nAJwIU1uGqG0HwTWcCHoGNnoJtGxgTHURcQQ8r2B1CZV+8YnZQa2dAGeLABY6AMyMCnP+hJFWsAEP\\r\\nYpc/b1DwXTsu0sHTrRgn33WZ6yzwODznY78Yqt/N3L856YaHxcNketgteo1c8VdRoILdb1Gqcbem\\r\\nIPdz0qzSv+2ScwEkbByrqwpssw19YNuuimpJoGHU4Y5lFDndCu68GIo4sRZUIvyp7kXru6MO41IM\\r\\nkjO+65KN13c8EX8HwUXRHkkYR/CpU4U+LfenzmzhCTkrmSUOqzCvDqszM/yF0uGmVaUjB/PrKUmb\\r\\nXuHRs/TiZvEW+dPCpNMLEOazU3VmVzAccDSERisCAwEAAaNmMGQwHwYDVR0jBBgwFoAUUsQeQTNM\\r\\n1as1XK3yk41mPYSXfPMwEgYDVR0TAQH/BAgwBgEB/wIBBDAOBgNVHQ8BAf8EBAMCAQYwHQYDVR0O\\r\\nBBYEFFLEHkEzTNWrNVyt8pONZj2El3zzMA0GCSqGSIb3DQEBCwUAA4IBAQA2ufW2tLe9xtICWsyp\\r\\nkM5pHxvXqGiPdI/UbW8NdCGLzdgihPDXTp7MHC2ryVsKX3HKYBRQNRhR69rE5mDCn8q0OtAfTdhy\\r\\neY+J/qmr9AkVHhOZAJ69sg5g/erkOPpn2ld/oopgdcCJVZ3B0n8kkYc1kWjgIpAkT4IynuLYWgfL\\r\\nY/FSpcU8Ky06E5wTCv5g6GtvM6H4O1alldFVvuURUDZ+xX0y7qshTXMxbCd+6Wq7gGWtefeuu6y+\\r\\nCPgygb5ook+c5QI6bridITbPJYkqtmn99yKMhouxPlQFmQecKo3kRD1/Znnl+cT0isQNW8mH0SA7\\r\\nD/RPlXKhSns7tqgPoqWB\\n-----END CERTIFICATE-----\\n\\n\"";
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			data = mapper.readValue(data , String.class);
			X509Certificate [] certificates = new PEMStream().extractCertChain(data);
			for(X509Certificate cert: certificates){
				System.err.println(cert.getSubjectDN().toString());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}