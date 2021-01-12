package com.apps.AppsDaily.Util.RecyclerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.AppsDaily.R;
import com.apps.AppsDaily.Util.TimeHelper.MilliesToTime;
import com.apps.AppsDaily.Util.UsageHelper.CustomUsageStats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UsageListAdapter extends RecyclerView.Adapter<UsageListAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<CustomUsageStats> dataList;
    public UsageListAdapter(LayoutInflater inflater, List<CustomUsageStats> dataList) {
        this.layoutInflater = inflater;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.row_layout, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MilliesToTime ms = new MilliesToTime();
        CustomUsageStats data = dataList.get(position);
        holder.name.setText(data.getNameApp());
        DateFormat date = new SimpleDateFormat("hh:mm:ss a");
        holder.lastUsage.setText(date.format(new Date(data.lastUsed)));
        holder.classesName.setText(data.getPackageName());
        if(data.getIcon() != null) {
            holder.gambar.setImageDrawable(data.getIcon());
        } else {
            holder.gambar.setImageResource(R.drawable.screen_on_off);
        }
        holder.packageName.setText(ms.converLongToTimeChar(data.getTimeProses()));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, lastUsage, packageName, classesName;
        ImageView gambar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            lastUsage = itemView.findViewById(R.id.date);
            gambar = itemView.findViewById(R.id.gambar);
            packageName = itemView.findViewById(R.id.packageName);
            classesName = itemView.findViewById(R.id.classes);
        }

    }

}
