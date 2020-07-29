package com.ftx.mvvm_template.model.rest;

import com.ftx.mvvm_template.BuildConfig;
import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.utils.network.RequestInterceptor;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;



/*
 * This class will provide the Retrofit singleton method.
 * And Builder so don't need to create builder Every Java File.
 */

/**
 * Name : ServiceGenerator
 * <br> Purpose :
 * This class will provide the Retrofit singleton method.
 * And Builder so don't need to create builder Every Java File.
 */
public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(TemplateApplication.getAppInstance().getJaksonConverter());


    /**
     * Name : ServiceGenerator createService
     * <br> Purpose : This method will create service single time for our interface a
     * and later on we can call all the method.
     *
     * @param serviceClass : {@link RestApis} class.
     * @param <S>          : return {@link RestApis}
     * @return
     */
    public static <S> S createService(Class<S> serviceClass) {
        /*
         *  If you want to put headers in each requests. then do like this.
         */
        HashMap<String, String> mHeaders = new HashMap<>();
        mHeaders.put("Accept", "application/json");
        mHeaders.put("Content-Type", "application/json");
        httpClient.addInterceptor(new RequestInterceptor(mHeaders));

        /*
         *  If you want to display network request log. then do like this
         */
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add logging as last interceptor
            httpClient.addInterceptor(logging);
        }
        // if you want to set time out.

        /*httpClient.connectTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        httpClient.readTimeout(10, TimeUnit.MINUTES);*/

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
