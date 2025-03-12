package com.example.backend.controller;

import com.example.backend.model.CredentialsRequest;
import com.example.backend.model.EmailMessage;
import com.example.backend.model.EmailRequest;
import com.example.backend.service.MailReceiver;
import com.example.backend.service.MailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Map<String, String>> sendEmail(@RequestBody EmailRequest request) {
        Map<String, String> response = new HashMap<>();

        // Try to send via MailSender
        try {
            MailSender.sendMail(
                    request.getHost(),
                    request.getPort(),
                    request.getUsername(),
                    request.getPassword(),
                    request.getTo(),
                    request.getSubject(),
                    request.getBody()
            );

            response.put("status", "success");
            response.put("message", "Email sent successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) { // if it didn't work for some reason, err code 500
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
                    request.getPassword()
            );

            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new ArrayList<>()); // if it didn't work for some reason, err code 500
        }
    }
}
