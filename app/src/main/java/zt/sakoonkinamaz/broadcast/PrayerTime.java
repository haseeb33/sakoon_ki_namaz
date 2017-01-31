package zt.sakoonkinamaz.broadcast;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import zt.sakoonkinamaz.bean.Bean;
import zt.sakoonkinamaz.database.PrayersDataSource;

import static zt.sakoonkinamaz.publicData.PublicClass.IntToLong;

/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class PrayerTime extends Service {

    public static int previousProfile = AudioManager.RINGER_MODE_SILENT;

    private MatchTime time = new MatchTime();
    private PrayersDataSource prayers;
    private ArrayList<Bean> beanArray = null;
    private Bean currentBean = null;

    @Override
    public void onCreate() {
        prayers = new PrayersDataSource(this);
        prayers.open();
      }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beanArray = prayers.getAllPrayers();
        Collections.sort(beanArray);
        currentBean = null;
        MoveToNextAndStart();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void MoveToNextAndStart() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int date = calendar.get(Calendar.DAY_OF_YEAR);
        long currentTime = IntToLong(h, m);
        int count = 0;
        previousProfile = AudioManager.RINGER_MODE_SILENT;

        for(int i = 0; i < beanArray.size(); i++){
            if(currentTime >= beanArray.get(i).getStartTime()){
                count++;
                if(currentBean == null && beanArray.get(i).getStartTime() != -1){
                    currentBean = beanArray.get(i);
                }
                if(currentBean != null && count == beanArray.size())
                {
                    int minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                    int hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                    String name = currentBean.getName();
                    Calendar startCalender = setOnCalender(hour, minute);
                    startCalender.set(Calendar.DAY_OF_YEAR, date+1);
                    time.handleTime(this, startCalender, name, 0);

                    minute = (int) ((currentBean.getEndTime() / (1000 * 60)) % 60);
                    hour = (int) ((currentBean.getEndTime()/ (1000 * 60 * 60)) % 24);
                    name = "";
                    Calendar endCalender = setOnCalender(hour, minute);
                    endCalender.set(Calendar.DAY_OF_YEAR, date+1);
                    time.handleTime(this, endCalender, name, 1);
                }
            } else {
                currentBean = beanArray.get(i);
                int minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                int hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                String name = currentBean.getName();
                Calendar startCalender = setOnCalender(hour, minute);
                time.handleTime(this, startCalender, name, 0);

                minute = (int) ((currentBean.getEndTime() / (1000 * 60)) % 60);
                hour = (int) ((currentBean.getEndTime()/ (1000 * 60 * 60)) % 24);
                name = "";
                Calendar endCalender = setOnCalender(hour, minute);
                time.handleTime(this, endCalender, name, 1);

                break;
            }
        }
    }

    private Calendar setOnCalender(int hr, int min){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hr);
        calendar.set(Calendar.MINUTE, min);
        return calendar;
    }
}
