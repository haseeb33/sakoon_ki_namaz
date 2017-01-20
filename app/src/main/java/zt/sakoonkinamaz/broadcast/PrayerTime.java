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
    public static boolean isStart = true;
    public static long databaseId = -1;
    public static int previousProfile = 0;

    private MatchTime time = new MatchTime();
    private PrayersDataSource prayers;
    private ArrayList<Bean> beanArray = null;
    private Bean currentBean = null;
    private int hour, minute;
    private String name;

    @Override
    public void onCreate() {
        prayers = new PrayersDataSource(this);
        prayers.open();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        beanArray = prayers.getAllPrayers();
        Collections.sort(beanArray);
        MoveToNextAndStart();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void MoveToNextAndStart() {
        if(isStart){
            doWithStartTime();
        } else {
            doWithEndTime(databaseId);
        }
    }

    private void doWithStartTime() {
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        long currentTime = IntToLong(h, m);
        int count = 0;
        for(int i = 0; i < beanArray.size(); i++){
            if(currentTime >= beanArray.get(i).getStartTime()){
               count++;
                if(count == beanArray.size())
                {
                    currentBean = beanArray.get(0);
                    minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                    hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                    databaseId = currentBean.getId();
                    name = currentBean.getName();
                    previousProfile = AudioManager.RINGER_MODE_SILENT;
                    isStart = true;
                }
            } else {
                currentBean = beanArray.get(i);
                minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                databaseId = currentBean.getId();
                name = currentBean.getName();
                isStart = true;
                break;
            }
        }
        if(minute != 0 && hour != 0) {
            time.setAlarm(this, hour, minute, name);
        }
    }

    private void doWithEndTime(long id) {
        for(int i = 0; i < beanArray.size(); i++){
            if(id == beanArray.get(i).getId()){
                currentBean = beanArray.get(i);
                minute = (int) ((currentBean.getEndTime() / (1000 * 60)) % 60);
                hour =(int) ((currentBean.getEndTime() / (1000 * 60 * 60)) % 24);
                databaseId = 0;
                isStart = false;
                name = "";
                break;
            }
        }
        time.setAlarm(this, hour, minute, name);
    }

}
