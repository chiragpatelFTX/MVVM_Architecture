package com.ftx.mvvm_template.utils.network;


import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p/>
 * This class is used to set headers before calling web service.
 * Here need to pass list of the header in the constructor
 * <p/>
 * <p/>
 * How to use:
 * <p/>
 * <Code>
 * ArrayMap<String,String> headers = new Arraymap<>();
 * headers.put("header1","header-value1");
 * final RequestInterceptor mRequestInterceptor = new RequestInterceptor(headers);
 * OkHttpClient client = new OkHttpClient.Builder().addInterceptor(mRequestInterceptor).build();
 * </Code>
 *
 * @author hrdudhat
 */
public class RequestInterceptor implements Interceptor {

    protected HashMap<String, String> mHeaders;

    public RequestInterceptor(HashMap<String, String> aHeaders) {
        mHeaders = aHeaders;
    }

    @Override
    public Response intercept(Chain aChain) throws IOException {

        Request.Builder builder = aChain.request().newBuilder();
        for (String key :
                mHeaders.keySet()) {
            builder.addHeader(key, mHeaders.get(key));
        }
        Request newRequest = builder.build();

        return aChain.proceed(newRequest);
    }
}