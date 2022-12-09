package org.acreo.activation;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivationEmail {
	
	public boolean sendEmail(final String from, String to, final String password, String activationUrl) throws Exception {

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {
			//
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject("Welcome Veidblock - Activation Email");
			message.setText("Please click on the following link to activate you account \n" + activationUrl
					+ " \nIf it is not working then please open your browser and paste this link into browser.  ");
			Transport.send(message);
			return true;

		} catch (MessagingException e) {
			throw new Exception(e);
		}
	}

	public static void main(String[] args) throws Exception {
		// Recipient's email ID needs to be mentioned.
		String to = "aghafoor77@gmail.com";
		final String from = "veidblock@gmail.com";
		final String password = "Agha1234_";
		String activationCode = "hjsdfjhsdjhfdfxmvbmnfkjdsfkkjkckfskjhdkjshdkjhckkjcbsjkhcskjfhksjdyfsierfisfdfbjsdbckhfsdsiuyfkvvxkbkjhfksdjkfsdkfbskcbkjcjksdhfjksddfjdfbskjhfsdhfksfxchjkxhfkjsfdsfusdfufcjkxdfsdfhshdfkksdfksdfkhfhsdkfhskdfhAAA";
		new ActivationEmail().sendEmail(from, to, password, activationCode);
	}
}