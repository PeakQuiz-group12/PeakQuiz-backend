package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  private final idatt2105.peakquizbackend.service.UserRepository userRepository;

  public EmailService(idatt2105.peakquizbackend.service.UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public void sendEmail(String fromUsername, String subject, String messageToSend) {
    System.out.println("Start sender mail");

    final String username = "peakquizgruppe12@gmail.com";
    final String password = "sbnx lxxo nfsq clnd ";

    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");

    Session session = Session.getInstance(prop,
      new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(username, password);
        }
      });

    try {
      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(username));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("peakquizgruppe12@gmail.com"));
      message.setSubject(subject);
      message.setText("Sent from user with email: " + userRepository.findUserByUsername(fromUsername).getEmail() + "\nMessage: " + messageToSend);

      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
    System.out.println("Slutt sender mail");
  }


  private Session createEmailSession() {
    final String username = "peakquizgruppe12@gmail.com";
    final String password = "sbnx lxxo nfsq clnd ";

    Properties prop = new Properties();
    prop.put("mail.smtp.host", "smtp.gmail.com");
    prop.put("mail.smtp.port", "587");
    prop.put("mail.smtp.auth", "true");
    prop.put("mail.smtp.starttls.enable", "true");

    return Session.getInstance(prop, new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }

  private String generateTemporaryPassword(int length) {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();
    StringBuilder tempPassword = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      tempPassword.append(characters.charAt(random.nextInt(characters.length())));
    }
    return tempPassword.toString();
  }

  public void forgotPassword(String email) {
    if (email == null || email.trim().isEmpty()) {
      System.out.println("Email address is null or empty.");
      return; // Exit the method if no valid email is provided
    }

    Session session = createEmailSession();
    try {
      User user = userRepository.findUserByEmail(email);
      if (user == null) {
        System.out.println("No user found with email: " + email);
        return; // Exit the method if no user is found
      }

      String tempPassword = generateTemporaryPassword(10);
      String hashedPassword = bCryptPasswordEncoder.encode(tempPassword);
      userRepository.updateUserPassword(email, hashedPassword);

      Message message = new MimeMessage(session);
      message.setFrom(new InternetAddress(email)); // Assuming 'username' is a valid email
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
      message.setSubject("PeakQuiz Temporary Password");
      message.setText("You have requested to reset your password. Please use the following temporary password to log in: " + tempPassword
        + "\nIt is recommended to change your password immediately after logging in.");
      Transport.send(message);
    } catch (MessagingException e) {
      e.printStackTrace();
      // Consider using a logger to log this exception and possibly inform the user/administrator of the failure
    }
  }

}