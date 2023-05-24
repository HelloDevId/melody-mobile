package com.melodybeauty.melody_mobile.Retrofit;

import com.melodybeauty.melody_mobile.Model.Product;
import com.melodybeauty.melody_mobile.Retrofit.ApiServices.ApiServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthServices {

    public static void signin(String email, String password, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getApiServices().signin(email, password);
        call.enqueue(callback);
    }
    public static void getProducts(Callback<ResponseBody> callback) {
        Call<ResponseBody> call = RetrofitClient.getApiServices().getProducts();
        call.enqueue(callback);
    }
}

