package idatt2105.peakquizbackend.service;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

  public static void main(String [] args) {
    String recipient = "larsmiloni@gmail.com";
    String sender = "sender@gmail.com";
    String host = "127.0.0.1";

    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);

    Session session = Session.getDefaultInstance(properties);

    try {
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(sender));
      message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
      message.setSubject("This is Subject");
      message.setText("This is a test mail");
      Transport.send(message);
      System.out.println("Mail successfully sent");
    } catch (MessagingException mex) {
      mex.printStackTrace();
    }
  }
}
