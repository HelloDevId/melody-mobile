package com.melodybeauty.melody_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_signin;
    LinearLayout ly_create;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btn_signin = findViewById(R.id.btn_signin);
        ly_create = findViewById(R.id.ll_create);

        btn_signin.setOnClickListener(this);
        ly_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_signin) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v == ly_create){
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}