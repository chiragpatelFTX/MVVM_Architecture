package com.ftx.mvvm_template.model.rest;

import com.ftx.mvvm_template.TemplateApplication;
import com.ftx.mvvm_template.utils.Constants;
import com.ftx.mvvm_template.utils.network.RequestInterceptor;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;



/*
* This class will provide the Retrofit singleton method.
 * And Builder so don't need to create builder Every Java File.
*/

/**
 * Name : ServiceGenerator
 *<br> Purpose :
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
     *<br> Purpose : This method will create service single time for our interface a
     * and later on we can call all the method.
     * @param serviceClass  : {@link RestApis} class.
     * @param <S>   : return {@link RestApis}
     * @return
     */
    public static <S> S createService(Class<S> serviceClass)
    {

        HashMap<String, String> mHeaders = new HashMap<>();
        mHeaders.put("Accept", "application/json");
        mHeaders.put("Content-Type", "application/json");
        httpClient.addInterceptor(new RequestInterceptor(mHeaders));

        OkHttpClient client = httpClient.build();

        // if you want to set time out.

        /*httpClient.connectTimeout(10, TimeUnit.MINUTES);
        httpClient.writeTimeout(10, TimeUnit.MINUTES);
        httpClient.readTimeout(10, TimeUnit.MINUTES);*/

        /*
         *  If you want to put headers in each requests. then do like this.
         */


        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
