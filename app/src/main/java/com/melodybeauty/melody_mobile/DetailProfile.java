    package com.melodybeauty.melody_mobile;

    import androidx.activity.result.ActivityResult;
    import androidx.activity.result.ActivityResultCallback;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.IntentSenderRequest;
    import androidx.activity.result.contract.ActivityResultContracts;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;

    import android.app.Activity;
    import android.app.ProgressDialog;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.util.Base64;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.Toast;

    import com.android.volley.AuthFailureError;
//    import com.android.volley.Request;
    import com.android.volley.RequestQueue;
//    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.StringRequest;
    import com.android.volley.toolbox.Volley;
    import com.github.dhaval2404.imagepicker.ImagePicker;
    import com.melodybeauty.melody_mobile.AuthServices.AuthServices;

    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.util.HashMap;
    import java.util.Map;

    import okhttp3.Call;
    import okhttp3.Callback;
    import okhttp3.MediaType;
    import okhttp3.MultipartBody;
    import okhttp3.OkHttpClient;
    import okhttp3.Request;
    import okhttp3.RequestBody;
    import okhttp3.Response;


    public class DetailProfile extends AppCompatActivity {

        Uri uri;
        ImageView pickUser;
        Bitmap bitmap;
        Button button;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_profile);
            pickUser = findViewById(R.id.pictUser);
            button = findViewById(R.id.upload);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                pickUser.setBackgroundResource(R.drawable.bg_logo);
                pickUser.setClipToOutline(true);
            }
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            pickUser.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            pickUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bitmap != null) {
                        // Mengubah bitmap menjadi file
                        File file = new File(getCacheDir(), "profile_image.jpg");
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Membuat permintaan upload menggunakan OkHttp
                        String url = AuthServices.getApi() + "ufoto"; // Sesuaikan dengan URL backend Anda

                        OkHttpClient client = new OkHttpClient();

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("image", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                                .build();


                        Request.Builder requestBuilder = new Request.Builder()
                                .url(url)
                                .post(requestBody);
                        SharedPreferences preferences = DetailProfile.this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                        String token = preferences.getString("token", "");

                        requestBuilder.addHeader("Authorization", "Bearer " + token);

                        Request request = requestBuilder.build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                // Gagal mengirim permintaan HTTP
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailProfile.this, "Gagal mengirim permintaan", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    // Berhasil mengupload foto
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailProfile.this, "Foto profil berhasil diupdate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    // Gagal mengupload foto
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailProfile.this, "Foto profil gagal diupdate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        // Tidak ada gambar yang dipilih
                        Toast.makeText(DetailProfile.this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }