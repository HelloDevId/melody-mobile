package com.melodybeauty.melody_mobile.Retrofit.ApiServices;

import com.melodybeauty.melody_mobile.Model.Product;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiServices {
    @FormUrlEncoded
    @POST("signin")
    Call<ResponseBody> signin (
            @Field("email") String email,
            @Field("password") String password
    );
    @GET("product")
    Call<ResponseBody> getProducts();
}
