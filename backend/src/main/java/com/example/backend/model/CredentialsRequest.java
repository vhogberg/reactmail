package com.example.backend.model;

// Model for credentials, host, port and username + password + folder

public class CredentialsRequest {
    private final String host; // ex smtp.gmail.com, imap.gmail.com
    private final String port; // ex 465, 993
    private final String username; // ex viktorhogberg@gmail.com
    private final String password; // ex google app password
    private final String folder; // ex "INBOX"

    // constructor
    public CredentialsRequest(String host, String port, String username, String password, String folder) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.folder = folder;
    }

    // Getters (setters not needed)
    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFolder() {
        return folder;
    }

}