package com.apps.AppsDaily.UI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.apps.AppsDaily.Network.APIAdapter;
import com.apps.AppsDaily.Network.APIService;
import com.apps.AppsDaily.Network.Response.GetUsersProfile;
import com.apps.AppsDaily.R;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserProfileManager;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeUI extends AppCompatActivity {
    UserProfileManager userProfileManager;
    UserManager user;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_ui);
        progressDialog = new ProgressDialog(this);
        user = new UserManager(this);
        userProfileManager = new UserProfileManager(this);
//        progressDialog.setIndeterminate(false);
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Memuat Data Profile");
//        progressDialog.show();
        APIService api = APIAdapter.getClientWithTimeOut().create(APIService.class);
        Call<GetUsersProfile> profile = api.getProfile("Bearer " + user.get());
//        profile.enqueue(new Callback<GetUsersProfile>() {
//            @Override
//            public void onResponse(Call<GetUsersProfile> call, Response<GetUsersProfile> response) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                if(response.isSuccessful()) {
//                    if(response.body().getStatus()) {
//                        Log.d("STATUS", response.body().getData().getUser().getProfile().getFullname());
//                        userProfileManager.save(UserProfileManager.PREF_NAMA, response.body().getData().getUser().getProfile().getFullname());
//                        userProfileManager.save(UserProfileManager.PREF_NOHP, response.body().getData().getUser().getProfile().getNoHandphone());
//                        userProfileManager.save(UserProfileManager.PREF_EMAIL, response.body().getData().getUser().getEmail());
//                        userProfileManager.save(UserProfileManager.PREF_BOD, response.body().getData().getUser().getProfile().getBod());
//                        userProfileManager.save(UserProfileManager.PREF_JK, response.body().getData().getUser().getProfile().getGender());
//                        userProfileManager.save(UserProfileManager.PREF_JOB, response.body().getData().getUser().getProfile().getWork());
//                        userProfileManager.save(UserProfileManager.PREF_ALAMAT, response.body().getData().getUser().getProfile().getLocation());
//                        userProfileManager.save(UserProfileManager.PREF_PROVINSI, response.body().getData().getUser().getProfile().getProvince());
//                        userProfileManager.save(UserProfileManager.PREF_PENDIDIKAN, response.body().getData().getUser().getProfile().getLastEducation());
//                        userProfileManager.save(UserProfileManager.PREF_KOTA, response.body().getData().getUser().getProfile().getCity());
//                        userProfileManager.save(UserProfileManager.PREF_PENDAPATAN, response.body().getData().getUser().getProfile().getIncome());
//                        userProfileManager.save(UserProfileManager.PREF_HARGAHP, response.body().getData().getUser().getProfile().getPriceRange());
//                        userProfileManager.save(UserProfileManager.PREF_PREFERENSIHP, response.body().getData().getUser().getProfile().getPreference());
//                        userProfileManager.save(UserProfileManager.PREF_MERKHP, response.body().getData().getUser().getProfile().getBrandHandphone());
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Token InValidate", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<GetUsersProfile> call, Throwable t) {
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//                    if(t instanceof SocketTimeoutException){
//                        Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
//                    }
//            }
//        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_penggunaan, R.id.navigation_dashboard, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void updateStatusBarColor(Boolean status, String color){// Color must be in hexadecimal fromat
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor(color));
            if(status) {
                iconDark();
            }
        }
    }

    public void iconDark() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
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

}
