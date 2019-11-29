package com.sql.projecttemplate.model.entities.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseResponse<T> {

    @JsonProperty("data")
    T data;

    public T getData() {
        return data;
    }

    public void setData(T aData) {
        data = aData;
    }
}
