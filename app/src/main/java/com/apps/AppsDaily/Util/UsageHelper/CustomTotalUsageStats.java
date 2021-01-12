package com.apps.AppsDaily.Util.UsageHelper;


public class CustomTotalUsageStats {
    public long timeProses, lastUsed;
    public int eType;
    public String packageName, dateUser;

    public CustomTotalUsageStats(long lastUsed, int eType, String packageName) {
        this.lastUsed = lastUsed;
        this.eType = eType;
        this.packageName = packageName;
    }

    public Long getLastUsed() {
        return lastUsed;
    }

    public int getEvents() {
        return eType;
    }

    public String getPackageName() {
        return packageName;
    }


    public Long getTimeProses() {
        return timeProses;
    }

    public void setDateUser(String dateUser) {
        this.dateUser = dateUser;
    }
    public void setTimeProses(long timeProses) {
        this.timeProses = timeProses;
    }
}
