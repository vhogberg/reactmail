package com.example.backend.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

// MailSender class that sends an email

/*
Postman test for sending an email:

POST http://localhost:8080/api/send-email
{
  "host": "smtp.gmail.com",
  "port": "465",
  "username": "x.x@gmail.com",
  "password": "xxx",
  "to": "y.y@gmail.com",
  "subject": "ReactMail - Test Email",
  "body": "This is a test email from ReactMail"
}
*/

public class MailSender {
    public static void sendMail(String host, String port, String username, String password,
                                String to, String subject, String bodyText) throws MessagingException {
        // e-postserverns inställningar
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.timeout", "5000");
        properties.put("mail.smtp.connectiontimeout", "5000");
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        // session autentisering
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // skapar e-postmeddelandet
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(bodyText);

        // Skicka e-post
        Transport.send(message);
    }
}
