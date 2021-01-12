package com.apps.AppsDaily.Util.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.AppsDaily.R;
import com.apps.AppsDaily.Util.TimeHelper.CalendarWeekly;
import com.apps.AppsDaily.Util.TimeHelper.MilliesToTime;
import com.apps.AppsDaily.Util.UsageHelper.CustomUsageStats;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<CustomUsageStats> dataList;
    CalendarWeekly cal;
    MilliesToTime ms;

    public DashboardAdapter(LayoutInflater inflater, List<CustomUsageStats> dataList) {
        this.layoutInflater = inflater;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.row_layout2, parent, false);
        cal = new CalendarWeekly();
        ms = new MilliesToTime();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomUsageStats data = dataList.get(position);
        holder.name.setText(data.getNameApp());
        holder.gambar.setImageDrawable(data.getIcon());
        holder.lastUsage.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date(data.getLastUsed())));
        holder.packageName.setText(ms.converLongToTimeChar(data.getTimeProses())+"");
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, lastUsage, packageName;
        ImageView gambar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            lastUsage = itemView.findViewById(R.id.date);
            gambar = itemView.findViewById(R.id.gambar);
            packageName = itemView.findViewById(R.id.packageName);
        }
    }
}