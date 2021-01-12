package com.apps.AppsDaily.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.AppsDaily.Network.APIAdapter;
import com.apps.AppsDaily.Network.APIService;
import com.apps.AppsDaily.Network.Response.Auth;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.apps.AppsDaily.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginUI extends AppCompatActivity {
    TextView  register;
    UserManager userManager;
    ProgressDialog progressDialog;
    TextInputEditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ui);
        progressDialog = new ProgressDialog(this);
        userManager = new UserManager(this);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.tvdaftar);
        saatDiklik();
    }

    public void saatDiklik() {
        checkPersmission();
        Button login = findViewById(R.id.btmasuk);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().length() > 0) {
                    if (isEmailValid(email.getText().toString())) {
                        if (password.getText().length() > 0) {
                            progressDialog.setIndeterminate(false);
                            progressDialog.setCancelable(false);
                            progressDialog.setMessage("Memeriksa Email & Password");
                            progressDialog.show();
                            APIService api = APIAdapter.getClientWithTimeOut().create(APIService.class);
                            Call<Auth> auth = api.auth(email.getText().toString(), password.getText().toString());

                            auth.enqueue(new Callback<Auth>() {
                                @Override
                                public void onResponse(Call<Auth> call, Response<Auth> response) {
                                    if(response.isSuccessful()) {
                                        if (progressDialog != null && progressDialog.isShowing()) {
                                            progressDialog.dismiss();
                                        }
                                        if(response.body().getStatus()) {
                                            userManager.save(response.body().getToken());
                                            startActivity(new Intent(LoginUI.this, HomeUI.class).
                                                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        } else {
                                            Snackbar.make(email, "Email atau Password Salah", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Auth> call, Throwable t) {
                                    if (progressDialog != null && progressDialog.isShowing()) {
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            password.setError("Field Password harus diisi");
                        }
                    } else {
                        email.setError("Karakter harus berupa Email");
                    }
                } else {
                    email.setError("Field Email harus diisi");
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterUI.class));
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Exit")
                .setMessage("Yakin mau keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void checkPersmission() {
        AppOpsManager opsMan = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsMan.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if(mode != AppOpsManager.MODE_ALLOWED) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Izin Diperlukan");
            alertDialog
                    .setMessage("Izin diperlukan untuk melihat data statistik")
                    .setCancelable(false)
                    .setPositiveButton("Izinkan", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                        }
                    }).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (userManager.get() != null) {
            startActivity(new Intent(LoginUI.this, HomeUI.class).
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }

    }
}
