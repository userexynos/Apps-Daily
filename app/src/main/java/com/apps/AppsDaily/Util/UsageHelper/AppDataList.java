package com.apps.AppsDaily.Util.UsageHelper;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppDataList {
    Context context;
    UsageStatsManager mUsageStatsManager;
    PackageManager packageManager;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AppDataList(Context context) {
        mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        packageManager = context.getPackageManager();
        this.context = context;
    }

    public List<CustomUsageStats> customUsageStats(long start, long end) {
        List<CustomUsageStats> set = new ArrayList<>();
        UsageEvents uEvents = mUsageStatsManager.queryEvents(start, end);
        int i = 0;
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);
            long lastUsed = 0;
            String namePackage[] = null;
            String nameApp = null;
            int eventType = 0;
            Drawable appicon = null;
            if (e != null && e.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                namePackage = e.getClassName().split("\\.");
                lastUsed = e.getTimeStamp();
                eventType = e.getEventType();
                try {
                    nameApp = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(e.getPackageName(), PackageManager.GET_META_DATA));
                    appicon = packageManager.getApplicationIcon(e.getPackageName());
                    set.add(new CustomUsageStats(nameApp, lastUsed, namePackage[namePackage.length - 1], eventType, appicon));
                } catch (PackageManager.NameNotFoundException ex) {
                    continue;
                }
            }
            if (e != null && e.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                namePackage = e.getClassName().split("\\.");
                lastUsed = e.getTimeStamp();
                eventType = e.getEventType();
                try {
                    nameApp = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(e.getPackageName(), PackageManager.GET_META_DATA));
                    appicon = packageManager.getApplicationIcon(e.getPackageName());
                    set.add(new CustomUsageStats(nameApp, lastUsed, namePackage[namePackage.length - 1], eventType, appicon));
                } catch (PackageManager.NameNotFoundException ex) {
                    continue;
                }
            }

        }
        set = new ArrayList<>(new LinkedHashSet<>(set));
        for(int ii = 1; ii < set.size()-1; ii++) {
            String background = null;
            if(set.get(ii-1).getEvents() == 2) {
                background = set.get(ii-1).getPackageName();
                if(background.equals(set.get(ii).getPackageName())) {
                    set.set(ii, new CustomUsageStats("Layar Hidup", set.get(ii).getLastUsed()+100, "onScreen", 11, null));
                    set.add(new CustomUsageStats("Layar Mati", set.get(ii-1).getLastUsed()+100,"offScreen", 11, null));
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return set;
    }

    public List<CustomTotalUsageStats> getTime(List<CustomTotalUsageStats> CustomTotalUsageStatsList, long calendar) {
        List<CustomTotalUsageStats> CustomTotalUsageStats = new ArrayList<>();
        Collections.sort(CustomTotalUsageStatsList, new Comparator<CustomTotalUsageStats>() {
            @Override
            public int compare(CustomTotalUsageStats lhs, CustomTotalUsageStats rhs) {
                return rhs.getLastUsed().compareTo(lhs.getLastUsed());
            }
        });
        for (int i=0;i<CustomTotalUsageStatsList.size()-1;i++) {
            CustomTotalUsageStats = CustomTotalUsageStatsList;
            CustomTotalUsageStats.get(0).setTimeProses(calendar - CustomTotalUsageStats.get(0).getLastUsed());

            long get = CustomTotalUsageStats.get(i).getLastUsed();
            long get2 = CustomTotalUsageStats.get(i+1).getLastUsed();
            CustomTotalUsageStats.get(i+1).setTimeProses(get - get2);
            CustomTotalUsageStats.get(i).setDateUser(sdf.format(new Date(CustomTotalUsageStats.get(i).getLastUsed())));
        }
        for (int iii = 1; iii < CustomTotalUsageStats.size(); iii++) {
            if(CustomTotalUsageStats.get(iii).getEvents() == 2) {
                CustomTotalUsageStats.remove(iii);
            } else {
                continue;
            }
        }
        return CustomTotalUsageStats;
    }

    public List<CustomUsageStats> getTimes(List<CustomUsageStats> customUsageStatsList, long calendar) {
        List<CustomUsageStats> customUsageStats = new ArrayList<>();
        Collections.sort(customUsageStatsList, new Comparator<CustomUsageStats>() {
            @Override
            public int compare(CustomUsageStats lhs, CustomUsageStats rhs) {
                return rhs.getLastUsed().compareTo(lhs.getLastUsed());
            }
        });
        for (int i=0;i<customUsageStatsList.size()-1;i++) {
            customUsageStats = customUsageStatsList;
            customUsageStats.get(0).setTimeProses(calendar - customUsageStats.get(0).getLastUsed());

            long get = customUsageStats.get(i).getLastUsed();
            long get2 = customUsageStats.get(i+1).getLastUsed();
            customUsageStats.get(i+1).setTimeProses(get - get2);
            customUsageStats.get(i).setDateUser(sdf.format(new Date(customUsageStats.get(i).getLastUsed())));
        }
        for (int iii = 1; iii < customUsageStats.size(); iii++) {
            if(customUsageStats.get(iii).getEvents() == 2) {
                customUsageStats.remove(iii);
            } else {
                continue;
            }
        }
        return customUsageStats;
    }

    public List<CustomUsageStats> getTimeDESC(List<CustomUsageStats> customUsageStatsList, long calendar) {
        List<CustomUsageStats> customUsageStats = new ArrayList<>();
        Collections.sort(customUsageStatsList, new Comparator<CustomUsageStats>() {
            @Override
            public int compare(CustomUsageStats lhs, CustomUsageStats rhs) {
                return rhs.getLastUsed().compareTo(lhs.getLastUsed());
            }
        });
        for (int i=0;i<customUsageStatsList.size()-1;i++) {
            customUsageStats = customUsageStatsList;
            customUsageStats.get(0).setTimeProses(calendar - customUsageStats.get(0).getLastUsed());

            long get = customUsageStats.get(i).getLastUsed();
            long get2 = customUsageStats.get(i+1).getLastUsed();
            customUsageStats.get(i+1).setTimeProses(get - get2);
            customUsageStats.get(i).setDateUser(sdf.format(new Date(customUsageStats.get(i).getLastUsed())));
        }
        Collections.sort(customUsageStats, new Comparator<CustomUsageStats>() {
            @Override
            public int compare(CustomUsageStats lhs, CustomUsageStats rhs) {
                return lhs.getLastUsed().compareTo(rhs.getLastUsed());
            }
        });
        for (int iii = 1; iii < customUsageStats.size(); iii++) {
            if(customUsageStats.get(iii).getEvents() == 2) {
                customUsageStats.remove(iii);
            } else {
                continue;
            }
        }

        return customUsageStats;
    }

    public List<CustomUsageStats> getReverseTime(List<CustomUsageStats> customUsageStatsList) {
        List<CustomUsageStats> customUsageStats = getTimes(customUsageStatsList, System.currentTimeMillis());
        Set<String> nameApp = new LinkedHashSet<>();
        for (int i = 0; i < customUsageStats.size(); i++) {
            nameApp.add(customUsageStats.get(i).getNameApp());
        }
        nameApp.remove("Layar Mati");
        nameApp.remove("Layar Hidup");
        List<String> convertToList = new ArrayList<>(nameApp);
        ArrayList<CustomUsageStats> appUsage = new ArrayList<>();
        for(int ii = 0; ii < convertToList.size();ii++) {
            appUsage.add( new CustomUsageStats(convertToList.get(ii), 0, null,  0, null));
            long a = 0;
            long date = 0;
            for (int i = 0;i < customUsageStats.size();i++) {
                if(convertToList.get(ii).equals(customUsageStats.get(i).getNameApp())) {
                    appUsage.set(ii, new CustomUsageStats(convertToList.get(ii), customUsageStats.get(i).getLastUsed(),
                            customUsageStats.get(i).getPackageName(), customUsageStats.get(i).getEvents(),
                            customUsageStats.get(i).getIcon()));
                    a += customUsageStats.get(i).getTimeProses();
                    date = customUsageStats.get(i).getLastUsed();
                } else {
                    continue;
                }
            }
            appUsage.get(ii).setTimeProses(appUsage.get(ii).getTimeProses()+a);
            sdf.format(new Date(date));
        }
        Collections.sort(appUsage, new Comparator<CustomUsageStats>(){
            public int compare(CustomUsageStats c1, CustomUsageStats c2) {
                return c2.getTimeProses().compareTo(c1.getTimeProses());
            }
        });

        return appUsage;
    }
    public List<CustomTotalUsageStats> timeTotal(long start, long end) {
        List<CustomTotalUsageStats> set = new ArrayList<>();
        UsageEvents uEvents = mUsageStatsManager.queryEvents(start, end);
        int i = 0;
        while (uEvents.hasNextEvent()){
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);
            long lastUsed = 0;
            int eventType = 0;
            String namePackage[] = null;
            if (e != null && e.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                lastUsed = e.getTimeStamp();
                eventType = e.getEventType();
                namePackage = e.getClassName().split("\\.");
                set.add(new CustomTotalUsageStats(lastUsed, eventType, namePackage[namePackage.length - 1]));
            }
            if (e != null && e.getEventType() == UsageEvents.Event.ACTIVITY_PAUSED) {
                namePackage = e.getClassName().split("\\.");
                lastUsed = e.getTimeStamp();
                eventType = e.getEventType();
                set.add(new CustomTotalUsageStats(lastUsed, eventType, namePackage[namePackage.length - 1]));
            }

        }
        set = new ArrayList<>(new LinkedHashSet<>(set));
        for(int ii = 1; ii < set.size()-1; ii++) {
            String background = null;
            if(set.get(ii-1).getEvents() == 2) {
                background = set.get(ii-1).getPackageName();
                if(background.equals(set.get(ii).getPackageName())) {
                    set.set(ii, new CustomTotalUsageStats(set.get(ii).getLastUsed()+100, 11, "onScreen"));
                    set.add(new CustomTotalUsageStats( set.get(ii-1).getLastUsed()+100, 11,"offScreen"));
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return set;
    }
}