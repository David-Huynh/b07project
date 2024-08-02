package com.b07project2024.group1;

public class User {
    private String email;
    private String password;

    public User() {
        this.email = "";
        this.password = "";
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUser() {
        return this.email;
    }

    public String getPass() {
        return this.password;
    }

    public void setUser(String email) {
        this.email = email;
    }

    public void setPass(String password) {
        this.password = password;
    }
}
