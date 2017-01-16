package zt.sakoonkinamaz.bean;

import android.content.Context;

import zt.sakoonkinamaz.enums.Prayer;
import zt.sakoonkinamaz.R;

/**
 * Created by Haseeb Bhai on 1/12/2017.
 */

public class Bean {
    private long id;
    private String name;
    private long startTime;
    private long endTime;
    private Prayer prayer;
    private int originalProfile;

    public Bean(Prayer p, long strt, long end){
        this.startTime = strt;
        this.endTime = end;
        this.prayer = p;
    }

    public Bean(String name, long strt, long end){
        this.startTime = strt;
        this.endTime = end;
        this.name = name;
    }

    public Bean(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setPrayer(Prayer prayer) {
        this.prayer = prayer;
    }

    public String getPrayer(Context context) {
        String name = "Custom";
        switch(prayer) {
            case FAJR:
                name = context.getResources().getString(R.string.fajr);
                break;
            case ZOHAR:
                name = context.getResources().getString(R.string.zohar);
                break;
            case ASR:
                name = context.getResources().getString(R.string.asr);
                break;
            case MAGHRIB:
                name = context.getResources().getString(R.string.maghrib);
                break;
            case ISHAA:
                name = context.getResources().getString(R.string.ishaa);
                break;
            case JUMMA:
                name = context.getResources().getString(R.string.jumma);
        }
        return name;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Prayer getPrayer() {
        return prayer;
    }

    public int getOriginalProfile() {
        return originalProfile;
    }

    public void setOriginalProfile(int originalProfile) {
        this.originalProfile = originalProfile;
    }
}
