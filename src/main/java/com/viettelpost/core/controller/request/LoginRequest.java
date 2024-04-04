package com.viettelpost.core.controller.request;

public class LoginRequest {
    private String user;
    private String pass;
    private String captcha;

    // Constructors, getters, and setters
    // You can generate these using your IDE or write them manually.

    // Example of getters and setters:
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
