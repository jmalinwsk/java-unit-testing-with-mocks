package services;

import models.Reservation;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/** https://www.tutorialspoint.com/java/java_sending_email.htm */
public class SendEmail {

    public static void send(Reservation reservation, String to, String from, String host) {
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port","3025");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("You just reserved a hotel room!");
            message.setText("Your reservation ID is " + reservation.getIdentificator() + ".");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}