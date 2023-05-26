    package com.melodybeauty.melody_mobile;

    import androidx.activity.result.ActivityResult;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.IntentSenderRequest;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.github.dhaval2404.imagepicker.ImagePicker;
    import com.melodybeauty.melody_mobile.AuthServices.AuthServices;

    import org.json.JSONObject;


    public class DetailProfile extends AppCompatActivity implements View.OnClickListener{

        Uri uri;
        ImageView pickUser;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_profile);
            pickUser = findViewById(R.id.pictUser);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pickUser.setBackgroundResource(R.drawable.bg_logo);
                pickUser.setClipToOutline(true);
            }
            pickUser.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == pickUser) {
                ImagePicker.with(DetailProfile.this)
                        .cropSquare()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start();
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getData();
                pickUser.setImageURI(uri);
                Log.e("Inpo Image", uri.toString());
                String image = uri.getPath();
                updateProfileImage(image);
            } else {
                Toast.makeText(getApplicationContext(), "no image selected", Toast.LENGTH_LONG).show();
            }
        }

        private void updateProfileImage(String image) {
            SharedPreferences preferences = DetailProfile.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
            String token = preferences.getString("token", "");
            final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
            AuthServices.updatefoto(getApplicationContext(), token, image, new AuthServices.UpdateFotoResponseListener() {
                @Override
                public void onSuccess(JSONObject response) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), "Update Successfuly", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    loading.dismiss();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

