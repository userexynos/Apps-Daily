package com.apps.AppsDaily.UI.Fragment.DailyUsage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.apps.AppsDaily.Network.APIAdapter;
import com.apps.AppsDaily.Network.APIService;
import com.apps.AppsDaily.Network.Response.LatestStore;
import com.apps.AppsDaily.Network.Response.SendData;
import com.apps.AppsDaily.R;
import com.apps.AppsDaily.UI.HomeUI;
import com.apps.AppsDaily.Util.SharedPreferenceManager.UserManager;
import com.apps.AppsDaily.Util.TimeHelper.CalendarWeekly;
import com.apps.AppsDaily.Util.TimeHelper.MilliesToTime;
import com.apps.AppsDaily.Util.UsageHelper.AppDataList;
import com.apps.AppsDaily.Util.UsageHelper.CustomUsageStats;
import com.apps.AppsDaily.Util.RecyclerAdapter.UsageListAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xyz.sangcomz.stickytimelineview.RecyclerSectionItemDecoration;
import xyz.sangcomz.stickytimelineview.TimeLineRecyclerView;
import xyz.sangcomz.stickytimelineview.model.SectionInfo;

public class DailyUsageFragment extends Fragment {
    private TimeLineRecyclerView recycle;
    UserManager user;
    DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
    CalendarWeekly cal = new CalendarWeekly();
    AppDataList app;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkPersmission();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((HomeUI) getActivity()).updateStatusBarColor(false, getResources().getString(R.color.colorPrimaryDark));
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        user = new UserManager(getContext());
        app = new AppDataList(getContext());
        TextView title = getActivity().findViewById(R.id.title2);
        title.setText("Penggunaan Harian");
        View root = inflater.inflate(R.layout.fragment_penggunaan, container, false);
        recycle = root.findViewById(R.id.timeline);
        recycle.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        getAppStatistic bg = new getAppStatistic();
        bg.execute();
        final SwipeRefreshLayout swipe = root.findViewById(R.id.refresh);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(swipe.isRefreshing()) {
                            swipe.setRefreshing(false);
                            List<CustomUsageStats> customUsageStats = app.getTimes(app.customUsageStats(cal.get(), System.currentTimeMillis()), System.currentTimeMillis());
                            recycle.setAdapter(new UsageListAdapter(getLayoutInflater(), customUsageStats));
                        }
                    }
                }, 1000);
            }
        });
        return root;
    }

    private class getAppStatistic extends AsyncTask<Void, Integer, List<CustomUsageStats>> {
        UserManager user = new UserManager(getActivity());
        AppDataList app = new AppDataList(getActivity());
        CalendarWeekly cal = new CalendarWeekly();
        MilliesToTime ms = new MilliesToTime();
        List<CustomUsageStats> forSend;
        ProgressDialog pDialog = new ProgressDialog(getContext());
        protected void onPreExecute() {
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Memuat Data Pemakaian");
            pDialog.show();
        }
        protected List<CustomUsageStats> doInBackground(Void... parameters) {
            List<CustomUsageStats> customUsageStats = app.getTimes(app.customUsageStats(cal.get(), System.currentTimeMillis()), System.currentTimeMillis());
            forSend = app.getTimeDESC( app.customUsageStats(cal.getMin1(), System.currentTimeMillis()), System.currentTimeMillis());

            return customUsageStats;
        }
        protected void onPostExecute(List<CustomUsageStats> usage) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            recycle.addItemDecoration(getSectionCallback(usage));
            recycle.setAdapter(new UsageListAdapter(getLayoutInflater(), usage));


            String hasSend = user.getTime();
            int last_send = 1;
            if(hasSend != null) {
                for (int i = 0; i < forSend.size()-1; i++) {
                    if (hasSend.equals(forSend.get(i).getDateUser())) {
                        last_send = i;
                    }
                }
            } else {
                last_send = 1;
            }
            APIService api = APIAdapter.getClient().create(APIService.class);
            for (int i = last_send; i < forSend.size()-1; i++) {
                Call<SendData> send = api.send("Bearer "+user.get(), forSend.get(i).getNameApp(), forSend.get(i).packageName,
                        ms.converLongToTimeChar(forSend.get(i).getTimeProses()), new SimpleDateFormat("yyyy-MM-dd").format(new Date(forSend.get(i).getLastUsed())),
                        new SimpleDateFormat("HH:mm:ss").format(new Date(forSend.get(i).getLastUsed())));
                send.enqueue(new Callback<SendData>() {
                    @Override
                    public void onResponse(Call<SendData> call, Response<SendData> response) {
                        if(response.isSuccessful()) {
                            Log.d("Sended", response.body().getStatus()+"  ");
                        }
                    }
                    @Override
                    public void onFailure(Call<SendData> call, Throwable t) {

                    }
                });
            }
        }
    }

    private RecyclerSectionItemDecoration.SectionCallback getSectionCallback(final List<CustomUsageStats> usageList) {
        return new RecyclerSectionItemDecoration.SectionCallback() {

            @Nullable
            @Override
            public SectionInfo getSectionHeader(int position) {
                CustomUsageStats stats = usageList.get(position);
                return new SectionInfo(date.format(new Date(stats.getLastUsed())), "Details", null);
            }
            @Override
            public boolean isSection(int position) {
                return false;
            }
        };
    }

    public void getLatestSend() {
        APIService api = APIAdapter.getClient().create(APIService.class);
        Call<LatestStore> getLatestSended = api.getLatest("Bearer"+user.get());
        getLatestSended.enqueue(new Callback<LatestStore>() {
            @Override
            public void onResponse(Call<LatestStore> call, Response<LatestStore> response) {
                if(response.isSuccessful()) {
                    if(response.body().getStatus()) {
                        if(response.body().getData() != null) {
                            user.saveTime(response.body().getData().getDate()+" "+response.body().getData().getTime());
                            Log.d("Last", response.body().getData().getTime());
                        }
                    } else {
                        user.saveTime(null);
                    }
                }
            }
            @Override
            public void onFailure(Call<LatestStore> call, Throwable t) {

            }
        });

    }

    public void checkPersmission() {
        AppOpsManager opsMan = (AppOpsManager)
                getActivity().getSystemService(Context.APP_OPS_SERVICE);
        int mode = opsMan.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getActivity().getPackageName());
        if(mode != AppOpsManager.MODE_ALLOWED) {
            dialogBuilder();
        }
    }

    public void dialogBuilder() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
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
    @Override
    public void onStart(){
        super.onStart();
        getLatestSend();
    }
}

