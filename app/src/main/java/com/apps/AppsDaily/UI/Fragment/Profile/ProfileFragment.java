package com.apps.AppsDaily.UI.Fragment.Profile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.apps.AppsDaily.Network.APIAdapter;
import com.apps.AppsDaily.Network.APIService;
import com.apps.AppsDaily.Network.Response.SendData;
import com.apps.AppsDaily.R;
import com.apps.AppsDaily.UI.HomeUI;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserProfileManager;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private Calendar calendar;
    private String[] gender;
    private ArrayAdapter<String> provinsiAdapter;
    private ArrayAdapter<String> kotaAdapter;
    private TextInputEditText nama, nomor_handphone, email,
            domisili, tanggal_lahir;
    private TextInputLayout namaed, noteled, emailed, ttled, alamated;
    private MaterialBetterSpinner jeniskelamin, pekerjaan, penghasilan, provinsi, kota, pendidikan, merkHP, harga_hp, preferensi_hp;
    private UserManager user;
    private UserProfileManager userProfileManager;
    private View root;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((HomeUI) getActivity()).updateStatusBarColor(false, getResources().getString(R.color.colorPrimaryDark));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        setHasOptionsMenu(false);
        calendar = Calendar.getInstance();
        user = new UserManager(getContext());
        userProfileManager = new UserProfileManager(getContext());
        TextView title = getActivity().findViewById(R.id.title2);
        title.setText("Profile");
        root = inflater.inflate(R.layout.fragment_user, container, false);
        nama = root.findViewById(R.id.nama);
        nomor_handphone = root.findViewById(R.id.nomor_telepon);
        email = root.findViewById(R.id.email);
        pekerjaan = root.findViewById(R.id.pekerjaan);
        penghasilan = root.findViewById(R.id.penghasilan);
        provinsi = root.findViewById(R.id.provinsi);
        kota = root.findViewById(R.id.kota);
        domisili = root.findViewById(R.id.domisili);
        tanggal_lahir = root.findViewById(R.id.tanggal_lahir);
        jeniskelamin = root.findViewById(R.id.jenis_kelamin);
        pendidikan = root.findViewById(R.id.edu);
        namaed = root.findViewById(R.id.namaedtLayout);
        preferensi_hp = root.findViewById(R.id.preferensi_beli_hp);
        merkHP = root.findViewById(R.id.merkHp);
        harga_hp = root.findViewById(R.id.harga_hp);
        noteled = root.findViewById(R.id.nomoredtLayout);
        emailed = root.findViewById(R.id.emailedtLayout);
        alamated = root.findViewById(R.id.edt_domisili);
        ttled = root.findViewById(R.id.ttledtLayout);
        onClick();

        return root;
    }

    public void onClick() {
        setSpinnerAdapter();
        Button button = root.findViewById(R.id.simpan);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAll();
            }
        });
        tanggal_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String formatTanggal = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
                        tanggal_lahir.setText(sdf.format(calendar.getTime()));
                    }
                },
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        getprofile();
    }

    public void getprofile() {
        if(userProfileManager != null) {
            nama.setText(userProfileManager.get(UserProfileManager.PREF_NAMA));
            nomor_handphone.setText(userProfileManager.get(UserProfileManager.PREF_NOHP));
            pekerjaan.setText(userProfileManager.get(UserProfileManager.PREF_JOB));
            domisili.setText(userProfileManager.get(UserProfileManager.PREF_ALAMAT));
            tanggal_lahir.setText(userProfileManager.get(UserProfileManager.PREF_BOD));
            jeniskelamin.setText(gender[Integer.parseInt(userProfileManager.get(UserProfileManager.PREF_JK))]);
            pendidikan.setText(userProfileManager.get(UserProfileManager.PREF_PENDIDIKAN));
            email.setText(userProfileManager.get(UserProfileManager.PREF_EMAIL));
            penghasilan.setText(userProfileManager.get(UserProfileManager.PREF_PENDAPATAN));
            provinsi.setText(userProfileManager.get(UserProfileManager.PREF_PROVINSI));
            kota.setText(userProfileManager.get(UserProfileManager.PREF_KOTA));
            merkHP.setText(userProfileManager.get(UserProfileManager.PREF_MERKHP));
            harga_hp.setText(userProfileManager.get(UserProfileManager.PREF_HARGAHP));
            preferensi_hp.setText(userProfileManager.get(UserProfileManager.PREF_PREFERENSIHP));
            provinsi.setClickable(false);
            provinsi.setFocusable(false);
            kota.setClickable(false);
            kota.setFocusable(false);
         }
    }

    public void setSpinnerAdapter() {
        String[] data = {"Belum ada data"};
        gender = getResources().getStringArray(R.array.jenis_kelamin);
        ArrayAdapter<String> jeniskelaminAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.jenis_kelamin));
        ArrayAdapter<String> pendidikanAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pendidikan_terakhir));
        ArrayAdapter<String> jobAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.pekerjaan));
        ArrayAdapter<String> penghasilanAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.penghasilan));
        ArrayAdapter<String> merkHPAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.nama_hp));
        ArrayAdapter<String> harga_hpAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.harga_hp));
        ArrayAdapter<String> preferensi_hpAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.preferensi_pembelian_hp));
        if(provinsiAdapter == null) {
            provinsiAdapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_dropdown_item_1line, data);
        }
        if(kotaAdapter == null) {
            kotaAdapter = new ArrayAdapter<>(
                    getContext(), android.R.layout.simple_dropdown_item_1line, data);
        }
        jeniskelamin.setAdapter(jeniskelaminAdapter);
        pendidikan.setAdapter(pendidikanAdapter);
        pekerjaan.setAdapter(jobAdapter);
        penghasilan.setAdapter(penghasilanAdapter);
        merkHP.setAdapter(merkHPAdapter);
        harga_hp.setAdapter(harga_hpAdapter);
        preferensi_hp.setAdapter(preferensi_hpAdapter);
        provinsi.setAdapter(provinsiAdapter);
        provinsi.invalidate();
        kota.setAdapter(kotaAdapter);
        kota.invalidate();
    }

    public void validateAll() {
        if(nama.getText().length() == 0) {
            namaed.setError("Masukan Nama");
        } else if(nomor_handphone.getText().length() == 0) {
            noteled.setError("Masukan Nomor Telepon");
        } else if(jeniskelamin.getText().length() == 0) {
            jeniskelamin.setError("Pilih Jenis Kelamin");
        } else if(!isEmailValid(email.getText().toString())) {
            emailed.setError("Format Email Salah");
        } else if(pekerjaan.getText().length() == 0) {
            pekerjaan.setError("Pilih Pekerjaan");
        } else if(penghasilan.getText().length() == 0) {
            penghasilan.setError("Pilih Penghasilan");
        } else if(pendidikan.getText().length() == 0) {
            pendidikan.setError("Pilih Pendidikan Terakhir");
        } else if(domisili.getText().length() == 0) {
            alamated.setError("Masukan Alamat");
        } else if(provinsi.getText().length() == 0) {
            provinsi.setError("Pilih Provinsi");
        } else if(kota.getText().length() == 0) {
            kota.setError("Pilih Kota");
        } else if(tanggal_lahir.getText().length() == 0) {
            ttled.setError("Masukan Tanggal Lahir");
        } else if(merkHP.getText().length() == 0) {
            merkHP.setError("Pilih Merk Handphone");
        } else if(harga_hp.getText().length() == 0) {
            harga_hp.setError("Pilih Kisaran Harga Handphone");
        } else if(preferensi_hp.getText().length() == 0) {
            preferensi_hp.setError("Pilih Preferensi Penggunaan Handphone");
        } else {
            saveProfile();
        }
    }

    public void saveProfile() {
        APIService api = APIAdapter.getClient().create(APIService.class);
        Call<SendData> send = api.update("Bearer "+user.get(), nama.getText().toString(), nomor_handphone.getText().toString(),
                Arrays.asList(gender).indexOf(jeniskelamin.getText().toString()), email.getText().toString(), pekerjaan.getText().toString(),
                pendidikan.getText().toString(), tanggal_lahir.getText().toString(), domisili.getText().toString(), penghasilan.getText().toString(), harga_hp.getText().toString(), preferensi_hp.getText().toString(), merkHP.getText().toString());

        userProfileManager.save(UserProfileManager.PREF_NAMA, nama.getText().toString() );
        userProfileManager.save(UserProfileManager.PREF_NOHP, nomor_handphone.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_EMAIL, email.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_BOD, tanggal_lahir.getText().toString() );
        userProfileManager.save(UserProfileManager.PREF_JK, Arrays.asList(gender).indexOf(jeniskelamin.getText().toString())+"");
        userProfileManager.save(UserProfileManager.PREF_JOB, pekerjaan.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_PENDIDIKAN, pendidikan.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_ALAMAT, domisili.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_PENDAPATAN, penghasilan.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_HARGAHP, harga_hp.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_PREFERENSIHP, preferensi_hp.getText().toString());
        userProfileManager.save(UserProfileManager.PREF_MERKHP, merkHP.getText().toString());
        Toast.makeText(getActivity(), "Sedang Menyimpan", Toast.LENGTH_SHORT).show();
        send.enqueue(new Callback<SendData>() {
            @Override
            public void onResponse(Call<SendData> call, Response<SendData> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus()) {
                        Toast.makeText(getActivity(), "Sukses mengubah profile", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Gagal mengubah profile", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<SendData> call, Throwable t) {
                Toast.makeText(getContext(), "Connection Timeout", Toast.LENGTH_LONG).show();
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