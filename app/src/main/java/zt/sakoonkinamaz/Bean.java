package zt.sakoonkinamaz;

import android.content.Context;

/**
 * Created by Haseeb Bhai on 1/12/2017.
 */

public class Bean {

    private String name;
    private long startTime;
    private long endTime;
    private Prayer prayer;

    public Bean(Prayer p, long strt, long end){
        this.startTime = strt;
        this.endTime = end;
        this.prayer = p;
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

}
