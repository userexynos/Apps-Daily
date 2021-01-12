package com.apps.AppsDaily.Util.TimeHelper;

public class MilliesToTime {
    public String converLongToTimeChar(long usedTime) {
        String hour="", min="", sec="";
        int h=(int)(usedTime/1000/60/60);
        if (h!=0)
            hour = h+" Jam ";
        int m=(int)((usedTime/1000/60) % 60);
        if (m!=0)
            min = m+" Menit ";
        int s=(int)((usedTime/1000) % 60);
        if (s==0 && (h!=0 || m!=0))
            sec="";
        else
            sec = s+" Detik";
        String date = null;
        if(h != 0) {
            date = hour+min;
        } else {
            date = min+sec;
        }
        return date;
    }

    public float convertMillToTime(long usedTime) {
        int min = 0;
        int m=(int)(usedTime/1000/60);
        if (m!=0)
            min = m;

        return min;
    }
}
