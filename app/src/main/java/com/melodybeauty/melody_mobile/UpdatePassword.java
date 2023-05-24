package com.melodybeauty.melody_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.melodybeauty.melody_mobile.AuthServices.AuthServices;

public class UpdatePassword extends AppCompatActivity implements View.OnClickListener{
    EditText etp_password, etp_conpassword;
    Button btp_update;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        etp_password = findViewById(R.id.etp_password);
        etp_conpassword = findViewById(R.id.etp_conpassword);
        btp_update = findViewById(R.id.btp_update);

        btp_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btp_update) {
            String password = etp_password.getText().toString().trim();
            String cpassword = etp_conpassword.getText().toString().trim();
            if (password.isEmpty()) {
                etp_password.setError("password tidak boleh kossong");
            } else if (password.equals("melodybeauty")) {
                etp_password.setError("password tidak boleh sama dengan password default");
            } else if (!password.equals(cpassword)){
                etp_password.setError("password harus sama");
                etp_conpassword.setError("password harus sama");
            } else if (password.length() < 8) {
                etp_password.setError("password tidak boleh kurang dari 8 karakter");
            } else {
                String token = UpdatePassword.this.getSharedPreferences("myPrefs", MODE_PRIVATE).getString("token", "");
//                AuthServices.updatepass(getApplicationContext(), token, password, new AuthServices.UpdatePasswordResponseListener() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Toast.makeText(UpdatePassword.this,response, Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(UpdatePassword.this, HomepageActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(String message) {
//                        Log.e("update" , message);
//                    }
//                });
            }
        }
    }
}