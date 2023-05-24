package com.melodybeauty.melody_mobile.AuthServices;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.melodybeauty.melody_mobile.HomepageActivity;
import com.melodybeauty.melody_mobile.Model.Kategori;
import com.melodybeauty.melody_mobile.Model.Post;
import com.melodybeauty.melody_mobile.Model.Product;
import com.melodybeauty.melody_mobile.Model.User;
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

import retrofit2.Callback;

public class AuthServices {
    //connect host url
    private static String HOST = "http://192.168.43.199:8000";
    private static String API = HOST + "/api/";
    private static String ImageProduct = HOST + "/foto/product/";
    private static String ImagePost = HOST + "/foto/post/";

    public static String getImageProduct() {
        return ImageProduct;
    }

    public static String getImagePost() {
        return ImagePost;
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
    public interface UpdatePasswordResponseListener{
        void onSuccess(String response);
        void onError(String message);
    }
    public interface UserDataResponseListener {
        void onSuccess(User user);
        void onError(String message);
    }
    public interface LogoutResponseListener {
        void onSuccess(String message);
        void onError(String message);
    }
    public interface KategoriResponseListener {
        void onSuccess(List<Kategori> kategoris);
        void onError(String message);
    }
    public interface PostResponseListener {
        void onSuccess(List<Post> posts);
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
    public static void product(Context context,final ProductResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "productall",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
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
                                    String createAtStr = productObj.getString("created_at");
                                    String updateAtStr = productObj.getString("updated_at");

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

                                    Product product = new Product(id, name, image, desciption, price, idKategori, jumlahTerjual, createAt, updateAt);
                                    productList.add(product);

                                }
                                listener.onSuccess(productList);
                            }
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
    //update password
    public static void updatepass(Context context, String token, String pass, final UpdatePasswordResponseListener listener ) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API + "updatepass",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("Password updated successfully")){
                                listener.onSuccess("Password updated successfully");
                            }
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
                                listener.onError("Gagal mengupdate password: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mengupdate: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("new_password", pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //get data user
    public static void getUserData(Context context, String token, final UserDataResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "userlogin",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("success")){
                                JSONObject userObj = jsonObject.getJSONObject("user");
                                String id = userObj.getString("id");
                                String name = userObj.getString("name");
                                String image = userObj.getString("image");
                                String password = userObj.getString("password");
                                String email = userObj.getString("email");
                                String jenisKelamin = userObj.getString("jenis_kelamin");
                                String JenisKulit = userObj.getString("jenis_kulit");
                                String tgllahir = userObj.getString("tanggal_lahir");
                                String nohp = userObj.getString("no_hp");
                                String Alamat = userObj.getString("alamat");
                                User user = new User(id, name,image,email,jenisKelamin, JenisKulit, tgllahir,nohp,Alamat);
                                listener.onSuccess(user);
                            }
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
                                listener.onError("Gagal mendapatkan data user: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan data user: network response is null");
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //logout
    public static void logOut(Context context, String token, LogoutResponseListener listener) {
        StringRequest request = new StringRequest(Request.Method.POST, API + "logout", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }
    //kategori6
    public static void kategori(Context context, final KategoriResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "kategori",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                List<Kategori> kategoriList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject kategoriObj = jsonArray.getJSONObject(i);
                                    String id = kategoriObj.getString("id");
                                    String name = kategoriObj.getString("name");

                                    Kategori kategori = new Kategori(id, name);
                                    kategoriList.add(kategori);
                                }
                                listener.onSuccess(kategoriList);
                            }
                        } catch (JSONException e) {
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
                                listener.onError("Gagal mendapatkan kategori: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan kategori: network response is null");
                        }
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
    //product sesuai kategori
    public static void kategoriproduct(Context context,String id,final ProductResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "product/"+ id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("status");
                            if (message.equals("success")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
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
                                    String createAtStr = productObj.getString("created_at");
                                    String updateAtStr = productObj.getString("updated_at");

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

                                    Product product = new Product(id, name, image, desciption, price, idKategori, jumlahTerjual, createAt, updateAt);
                                    productList.add(product);
                                }
                                listener.onSuccess(productList);
                            }
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
    //post user
    public static void post(Context context,String token,final PostResponseListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API + "posts/user",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            if (message.equals("Data retrieved successfully")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("posts");
                                List<Post> postList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject postObj = jsonArray.getJSONObject(i);
                                    String id = postObj.getString("id");
                                    String title = postObj.getString("title");
                                    String slug = postObj.getString("slug");
                                    String image = postObj.getString("image");
                                    String content = postObj.getString("content");
                                    String dateStr = postObj.getString("date");
                                    String createAtStr = postObj.getString("created_at");
                                    String updateAtStr = postObj.getString("updated_at");

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date createAt;
                                    Date updateAt;
                                    Date date;
                                    try {
                                        createAt = sdf.parse(createAtStr);
                                        updateAt = sdf.parse(updateAtStr);
                                        date = sdf1.parse(dateStr);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        continue;
                                    }

                                    Post post = new Post(id, title, slug, image, content, date, createAt, updateAt);
                                    postList.add(post);
                                }
                                listener.onSuccess(postList);
                            }
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
                                listener.onError("Gagal mendapatkan posts: " + e.getMessage());
                            }
                        } else {
                            listener.onError("Gagal mendapatkan posts: network response is null");
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
