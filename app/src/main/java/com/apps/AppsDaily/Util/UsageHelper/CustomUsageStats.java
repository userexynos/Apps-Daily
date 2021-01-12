package com.apps.AppsDaily.Util.UsageHelper;

import android.graphics.drawable.Drawable;

public class CustomUsageStats {
    public long timeProses, lastUsed;
    public String nameApp, packageName, dateUser;
    public int eType;
    public Drawable icon;

    public CustomUsageStats(String nameApp, long lastUsed, String packageName, int e, Drawable icon) {
        this.lastUsed = lastUsed;
        this.packageName = packageName;
        this.eType = e;
        this.nameApp = nameApp;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Long getLastUsed() {
        return lastUsed;
    }

    public String getNameApp() {
        return nameApp;
    }

    public int getEvents() {
        return eType;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDateUser() {
        return dateUser;
    }

    public void setDateUser(String dateUser) {
        this.dateUser = dateUser;
    }

    public Long getTimeProses() {
        return timeProses;
    }

    public void setTimeProses(long timeProses) {
        this.timeProses = timeProses;
    }
}
