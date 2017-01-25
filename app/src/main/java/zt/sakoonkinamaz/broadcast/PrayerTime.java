package zt.sakoonkinamaz.broadcast;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

    public static long databaseId = -1;
    public static int previousProfile = AudioManager.RINGER_MODE_SILENT;

    private MatchTime time = new MatchTime();
    private PrayersDataSource prayers;
    private ArrayList<Bean> beanArray = null;
    private Bean currentBean = null;
    private String name;

    @Override
    public void onCreate() {
        prayers = new PrayersDataSource(this);
        prayers.open();
        SharedPreferences pref = this.getSharedPreferences("SakoonKiNamaz", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isStart", true);
        editor.commit();
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
        SharedPreferences pref = getSharedPreferences("SakoonKiNamaz", MODE_PRIVATE);
        Boolean isStart = pref.getBoolean("isStart", true);
        if(isStart) {
            doWithStartTime();
        } else {
            doWithEndTime(databaseId);
        }
    }

    private void doWithStartTime() {
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
                    databaseId = currentBean.getId();
                    String name = currentBean.getName();
                    Calendar c = setOnCalender(hour, minute);
                    c.set(Calendar.DAY_OF_YEAR, date+1);
                    time.setAlarm(this, c, name);
                }
            } else {
                currentBean = beanArray.get(i);
                int minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                int hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                databaseId = currentBean.getId();
                String name = currentBean.getName();
                Calendar c = setOnCalender(hour, minute);
                time.setAlarm(this, c, name);
                break;
            }
        }
    }

    private void doWithEndTime(long id) {
        for(int i = 0; i < beanArray.size(); i++){
            if(id == beanArray.get(i).getId()){
                currentBean = beanArray.get(i);
                int minute = (int) ((currentBean.getEndTime() / (1000 * 60)) % 60);
                int hour =(int) ((currentBean.getEndTime() / (1000 * 60 * 60)) % 24);
                databaseId = -1;
                name = "";
                Calendar c = setOnCalender(hour, minute);
                time.setAlarm(this, c, name);
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
