package com.example.backend.service;

import com.example.backend.model.Attachment;
import com.example.backend.model.EmailMessage;
import jakarta.mail.*;
import java.text.SimpleDateFormat;
import java.util.*;

// MailReceiver class that retrieves the latest emails in inbox

/*
Postman test for receiving emails:

* POST http://localhost:8080/api/receive-emails

{
  "host": "imap.gmail.com",
  "port": "993",
  "username": "x.x@gmail.com",
  "password": "xxx"
}
*/

public class MailReceiver {
    public static List<EmailMessage> receiveMailAsList(String host, String port, String username, String password) throws MessagingException {
        List<EmailMessage> emailMessages = new ArrayList<>();

        // e-postserverns inställningar
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", port);
        properties.put("mail.imaps.ssl.enable", "true");
        properties.put("mail.imaps.timeout", "5000");
        System.setProperty("mail.imaps.ssl.protocols", "TLSv1.2");

        try {
            // session för att ansluta till imap
            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imaps");
            store.connect(username, password);

            // öppna inkorgen / "INBOX"
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // hämta alla meddelanden
            Message[] messages = inbox.getMessages();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

            // Process the 10 most recent messages (or all if fewer than 10)
            for (int i = 0; i < Math.min(10, messages.length); i++) {
                Message message = messages[messages.length - 1 - i];

                EmailMessage emailMessage = new EmailMessage();
                emailMessage.setId(String.valueOf(message.getMessageNumber()));

                // Set sender information
                Address[] fromAddresses = message.getFrom();
                if (fromAddresses != null && fromAddresses.length > 0) {
                    emailMessage.setFrom(fromAddresses[0].toString());
                }

                // Set subject
                emailMessage.setSubject(message.getSubject() != null ? message.getSubject() : "(No subject)");

                // Set date
                Date sentDate = message.getSentDate();
                if (sentDate != null) {
                    emailMessage.setDate(dateFormat.format(sentDate));
                } else {
                    emailMessage.setDate("Unknown date");
                }

                // Get content
                try {
                    Object content = message.getContent();
                    if (content instanceof String) {
                        emailMessage.setBody((String) content);
                    } else if (content instanceof Multipart) {
                        Multipart multipart = (Multipart) content;
                        StringBuilder bodyText = new StringBuilder();
                        List<Attachment> attachments = new ArrayList<>();

                        for (int j = 0; j < multipart.getCount(); j++) {
                            BodyPart bodyPart = multipart.getBodyPart(j);
                            String disposition = bodyPart.getDisposition();

                            if (disposition != null && (disposition.equalsIgnoreCase(Part.ATTACHMENT) ||
                                    disposition.equalsIgnoreCase(Part.INLINE))) {
                                // attachment
                                Attachment attachment = new Attachment();
                                attachment.setName(bodyPart.getFileName());
                                attachment.setSize(bodyPart.getSize());
                                attachment.setType(bodyPart.getContentType());
                                attachments.add(attachment);
                            } else {
                                // message body
                                Object partContent = bodyPart.getContent();
                                if (partContent instanceof String) {
                                    bodyText.append(partContent);
                                }
                            }
                        }

                        emailMessage.setBody(bodyText.toString());
                        if (!attachments.isEmpty()) {
                            emailMessage.setAttachments(attachments);
                        }
                    }
                } catch (Exception e) {
                    emailMessage.setBody("Error! Not able to display message content");
                }

                emailMessages.add(emailMessage);
            }

            // stäng anslutningar
            inbox.close(false);
            store.close();

        } catch (MessagingException e) {
            throw e;
        }

        return emailMessages;
    }
}
