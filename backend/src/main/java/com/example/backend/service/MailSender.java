package com.example.backend.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import java.io.File;
import java.util.List;

// MailSender class that sends an email

public class MailSender {

    public static void sendMail(String host, String port, String username, String password,
                                String to, String subject, String bodyText, List<File> attachments) {
        // e-postserverns inställningar
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.timeout", "10000");
        properties.put("mail.smtp.connectiontimeout", "10000");
        System.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

        // session autentisering
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // skapar e-postmeddelandet
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Check if we have attachments
            if (attachments != null && !attachments.isEmpty()) {
                // Create multipart message
                Multipart multipart = new MimeMultipart();

                // Text part
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(bodyText);
                multipart.addBodyPart(textPart);

                // Attachments
                for (File file : attachments) {
                    if (file.exists() && file.isFile()) {
                        MimeBodyPart attachmentPart = new MimeBodyPart();
                        DataSource source = new FileDataSource(file);
                        attachmentPart.setDataHandler(new DataHandler(source));
                        attachmentPart.setFileName(file.getName());
                        multipart.addBodyPart(attachmentPart);
                    }
                }

                // Set the complete message
                message.setContent(multipart);
            } else {
                // Simple text message
                message.setText(bodyText);
            }

            // Skicka e-post
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
