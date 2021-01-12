package com.apps.AppsDaily.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.apps.AppsDaily.Network.APIAdapter;
import com.apps.AppsDaily.Network.APIService;
import com.apps.AppsDaily.Network.Response.GetAllCity;
import com.apps.AppsDaily.Network.Response.GetAllProvince;
import com.apps.AppsDaily.Network.Response.GetCity;
import com.apps.AppsDaily.Network.Response.GetProvince;
import com.apps.AppsDaily.Network.Response.Register;
import com.apps.AppsDaily.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUI extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 1;
    Calendar calendar;
    List<String> getAllProvinsi;
    ArrayAdapter<String> pendidikanAdapter, jeniskelaminAdapter, jobAdapter, penghasilanAdapter,
            provinsiAdapter, kotaAdapter, merkHPAdapter, harga_hpAdapter, preferensi_hpAdapter;
    MaterialBetterSpinner pendidikan, jenis_kelamin, job, penghasilan, provinsi, kota, merkHP, harga_hp, preferensi_hp;
    TextInputLayout namaed, nomorhped, emailed, ttled, passed, alamated;
    TextInputEditText nama, nomorhp, email, ttl, password, alamat;
    Button register, tetapkan_provinsi;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ui);
        progressDialog = new ProgressDialog(this);
        getAllProvinsi = new ArrayList<>();
        calendar = Calendar.getInstance();
        alamat = findViewById(R.id.domisili);
        jenis_kelamin = findViewById(R.id.jenis_kelamin);
        nama = findViewById(R.id.nama);
        nomorhp = findViewById(R.id.nomor_telepon);
        email = findViewById(R.id.email);
        job = findViewById(R.id.pekerjaan);
        penghasilan = findViewById(R.id.penghasilan);
        provinsi = findViewById(R.id.provinsi);
        kota = findViewById(R.id.kota);
        merkHP = findViewById(R.id.merkHp);
        harga_hp = findViewById(R.id.harga_hp);
        preferensi_hp = findViewById(R.id.preferensi_beli_hp);
        pendidikan = findViewById(R.id.edu);
        ttl = findViewById(R.id.tanggal_lahir);
        password = findViewById(R.id.password);
        namaed = findViewById(R.id.namaedtLayout);
        nomorhped = findViewById(R.id.nomoredtLayout);
        emailed = findViewById(R.id.emailedtLayout);
        alamated = findViewById(R.id.edt_domisili);
        ttled = findViewById(R.id.ttledtLayout);
        passed = findViewById(R.id.passedtLayout);
        register = findViewById(R.id.daftar);
        tetapkan_provinsi = findViewById(R.id.tetapkan);
        saatDiklik();
    }

    public void setSpinnerAdapter() {
        String[] data = {"Belum ada data"};
        jeniskelaminAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.jenis_kelamin));
        pendidikanAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pendidikan_terakhir));
        jobAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pekerjaan));
        penghasilanAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.penghasilan));
        merkHPAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.nama_hp));
        harga_hpAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.harga_hp));
        preferensi_hpAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.preferensi_pembelian_hp));
        if(provinsiAdapter == null) {
            provinsiAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, data);
        }
        if(kotaAdapter == null) {
            kotaAdapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, data);
        }
        jenis_kelamin.setAdapter(jeniskelaminAdapter);
        pendidikan.setAdapter(pendidikanAdapter);
        job.setAdapter(jobAdapter);
        penghasilan.setAdapter(penghasilanAdapter);
        merkHP.setAdapter(merkHPAdapter);
        harga_hp.setAdapter(harga_hpAdapter);
        preferensi_hp.setAdapter(preferensi_hpAdapter);
        provinsi.setAdapter(provinsiAdapter);
        provinsi.invalidate();
        kota.setAdapter(kotaAdapter);
        kota.invalidate();
    }

    public void saatDiklik() {
        setSpinnerAdapter();
        getProvinsiKota();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateAll();
            }
        });
        ttl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterUI.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formatTanggal = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        ttl.setText(sdf.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void validateAll() {
        if(nama.getText().length() == 0) {
            namaed.setError("Masukan Nama");
        } else if(nomorhp.getText().length() == 0) {
            nomorhped.setError("Masukan Nomor Telepon");
        } else if(jenis_kelamin.getText().length() == 0) {
            jenis_kelamin.setError("Pilih Jenis Kelamin");
        } else if(!isEmailValid(email.getText().toString())) {
            emailed.setError("Format Email Salah");
        } else if(job.getText().length() == 0) {
            job.setError("Pilih Pekerjaan");
        } else if(penghasilan.getText().length() == 0) {
            penghasilan.setError("Pilih Penghasilan");
        } else if(pendidikan.getText().length() == 0) {
            pendidikan.setError("Pilih Pendidikan Terakhir");
        } else if(alamat.getText().length() == 0) {
            alamated.setError("Masukan Alamat");
        } else if(provinsi.getText().length() == 0) {
            provinsi.setError("Pilih Provinsi");
        } else if(kota.getText().length() == 0) {
            kota.setError("Pilih Kota");
        } else if(ttl.getText().length() == 0) {
            ttled.setError("Masukan Tanggal Lahir");
        } else if(merkHP.getText().length() == 0) {
            merkHP.setError("Pilih Merk Handphone");
        } else if(harga_hp.getText().length() == 0) {
            harga_hp.setError("Pilih Kisaran Harga Handphone");
        } else if(preferensi_hp.getText().length() == 0) {
            preferensi_hp.setError("Pilih Preferensi Penggunaan Handphone");
        } else if(password.getText().length() == 0) {
            passed.setError("Masukan Password");
        } else {
            startRegister();
        }
    }

    public void startRegister() {
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Memeriksa Data");
        progressDialog.show();
        String[] getIdProvince = provinsi.getText().toString().split("-");
        String[] getIdCity = kota.getText().toString().split("-");
        String[] jenis_kelamin = getResources().getStringArray(R.array.jenis_kelamin);
        APIService api = APIAdapter.getClientWithTimeOut().create(APIService.class);
        Call<Register> register = api.register( nama.getText().toString(), nomorhp.getText().toString(),
                Arrays.asList(jenis_kelamin).indexOf(this.jenis_kelamin.getText().toString()), email.getText().toString(), job.getText().toString(),
                pendidikan.getText().toString(), ttl.getText().toString(),
                password.getText().toString(), alamat.getText().toString(), Integer.parseInt(getIdProvince[getIdProvince.length-1]),
                Integer.parseInt(getIdCity[getIdCity.length-1]), penghasilan.getText().toString(), harga_hp.getText().toString(),
                preferensi_hp.getText().toString(), merkHP.getText().toString());
        register.enqueue(new Callback<Register>() {
            @Override
            public void onResponse(Call<Register> call, Response<Register> response) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(response.isSuccessful()) {
                    if(response.body().getStatus()) {
                        startActivity(new Intent(getApplicationContext(), LoginUI.class));
                        Toast.makeText(getApplicationContext(), "Register Sukses", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if(!response.body().getStatus() && response.body().getMessages().get(0).equals("Email telah di gunakan")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                    } else if(!response.body().getStatus() && response.body().getMessages().get(0).equals("Nomor handphone telah di gunakan")) {
                        Toast.makeText(getApplicationContext(), response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessages()+"", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onFailure(Call<Register> call, Throwable t) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                if(t instanceof SocketTimeoutException){
                    Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getProvinsiKota() {
        APIService api = APIAdapter.getClient().create(APIService.class);
        final Call<GetProvince> province = api.getProvince();
        province.enqueue(new Callback<GetProvince>() {
            @Override
            public void onResponse(Call<GetProvince> call, final Response<GetProvince> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus()) {
                        for(GetAllProvince provinsi : response.body().getData()) {
                            getAllProvinsi.add(provinsi.getName() +"-"+provinsi.getId());
                        }
                        provinsiAdapter = new ArrayAdapter<>(
                                getApplicationContext(), android.R.layout.simple_dropdown_item_1line, getAllProvinsi);
                        provinsi.setAdapter(provinsiAdapter);
                        tetapkan_provinsi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                kota.setText("");
                                if(provinsi.getText().length() > 0) {
                                    APIService api = APIAdapter.getClient().create(APIService.class);
                                    Call<GetCity> city = api.getKota(Integer.parseInt(response.body().getData().get(getAllProvinsi.indexOf(provinsi.getText().toString())).getId()));
                                    city.enqueue(new Callback<GetCity>() {
                                        @Override
                                        public void onResponse(Call<GetCity> call, Response<GetCity> res) {
                                            if (res.isSuccessful()) {
                                                if (res.body().getStatus()) {
                                                    kota.setVisibility(View.VISIBLE);
                                                    List<String> city = new ArrayList<>();
                                                    for (GetAllCity getCity : res.body().getData().getCities()) {
                                                        city.add(getCity.getName()+"-"+getCity.getId());
                                                    }
                                                    kotaAdapter = new ArrayAdapter<>(
                                                            getApplicationContext(), android.R.layout.simple_dropdown_item_1line, city);
                                                    kota.setAdapter(kotaAdapter);
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetCity> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    provinsi.setError("Tetapkan Lokasinya");
                                }
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<GetProvince> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
