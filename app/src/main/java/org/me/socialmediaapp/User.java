package org.me.socialmediaapp;

public class User {

    private String mEmail, mName, mPassword;

    public User() {}

    public User(String email, String name, String password) {
        mEmail = email;
        mName = name;
        mPassword = password;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getName() {
        return mName;
    }

    public String getPassword() {
        return mPassword;
    }
}
