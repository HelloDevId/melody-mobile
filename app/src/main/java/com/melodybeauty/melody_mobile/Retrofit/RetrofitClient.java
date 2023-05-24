package com.melodybeauty.melody_mobile.Retrofit;

import com.melodybeauty.melody_mobile.Retrofit.ApiServices.ApiServices;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";
    private static Retrofit retrofit;

    public  static Retrofit getInstance(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public  static ApiServices getApiServices(){
        ApiServices apiServices = getInstance().create(ApiServices.class);

        return apiServices;
    }
}
