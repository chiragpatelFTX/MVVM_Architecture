package com.sql.projecttemplate.model.entities.response;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Name : RegisterResponse
 *<br> Purpose :This class will holds the parameter of register API response.
 */
public class RegisterResponse {
    @JsonProperty("token")
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String aToken) {
        mToken = aToken;
    }
}
