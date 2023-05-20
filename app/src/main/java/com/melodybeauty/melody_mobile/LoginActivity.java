package com.melodybeauty.melody_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    TextView tv_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        iv_back = findViewById(R.id.iv_back);
        tv_signup = findViewById(R.id.tv_sign_up);

        iv_back.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_back) {
            Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else if(v == tv_signup){
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}