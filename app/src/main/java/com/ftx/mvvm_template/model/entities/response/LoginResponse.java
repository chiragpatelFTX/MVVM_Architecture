package com.ftx.mvvm_template.model.entities.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ftx.mvvm_template.framework.model.ApiResponse;

/**
 * Name : LoginResponse
 *<br> Purpose :This class will holds the parameter of login API response.
 */
public class LoginResponse extends ApiResponse<LoginResponse> {

    @JsonProperty("token")
    private String mToken;

    public String getToken() {
        return mToken;
    }

    public void setToken(String aToken) {
        mToken = aToken;
    }
}
