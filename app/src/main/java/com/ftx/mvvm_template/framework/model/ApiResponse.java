package com.ftx.mvvm_template.framework.model;

import java.util.List;


/**
 * Name : ApiResponse
 * <br> Purpose : We will use ApiResponse to communicate data from Repository to ViewModel and
 * ultimately to Activity. So if we get any singalData while fetching data from the remote api,
 * we will set Error in the ApiResponse, else we will set the list of Issue objects into it
 */

public class ApiResponse<T> {
    private List<T> listData;
    private T singalData;
    private int responseCode;

    public ApiResponse(List<T> listData) {
        this.listData = listData;
        this.singalData = null;
    }

    public ApiResponse(T singalData) {
        this.singalData = singalData;
        this.listData = null;
    }

    public ApiResponse() {
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    // Getters...
    public List<T> getListData() {
        return listData;
    }

    public T getSingalData() {
        return singalData;
    }

}