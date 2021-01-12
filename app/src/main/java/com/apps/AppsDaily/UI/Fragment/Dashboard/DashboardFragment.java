package com.apps.AppsDaily.UI.Fragment.Dashboard;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.AppsDaily.R;
import com.apps.AppsDaily.UI.HomeUI;
import com.apps.AppsDaily.Util.RecyclerAdapter.DashboardAdapter;
import com.apps.AppsDaily.Util.TimeHelper.CalendarWeekly;
import com.apps.AppsDaily.Util.UsageHelper.CustomTotalUsageStats;
import com.apps.AppsDaily.Util.UsageHelper.CustomUsageStats;
import com.apps.AppsDaily.Util.UsageHelper.AppDataList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.apps.AppsDaily.Util.TimeHelper.MilliesToTime;

public class DashboardFragment extends Fragment {
    private BarChart chart;
    private TextView usageToday, usageWeekly;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM");
    private CalendarWeekly cal = new CalendarWeekly();
    private MilliesToTime convert = new MilliesToTime();
    private RecyclerView recyclerView;
    private AppDataList appData;
    private List<CustomTotalUsageStats> pilihan1, pilihan2, pilihan3, pilihan4, pilihan5, pilihan6, pilihan7;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        ((HomeUI) getActivity()).updateStatusBarColor(true, "#FFFFFF");
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        new LoadLayout().execute();
        appData = new AppDataList(getContext());
        recyclerView = root.findViewById(R.id.recyclerV);
        ProgressBar progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        chart = root.findViewById(R.id.barCharts);
        usageToday = root.findViewById(R.id.today_usage_stats);
        usageWeekly = root.findViewById(R.id.usage_weekly_stats);
        new LoadLayout2().execute();
        RecyclerView();
        return root;
    }

    public void RecyclerView() {
        List<CustomUsageStats> listAdapter = appData.getReverseTime(appData.customUsageStats(
                cal.get(), System.currentTimeMillis()
        ));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new DashboardAdapter(getLayoutInflater(), listAdapter));
    }

    public List<BarEntry> barChart(float min6, float min5, float min4, float min3,
                                   float min2, float min1, float min) {
        List<BarEntry> barEntry = new ArrayList<>();
        barEntry.add(new BarEntry(0, min6));
        barEntry.add(new BarEntry(1, min5));
        barEntry.add(new BarEntry(2, min4));
        barEntry.add(new BarEntry(3, min3));
        barEntry.add(new BarEntry(4, min2));
        barEntry.add(new BarEntry(5, min1));
        barEntry.add(new BarEntry(6, min));

        chart.getAxisRight().setEnabled(false);
        chart.setDescription(null);
        chart.setScaleEnabled(false);
        chart.setGridBackgroundColor(Color.parseColor("#0FFFFFFF"));
        String[] hari = {dateFormat.format(new Date(cal.getMin6())), dateFormat.format(new Date(cal.getMin5())), dateFormat.format(new Date(cal.getMin4())),
                dateFormat.format(new Date(cal.getMin3())), dateFormat.format(new Date(cal.getMin2())), dateFormat.format(new Date(cal.getMin1())), dateFormat.format(new Date(cal.get()))};
        XAxis xaxis = chart.getXAxis();
        xaxis.setValueFormatter(new IndexAxisValueFormatter(hari));
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularity(1);
        xaxis.setDrawGridLines(true);
        xaxis.isCenterAxisLabelsEnabled();
        xaxis.setGranularityEnabled(true);

        return barEntry;
    }

    public List<CustomTotalUsageStats> getPackageLastTimeUsed(int pilihan) {
        List<CustomTotalUsageStats> stats = new ArrayList<>();
        if(pilihan == 1) {
            stats = pilihan1;
        } else if(pilihan == 2) {
            stats = pilihan2;
        } else if(pilihan == 3) {
            stats = pilihan3;
        } else if(pilihan == 4) {
            stats = pilihan4;
        } else if(pilihan == 5) {
            stats = pilihan5;
        } else if (pilihan == 6) {
            stats = pilihan6;
        } else if (pilihan == 7) {
            stats = pilihan7;
        }

        return stats;
    }

    public long Calculate(int pilihan) {
        List<CustomTotalUsageStats> stats = getPackageLastTimeUsed(pilihan);
        long data = 0;
        for(CustomTotalUsageStats a:stats) {
            if(a.getEvents() == 1) {
                data += a.getTimeProses();
            }
        }

        return data;
    }

    @SuppressLint("StaticFieldLeak")
    class LoadLayout extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pDialog = new ProgressDialog(getContext());
        @Override
        protected void onPreExecute() {
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mengambil Statistik data");
            pDialog.show();
        }
        protected Void doInBackground(Void... parameters) {
            pilihan1 = appData.getTime(appData.timeTotal(cal.get(), System.currentTimeMillis()), System.currentTimeMillis());
            pilihan2 = appData.getTime(appData.timeTotal(cal.getMin1(), cal.get()), cal.get());
            pilihan3 = appData.getTime(appData.timeTotal(cal.getMin2(), cal.getMin1()), cal.getMin1());
            pilihan4 = appData.getTime(appData.timeTotal(cal.getMin3(), cal.getMin2()), cal.getMin2());
            pilihan5 = appData.getTime(appData.timeTotal(cal.getMin4(), cal.getMin3()), cal.getMin3());
            pilihan6 = appData.getTime(appData.timeTotal(cal.getMin5(), cal.getMin4()), cal.getMin4());
            pilihan7 = appData.getTime(appData.timeTotal(cal.getMin5(), cal.getMin5()), cal.getMin5());

            return null;
        }
        protected void onPostExecute(Void param) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog.invalidateOptionsMenu();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    class LoadLayout2 extends AsyncTask<Void, Integer, Void> {
        ProgressDialog pDialog = new ProgressDialog(getContext());
        int ch1 = 0, ch2 = 0, ch3 = 0, ch4 = 0, ch5 = 0, ch6 = 0, ch7 = 0;
        @Override
        protected void onPreExecute() {
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mengambil Statistik data");
            pDialog.show();
        }
        protected Void doInBackground(Void... parameters) {
            ch1 = (int) convert.convertMillToTime(Calculate(7));
            ch2 = (int) convert.convertMillToTime(Calculate(6));
            ch3 = (int) convert.convertMillToTime(Calculate(5));
            ch4 = (int) convert.convertMillToTime(Calculate(4));
            ch5 = (int) convert.convertMillToTime(Calculate(3));
            ch6 = (int) convert.convertMillToTime(Calculate(2));
            ch7 = (int) convert.convertMillToTime(Calculate(1));

            return null;
        }
        protected void onPostExecute(Void param) {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
                pDialog.invalidateOptionsMenu();
            }
            List<BarEntry> entry = barChart(ch1, ch2, ch3, ch4, ch5, ch6, ch7);
            usageToday.setText(ch7+" Menit");
            usageWeekly.setText((ch1+ch2+ch3+ch4+ch5+ch6+ch7)/10+" Menit");
            BarDataSet dataSet = new BarDataSet(entry, "Pemakaian");
            dataSet.setColor(Color.parseColor("#F57D26"));
            BarData data = new BarData(dataSet);
            chart.setData(data);
            chart.postInvalidate();
        }
    }
}
