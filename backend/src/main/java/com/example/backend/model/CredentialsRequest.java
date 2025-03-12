package com.example.backend.model;

// Model for credentials, host, port and username + password

public class CredentialsRequest {
    private String host; // ex smtp.gmail.com, imap.gmail.com
    private String port; // ex 465, 993
    private String username; // ex viktorhogberg@gmail.com
    private String password; // ex google app password

    // Getters and setters
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}