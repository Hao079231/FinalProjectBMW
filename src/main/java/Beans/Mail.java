package Beans;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeUtility;
import java.util.Properties;

public class Mail {
  private static final String EMAIL_ID = "haotrung123test@gmail.com"; // Consider moving to config file
  private static final String EMAIL_PASSWORD = "ekktlyrstylcghkj"; // Consider moving to config file

  public static boolean sendMail(String recipientMailId, String subject, String body) throws MessagingException {
    Properties properties = new Properties();
    properties.put("mail.smtp.host", "smtp.gmail.com");
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");
    properties.put("mail.smtp.port", "587");

    Session session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(EMAIL_ID, EMAIL_PASSWORD);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(EMAIL_ID));
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientMailId));
      message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
      message.setContent(body, "text/html; charset=UTF-8");

      Transport.send(message);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      throw new MessagingException("Failed to send email: " + e.getMessage());
    }
  }
}
