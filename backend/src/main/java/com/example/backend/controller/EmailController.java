package com.example.backend.controller;

import com.example.backend.model.CredentialsRequest;
import com.example.backend.model.EmailMessage;
import com.example.backend.service.MailReceiver;
import com.example.backend.service.MailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

import com.example.backend.model.Attachment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

// REST API CONTROLLER

/*
Postman test for receiving emails on API:

* POST http://localhost:8080/api/receive-emails

{
  "host": "imap.gmail.com",
  "port": "993",
  "username": "x.x@gmail.com",
  "password": "xxx",
  "folder": "INBOX"
}
*/

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

@RestController
@CrossOrigin(origins = "*") // CORS, for development only
@RequestMapping("/api")
public class EmailController {

    // store attachments temporarily in memory for download
    private static final Map<String, Attachment> attachmentCache = new HashMap<>();

    // Sending emails, api request ends with /send-email
    @PostMapping("/send-email")
    public ResponseEntity<Map<String, String>> sendEmail(
            // swapped to using requestparam instead of the emailrequest class, for handling files
            @RequestParam String host,
            @RequestParam String port,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam(required = false) List<MultipartFile> attachments) { // not required to send files

        Map<String, String> response = new HashMap<>();

        try {
            // Convert multipart files to File objects
            List<File> attachmentFiles = new ArrayList<>();
            if (attachments != null) {
                for (MultipartFile multipartFile : attachments) {
                    File tempFile = File.createTempFile("attachment-", multipartFile.getOriginalFilename());
                    multipartFile.transferTo(tempFile);
                    attachmentFiles.add(tempFile);
                }
            }

            // Use mailsender with given param
            MailSender.sendMail(
                    host, port, username, password, to, subject, body, attachmentFiles
            );

            response.put("status", "success");
            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    // Receiving/retrieving emails, api request ends with /receive-emails
    @PostMapping("/receive-emails")
    public ResponseEntity<List<EmailMessage>> receiveEmails(@RequestBody CredentialsRequest request) {
        // Try retrieve set of emails via MailReceiver as a list
        try {
            List<EmailMessage> messages = MailReceiver.receiveMailAsList(
                    request.getHost(),
                    request.getPort(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getFolder() // Get folder too
            );

            // get attachments and add them to cache
            for (EmailMessage message : messages) {
                if (message.getAttachments() != null) {
                    for (Attachment attachment : message.getAttachments()) {
                        // a unique ID for each attachment
                        String attachmentId = UUID.randomUUID().toString();
                        attachment.setId(attachmentId);

                        // store in cache
                        attachmentCache.put(attachmentId, attachment);
                    }
                }
            }

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>()); // if it didn't work for some reason, err code 500
        }
    }

    // Get endpoint for downloading attachments
    @GetMapping("/download-attachment/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable String id) {
        Attachment attachment = attachmentCache.get(id); // get an attachment via its id from cache

        // if attachment with that id does not exist
        if (attachment == null) {
            return ResponseEntity.notFound().build();
        }

        // create HTTP headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(attachment.getType()));
        headers.setContentDispositionFormData("attachment", attachment.getName());
        return new ResponseEntity<>(attachment.getContent(), headers, HttpStatus.OK);
    }
}
