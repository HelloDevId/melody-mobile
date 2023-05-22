package com.melodybeauty.melody_mobile.AuthServices;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melodybeauty.melody_mobile.HomepageActivity;
import com.melodybeauty.melody_mobile.Model.Product;
import com.melodybeauty.melody_mobile.RegisterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthServices {
    //connect host url
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";
    private static String ImageProduct = HOST + "/foto/product/";

    public static String getImageProduct() {
        return ImageProduct;
    }

    //interface listener register
    public interface RegisterResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    //interface listener login
    public interface LoginResponseListener {
        void onSuccess(JSONObject response);
        void onError(String message);
    }
    //interface listener fetch data product
    public interface ProductResponseListener {
        void onSuccess(List<Product> productList);
        void onError(String message);
    }
    //method request menggunakan library volley untuk register user
    public static void register(Context context, String name, String email, String password, RegisterResponseListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.0.118:8000/api/auth/register", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("User berhasil terdaftar")){
                        JSONObject userObj = jsonObject.getJSONObject("data");
                        listener.onSuccess(userObj);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                if (message.equals("Email sudah terdaftar")) {
                                    listener.onError("Email sudah terdaftar , Silahkan gunakan email yang lain");
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal register: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal register: network response is null");
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //method request menggunakan library volley untuk login user
    public static void login(Context context, String email, String pass, LoginResponseListener listener) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "signin", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("login successfull")){
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        String token = jsonObject.getString("token");
                        SharedPreferences.Editor editor = context.getSharedPreferences("myPrefs", MODE_PRIVATE).edit();
                        editor.putString("token", token);
                        editor.putBoolean("isLogin", true);
                        editor.apply();

                        //cek password user
                        listener.onSuccess(userObj);
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                if (message.equals("incorrect email")) {
                                    listener.onError("Email Anda Belum Terdaftar");
                                } else if (message.equals("incorrect password")) {
                                    listener.onError("Password Anda Salah");
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal Login: " + e.getMessage());
                            }
                        }
                        else{
                            listener.onError("Gagal Login: network response is null");
                            Log.e("AuthServices", "Error: " + error.getMessage());
                        }
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //fetch data product
    public static void product(Context context, final ProductResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "product",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray();
                            List<Product> productList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject productObj = jsonArray.getJSONObject(i);
                                String id = productObj.getString("id");
                                String name = productObj.getString("name");
                                String image = productObj.getString("image");
                                String desciption = productObj.getString("description");
                                String price = productObj.getString("price");
                                String idKategori = productObj.getString("id_kategori");
                                String jumlahTerjual = productObj.getString("jumlah_terjual");
                                String deleteAt = productObj.getString("delete_at");
                                String createAtStr = productObj.getString("create_at");
                                String updateAtStr = productObj.getString("update_at");

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                                Date createAt;
                                Date updateAt;
                                try {
                                    createAt = sdf.parse(createAtStr);
                                    updateAt = sdf.parse(updateAtStr);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    continue;
                                }

                                Product product = new Product(id,name,image,desciption,price,idKategori,jumlahTerjual,deleteAt,createAt,updateAt);
                                productList.add(product);
                            }
                            listener.onSuccess(productList);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String message = jsonObject.getString("message");
                                listener.onError(message);
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                listener.onError("Gagal mendapatkan product: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan product: network response is null");
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
