package com.example.belajarbahasa.models;

public class User {

    private String email;
    private String username; // Tambahkan field username
    private String level;

    // Constructor kosong (untuk Firebase)
    public User() {}

    // Constructor dengan email, username, dan level
    public User(String email, String username, String level) {
        this.email = email;
        this.username = username;
        this.level = level;
    }

    // Getter dan Setter untuk email, username, dan level
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
