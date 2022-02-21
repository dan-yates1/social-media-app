package org.me.socialmediaapp;

public class User {

    private String email, name, password, bio;

    public User() {
    }

    public User(String email, String name, String password, String bio) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
