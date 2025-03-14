package com.example.backend.controller;

import com.example.backend.model.CredentialsRequest;
import com.example.backend.model.EmailMessage;
import com.example.backend.service.MailReceiver;
import com.example.backend.service.MailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// REST API CONTROLLER

@RestController
@CrossOrigin(origins = "*") // CORS, for development only
@RequestMapping("/api")
public class EmailController {

    // Sending emails, api request ends with /send-email
    @PostMapping("/send-email")
    public ResponseEntity<Map<String, String>> sendEmail(
            // swapped to using requestparam instead of the emailrequest class, for convenience and handling files
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
                    File tempFile = File.createTempFile("email-attachment-", multipartFile.getOriginalFilename());
                    multipartFile.transferTo(tempFile);
                    attachmentFiles.add(tempFile);
                }
            }

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
        // Try retrieve inbox of emails via MailReceiver as a list
        try {
            List<EmailMessage> messages = MailReceiver.receiveMailAsList(
                    request.getHost(),
                    request.getPort(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getFolder() // Get folder too
            );

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>()); // if it didn't work for some reason, err code 500
        }
    }
}
