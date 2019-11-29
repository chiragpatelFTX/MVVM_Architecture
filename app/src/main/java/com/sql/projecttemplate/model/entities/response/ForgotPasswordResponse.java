package com.sql.projecttemplate.model.entities.response;


/**
 * Name : ForgotPasswordResponse
 *<br> Purpose :This class will holds the parameter of forgot password API response.
 */
public class ForgotPasswordResponse {

    private int mStatus;
    private String mMessage;

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
