package org.acreo.auth.util;

import org.acreo.common.Representation;
import org.acreo.common.entities.TokenCO;
import org.acreo.common.exceptions.VeidblockException;
import org.acreo.common.utils.RestClient;

public class VeidblockLedgerConnector {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String vblURL = "http://localhost:10000/vbla/add/670467210";
		TokenCO co = new TokenCO();
		co.setToken("MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwGggCSABIID6HsiYXV0aG9yaXplZEJ5Ijo2NzA0NjcyMTAsImF1dGhvcml6ZWRUbyI6NjcwNDY3MjE5LCJwYXlsb2FkIjoiTUlBR0NTcUdTSWIzRFFFSEE2Q0FNSUFDQVFBeGdnRmtNSUlCWUFJQkFEQklNRUF4S2pBb0Jnb0praWFKay9Jc1pBRVpGaHBvZEhSd09pOHZiRzlqWVd4b2IzTjBPamt3TURBdllYVjBhREVTTUJBR0ExVUVBd3dKTXpNME1qRXpPVGswQWdRUmlsZWhNQTBHQ1NxR1NJYjNEUUVCQVFVQUJJSUJBRFdnM2Q4OTJSSVJuRDhoVWNTSytWbnFGYjNUN2w5YmcrMFYwMjZiZzljK0V2MDRsYWVvdlAyUG5ETDZMck44RTdndUtGeEJoRWdWeWIza3BHUWl6VEJJS2NZeWErU0d4WXhIRVFtRXk3V3NrakUyWVhNS3RuR1NlME1ScmhYTi9yMkVtOHM5WVQ1QWljSXVTNVA5RDZBdGdVU2RJc21mRGFZNlloaUgrL29tenRnNDM5RUkxSGcvcHRQSHFEYkxmWGVSOXRpc2ZzZDE0L0tmMXJENVRMb3JlUEhsUVVuMU81WmYwNHBqQ1VFZ1pUNlFhdmFoNmpmRk05eGkyK2pabWRET3ErcVMyeGRrL2FzbVl1MU9ONHZYVHJWVlUvcStVU010Rzdna0U2UmFkcXMzS2l5S2NQWk5YRUxIQ0pCK2dUWnczVFFxNXJQdnYyRXpyeGpxY0I4d2dBWUpLb1pJaHZjTkFRY0JNQjBHQ1dDR1NBRmxBd1FCQWdRUXlKMzBBczFpdXFoNmM2NDcwbGhGTHFDQUJJSUQ2TkVaNmNVYjdERm1HMEY1bG92S1VySk9sUXNxNWxNajdjTnpHdTFFb0xxU0cyd1BSbGM3SDlqOHl5TFdLQVFINW9NK1F3SXNTNFVzMzJlUUlZS1dKQzkramxvNUxoZjRuWTZoWUszQ1U4UUlMNjdtaGY0Wm00bWk0SVlUempaU2hRaGVyQ01VLzhUYmc5U1lWR1VUSEFjVHlQbUFNZUhSaUpHSzlwSjhUYU51aUxBNlBVT0hNUkJoVXloU2Q5YUhVOTdsWGZOc1hzbVVoTXBuU3VDMXhFN3NGTzBMZXR6eGE1WG5DN3ZaRXRDUnFsYitmejBBMVJjZ2hEZThwUHRXeVI3c1dYVDRNVGwycVQyYzlVeXpTb1RRSnJFRTJEWG5RN092VGc4OFN4RFJ4S0pJTnppYU5WYmd6eGhIb0NNUzlzVFdyanlxNzl0ZkFYL3lZYjNiQUZndklVa1F0TGZPREFEYVUzSmxEaUVlYWgEggPoS0RkbGdWZDJvOERZN2k1TThzV0M2OVRCZEk2bXpNRXp2dk9UTVVFUVVvTGsyTjJVNWhqZ2dVbTJqQ2cyckc2MGh6c1oyQkNDdVMxVEZvVGhYN1dRUG9oM1ZsTE0yd2duYXJ5ZFJ0U1ZlUU1iMkJPdWtMcE1Ta3pPZnVlM2lLY0VqeHpvN3g3TXJJa1VSbTIwRm9raVZja1o0a2plOGEvNVVVR2xDczdiSlZIZTByc1NmSjFVVzJXQ2VLUW85bmhtZnFTckVRRE83VVBjbVRWRFFyZEc4Qk5ScEt2YXJFamtrSVFOWCtJKytWam03dHRjbTNDb2tiMURuYkpwaUhHc1pXMjdNOXByS1FReUQyWmRPZTlRYm5iYnloYUd3NEJ4V0VvSWJZdGdJKzRnM1hHUFcyRjYvRklrUk50TTJGZmFKbVBGR0tCdmRPWUFEeW50eHo5ek10V2g1TkhFVmxXVmpyeTZsbWtraVFWdkZSd1JKSnBxSGQvTUVRN1VGU3JacGNYR0xLQlYwRklsbi9XUytoNzNnQlNUNDRvSzhDUktseGJxSHJMSXRyOXVCbk9sRDRqK09kR04vTTR5YjFLTWZoUGsxaXg0VVhZZmhQQ0lZZnVLRGxBdVZsQUlFVE1VTWNaVm1MdGlzeGI0L3p5cGVTVXc1QkQzc2Fuc3J3OEFCa3I3UlhaM21sZWJic2h5N3k2TlgxVXJ2UlovbWJGZno3MnJVTlhldzZGdFlaZ0VBdGxQcTZWZWt3djBuZElySG01TEN2dmNkMWl1UlVMeExEVEdvZjRKQmthQlhsVjdZQWVrR0U2UnNJTlZrWlVJS1I5VWNKVFFjSnJqR3NJbWFBSkJ2ZVFNdXRhMEdiU3U4M0dZaDZPY2xOWjNwWjB5RU5Vbjk4L2lHa0Z3QXdlS3JXN05aMzg1eFNFejEzYWxiKzdRUk1rZ3FhZUN6dnNIOFdUMXowUisySGkzU1NRZU5KZDJlTng5dnhvOURGczBTQkJKOUF6V0VzbkxDcVgvSDI2WkhFRWtlZEU1ai9XL2ZsbnRLYlMzUU4vbWtITTR3aEtzOXVTWENZbFVJNWFJZjVOMmprY2lZZGhIVkdSS1NxK0ErUDlhck5rT09uYVlSSC9kUE50VGx1UzlEUktJaHdqSldzMHlpenFNMWwvTUI5Z1RtYnljbGM1TzkvS25ZV3ZtaHU4VlhIU0NlN0ZCWkNERy9ZTzB4Q3UzbUI5RElDUngxNGg4V1FEZDAvYU5xYm9OQUdkbFZPMjZTWk03ajYzTndBTENZRWdnTjQ5ZW9GcHNIMTNHZ1JFd3RnYi9jWHB2aWM5YwSCA+h2RXA3WWFXZzlJK0lTL3VQTkE4cjkwT2hINUs0U0ZJWHk2Wlp6VWZpVzM5OWhmd1pwbU1KUlBObkdlSjhxMUpnSFJ5S1Jmb0pMbUl4dk1iNzhieEFWSno4Vyt1Y3p4YVBpTHFJMUF5cEplWFVEbDBXSHB6emtwR3F6cGhjYml6VG0xcDdQaXg4ZldRTHhDU1plYnF4YmRUdVhnQkRUTUxWcm5wQVU1RFlpRzZCYWFRMDNqaHBzSWsxRUF3ODBKelJkOWw1TDFmNUZXcEprd2FVUHgzSVlnTjFDSjNrVXR3NGxtNDI2QkJyYmpWZXBBUEF6dUVtcmQxY3MrbFp3eDdFTWpkYWZWd2krVWEwRUFsMU1ER0VoOUpaSzIwa2NsR0dYNzViWDc2MC8yOTFJRERJZFRzZExlZlo4L2pWZ3R0Y2NEWTdxTE1zUktXRGEvK3FYWWtZaENURE5SNHExdk5rR0Q5NHhhSTlBUVZOa295VVFPZjNIWWgyTmdWaiszaVU0RllaSTAwekw3WUVTWVpHMEtReDdHT3BHQldTZjNRc3A3RTZSSkdiQ2VPU1lFaDhJZGFpM2Ywbi9ESUZ5RUdXZTdiU09zNlFseGlZRFNDS2RGL0FxYWhGNDVnN3hQMjhUZWROMTArYmhLSDBPZ2FCOERIT3FjeTI1RTdUazVTZk0xWmp1QXRqbEtrYkZGZHpBUStHYVdtaHozZm53QmptM1BLRTM5eWdJWk03TGNNMHhUZTg5Rmk3QUhCd0owZVFGdEg1N3dFMkJHM3hvR1Qzb2hjd05Bc1U0ZnRQWDNJK3RrV0xHTFFkNENESEdHakRKSWtHZ2tZLytnNUJGeVozMGl4NTZzdHgzR0Y0d2Z6SEEyVjdtQVNoZ2dnOHIyVGdQbnVBTWFlSWFQL3hNRkQwL21wSm4xR2dsTDR4enJydkdjci9TeHpBSnFKTDV6eCtrSXRhTTI5MmJRWFJOVDJ3KzVvWHQySHgzeDlVRXZMdFNRWEVWaGo1SmVtNUxwNlFGUUh6SDhaQlFuYkNUc1lzN1Rid2FDZXpPMjQ2TFZ3KzczQlNFNmhyYVJuSm5na3JRSWxlZnNLWFFWMVZ0cDBYeWZhU1pMNjB0UDFuRmJIWkVBeFNjNWl4SXlqWnBFVWhnS25yeThDakFoMHE0S3RzcHJ6N1E1eHV0aXJYTCsvYnljdllkZzJvOGNoK1pBRVFRQ2NvcGE3dnZ6Y1RIZHFnNUtKOFBLbmppYW1zY1luNGNFT1I2T2pHRStYRzlDUXA5TWJtZHU4bGZJMjhhaEhHcGN0N3RscjBKaVhjTHdoYThoY1pOV3FoBIGwSUVhczFWTDVKUTd5Y0lXb0xORDlHYkh1NE1QMVlwMmozRWtuelVwUkRDYjZpUU9zeTM5czBHcVpRdVJDcTNhQjZ1YyswVFlyMGtWVHVEVHNPcy9oUWNYQmtYZDZWZG44UVFaSnV5dWNnZXdvRzZsd0xLcHpqSjBGQ2p2VlJlWks2OE9WVlVpbndCL3d3OFVGc2NRbWZONXJTYWdnQkNBQUFBQUFBQUFBQUFBQT09In0AAAAAAACggDCCAykwggIRoAMCAQICBNGvJJUwDQYJKoZIhvcNAQELBQAwQDEqMCgGCgmSJomT8ixkARkWGmh0dHA6Ly9sb2NhbGhvc3Q6OTAwMC9hdXRoMRIwEAYDVQQDDAkzMzQyMTM5OTQwHhcNMTcxMjE0MTQ0OTE2WhcNMTgxMjE0MTQ0OTE2WjAUMRIwEAYDVQQDDAk2NzA0NjcyMTAwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCHqe+z3ucYT1yRKF/T6atAogGIT9Cp8PTuDV7lloRFVPxaefOIhs0IvsO1csmuR8hCN0WJxdZZebD3H9QR9bVw0GoI/KPYzpsat23Whd5vW7iZO/JAL1p3QNinp083huOPX0PfweBdHC64CUwP0RGMsg8uAE2rUJ4hWx2Lgc5RCnY3YY6A0M72t97kWnfK+yyXd/f1iJ7AM0vurhxVluuP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");
		//new VeidblockLedgerConnector().sendAuthorizedVeidblock(vblURL, co);
	}

	public Representation<String> sendAuthorizedVeidblock(String vblURL, long owner, TokenCO obj) {

		try {
			
			RestClient restClient = RestClient.builder().baseUrl(vblURL).build();
			Representation<String> response = restClient.post("/vbla/add/"+owner, obj, null);
			return response ;
		} catch (VeidblockException e) {
			return new Representation<String>(502,"Internal error :"+e.getMessage());
		}
	}
	public Representation<String> fetchAuthorizedVeidblock(String vblURL, long owner) {

		try {
			RestClient restClient = RestClient.builder().baseUrl(vblURL).build();
			//System.out.println(vblURL + "/vbla/authorized/" + owner);
			Representation<String> response = restClient.get("/vbla/authorized/" + owner, null);
			return response ;
		} catch (VeidblockException e) {
			return new Representation<String>(502,"Internal error :"+e.getMessage());
		}
	}
}