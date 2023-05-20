package com.melodybeauty.melody_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melodybeauty.melody_mobile.AuthServices.AuthServices;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iv_back;
    TextView tv_signin;
    EditText etr_name,etr_email,etr_pass,etr_con_pass;
    Button btr_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        iv_back = findViewById(R.id.iv_back);
        tv_signin = findViewById(R.id.tv_sign_in);
        btr_signup = findViewById(R.id.btn_signup);

        etr_name = findViewById(R.id.et_name);
        etr_email = findViewById(R.id.et_email);
        etr_pass = findViewById(R.id.et_password);
        etr_con_pass = findViewById(R.id.et_conn_password);


        iv_back.setOnClickListener(this);
        tv_signin.setOnClickListener(this);
        btr_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == iv_back) {
            Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else if (v == tv_signin){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (v == btr_signup){
            String name = etr_name.getText().toString().trim();
            String email = etr_email.getText().toString().trim();
            String password = etr_pass.getText().toString().trim();
            String con_password = etr_con_pass.getText().toString().trim();

            if (name.isEmpty()) {
                etr_name.setError("Enter your name");
            } else if (email.isEmpty()) {
                etr_email.setError("Enter your email");
            } else if (!isValidEmail(email)) {
                etr_email.setError("Enter a valid email address");
            } else if (password.isEmpty()) {
                etr_pass.setError("Enter your password");
            } else if (con_password.isEmpty()) {
                etr_con_pass.setError("Enter your confirm password");
            } else if (password.length() < 8) {
                etr_pass.setError("Password cannot be less than 8 characters");
            } else if (!password.equals(con_password)) {
                etr_pass.setError("Passwords cannot be different");
                etr_con_pass.setError("Passwords cannot be different");
            } else {
                AuthServices.register(this, name, email, password, new AuthServices.RegisterResponseListener() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Berhasil Mengaktifkan Akun Anda", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }
    public static boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}