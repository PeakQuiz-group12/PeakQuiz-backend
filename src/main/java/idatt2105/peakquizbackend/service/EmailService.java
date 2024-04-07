package idatt2105.peakquizbackend.service;

import idatt2105.peakquizbackend.exceptions.UserNotFoundException;
import idatt2105.peakquizbackend.model.User;
import idatt2105.peakquizbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Service class for sending emails and handling password resets.
 */
@Service
public class EmailService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepository;

    private static final int GENERATED_PASSWORD_LENGTH = 10;
    private static final String USERNAME = "peakquizgruppe12@gmail.com";
    private static final String PASSWORD = "sbnx lxxo nfsq clnd ";

    public EmailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Sends an email.
     * 
     * @param fromUsername
     *            The username of the sender
     * @param subject
     *            The subject of the email
     * @param messageToSend
     *            The message content of the email
     */
    public void sendEmail(String fromUsername, String subject, String messageToSend) throws MessagingException {
        System.out.println("Start sender mail");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(USERNAME));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("peakquizgruppe12@gmail.com"));
        message.setSubject(subject);
        User fromUser = userRepository.findUserByUsername(fromUsername).orElseThrow(UserNotFoundException::new);
        message.setText("Sent from user with email: " + fromUser.getEmail() + "\nMessage: " + messageToSend);

        Transport.send(message);
        System.out.println("Slutt sender mail");
    }

    private Session createEmailSession() {

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, PASSWORD);
            }
        });
    }

    /**
     * Generates a temporary password.
     * 
     * @return The generated temporary password
     */
    private String generateTemporaryPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder tempPassword = new StringBuilder(GENERATED_PASSWORD_LENGTH);
        for (int i = 0; i < GENERATED_PASSWORD_LENGTH; i++) {
            tempPassword.append(characters.charAt(random.nextInt(characters.length())));
        }
        return tempPassword.toString();
    }

    /**
     * Sends a temporary password to the user's email address to reset their password.
     * 
     * @param email
     *            The email address of the user requesting the password reset
     */
    public void forgotPassword(String email) throws MessagingException {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email address is null or empty.");
            return; // Exit the method if no valid email is provided
        }

        Session session = createEmailSession();
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            System.out.println("No user found with email: " + email);
            return; // Exit the method if no user is found
        }

        String tempPassword = generateTemporaryPassword();
        String hashedPassword = bCryptPasswordEncoder.encode(tempPassword);
        userRepository.updateUserPassword(email, hashedPassword);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email)); // Assuming 'username' is a valid email
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("PeakQuiz Temporary Password");
        message.setText(
                "You have requested to reset your password. Please use the following temporary password to log in: "
                        + tempPassword + "\nIt is recommended to change your password immediately after logging in.");
        Transport.send(message);

    }
}