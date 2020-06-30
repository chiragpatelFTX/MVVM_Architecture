package com.ftx.mvvm_template.model.entities.request;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Name : RegisterRequest
 *<br> Purpose :This class will keep the request parameters for register
 */
public class RegisterRequest {

    @JsonProperty("email")
    private String mEmail;

    @JsonProperty("password")
    private String mPassword;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String aEmail) {
        mEmail = aEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String aPassword) {
        mPassword = aPassword;
    }
}
