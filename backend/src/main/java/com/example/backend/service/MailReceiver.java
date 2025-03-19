package com.example.backend.service;

import com.example.backend.model.Attachment;
import com.example.backend.model.EmailMessage;
import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;

import java.text.SimpleDateFormat;
import java.util.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;

// MailReceiver class that retrieves the latest emails in inbox for given folder

public class MailReceiver {
    public static List<EmailMessage> receiveMailAsList(String host, String port, String username, String password, String folderName) throws MessagingException {
        List<EmailMessage> emailMessages = new ArrayList<>();

        // e-postserverns inställningar
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", host);
        properties.put("mail.imaps.port", port);
        properties.put("mail.imaps.ssl.enable", "true");
        properties.put("mail.imaps.timeout", "5000");
        properties.put("mail.imaps.connectiontimeout", "5000");  // connection timeout
        properties.put("mail.imaps.fetchsize", "1048576");  // size optimization
        System.setProperty("mail.imaps.ssl.protocols", "TLSv1.2");

        try {
            // session för att ansluta till imap
            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imaps");
            store.connect(username, password);

            // Open the specified folder (default to INBOX if null/empty)
            Folder folder;
            if (folderName != null && !folderName.isEmpty()) {
                folder = store.getFolder(folderName);
            }
            else {
                folder = store.getFolder("INBOX");
            }
            folder.open(Folder.READ_ONLY);

            // hämta alla meddelanden
            Message[] messages = folder.getMessages();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

            // get the 10 most recent messages (or all if fewer than 10)
            for (int i = 0; i < Math.min(10, messages.length); i++) {
                Message message = messages[messages.length - 1 - i];
                EmailMessage emailMessage = new EmailMessage();

                // Set an id based on message number
                emailMessage.setId(String.valueOf(message.getMessageNumber()));

                // Get sender information
                Address[] fromAddresses = message.getFrom();
                if (fromAddresses != null && fromAddresses.length > 0) {
                    emailMessage.setFrom(fromAddresses[0].toString());
                }

                // Get subject if there is one, else say "no subject"
                if (message.getSubject() != null) {
                    emailMessage.setSubject(message.getSubject());
                }
                else {
                    emailMessage.setSubject("No subject");
                }

                // Get date
                Date sentDate = message.getSentDate();
                if (sentDate != null) {
                    emailMessage.setDate(dateFormat.format(sentDate));
                } else {
                    emailMessage.setDate("Unknown date");
                }

                // Get content and attachments if there are any
                try {
                    if (message.isMimeType("text/plain")) {
                        // Simple text email
                        emailMessage.setBody(message.getContent().toString());
                    } else if (message.isMimeType("multipart/*")) {
                        // Email with attachment
                        Multipart multipart = (Multipart) message.getContent();
                        List<Attachment> attachments = new ArrayList<>();
                        StringBuilder bodyText = new StringBuilder();

                        for (int i2 = 0; i2 < multipart.getCount(); i2++) {
                            MimeBodyPart part = (MimeBodyPart) multipart.getBodyPart(i2);

                            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                                // get attachment info
                                Attachment attachment = new Attachment();
                                attachment.setName(part.getFileName());
                                attachment.setType(part.getContentType());
                                attachment.setSize(part.getSize());

                                // read the attachment content for downloading
                                InputStream inputStream = part.getInputStream();
                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, bytesRead);
                                }
                                // set its content and add to list of total attachments
                                attachment.setContent(outputStream.toByteArray());
                                attachments.add(attachment);

                            } else if (part.getContent() instanceof String) {
                                // email body when there are attachments too
                                if (bodyText.length() > 0) {
                                    bodyText.append("\n\n");
                                }
                                bodyText.append(part.getContent().toString());
                            }
                        }

                        emailMessage.setBody(bodyText.toString());
                        if (!attachments.isEmpty()) {
                            emailMessage.setAttachments(attachments);
                        }

                    } else {
                        // for safety set with string too so we dont miss some type
                        emailMessage.setBody(message.getContent().toString());
                    }
                } catch (Exception e) {
                    emailMessage.setBody("Error! Not able to display message content");
                }
                emailMessages.add(emailMessage);
            }

            // stäng anslutningar
            folder.close(false);
            store.close();

        } catch (MessagingException e) {
            throw e;
        }

        // return list of email messages
        return emailMessages;
    }
}
