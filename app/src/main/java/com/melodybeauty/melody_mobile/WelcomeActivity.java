package com.melodybeauty.melody_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melodybeauty.melody_mobile.AuthServices.AuthServices;
import com.melodybeauty.melody_mobile.Model.Product;
import com.melodybeauty.melody_mobile.fragment.SemuaFragment;

import java.text.SimpleDateFormat;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //cek user login
        SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        boolean isLogin = preferences.getBoolean("isLogin", false);

        if (isLogin) {
            Intent intent = new Intent(WelcomeActivity.this, HomepageActivity.class);
            startActivity(intent);
            finish();
        }

        btn_signin = findViewById(R.id.btn_signin);
        btn_signin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_signin) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}