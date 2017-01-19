package zt.sakoonkinamaz.broadcast;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import zt.sakoonkinamaz.R;
import zt.sakoonkinamaz.activity.MainActivity;
import zt.sakoonkinamaz.bean.Bean;
import zt.sakoonkinamaz.database.PrayersDataSource;

import static zt.sakoonkinamaz.publicData.PublicClass.IntToLong;

/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class PrayerTime extends Service {

    private MatchTime time = new MatchTime();
    private PrayersDataSource prayers;
    private ArrayList<Bean> beanArray = null;
    private Bean currentBean = null;
    private int hour, minute;
    private String name;
    boolean isStart = true;
    long dbId = 0;
    int profile = 100;
    private NotificationManager mNotificationManager;
    public static final int NOTIFICATION_ID = 33;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        prayers = new PrayersDataSource(this);
        prayers.open();
        beanArray = prayers.getAllPrayers();
        Collections.sort(beanArray);
        MoveToNextAndStart(isStart, dbId, profile);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void MoveToNextAndStart(boolean flagStartEnd, long databaseID, int currentProfile) {
        if(!flagStartEnd){
            doWithEndTime(databaseID, currentProfile);
        } else {
            doWithStartTime();
        }
    }

    private void doWithEndTime(long id, int currentProfile) {
        for(int i = 0; i < beanArray.size(); i++){
            if(id == beanArray.get(i).getId()){
                currentBean = beanArray.get(i);
                minute = (int) ((currentBean.getEndTime() / (1000 * 60)) % 60);
                hour =(int) ((currentBean.getEndTime() / (1000 * 60 * 60)) % 24);
                dbId = 0;
                isStart = false;
                name = "";
                profile = currentProfile;
                break;
            }
        }
        time.setAlarm(this, hour, minute, name, isStart, dbId, profile);
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
                if(count == beanArray.size());
                {
                    currentBean = beanArray.get(0);
                    minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                    hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                    dbId = currentBean.getId();
                    name = currentBean.getName();
                    isStart = true;
                    profile = 100;
                }
            } else {
                currentBean = beanArray.get(i);
                minute =(int) ((currentBean.getStartTime() / (1000 * 60)) % 60);
                hour =(int) ((currentBean.getStartTime() / (1000 * 60 * 60)) % 24);
                dbId = currentBean.getId();
                name = currentBean.getName();
                isStart = true;
                profile = 100;
                break;
            }
        }
        time.setAlarm(this, hour, minute, name, isStart, dbId, profile);
    }

    public void showNotification(String name) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.notification_title))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(name + " time it is!"))
                        .setContentText(name + " time it is!");
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    public void hideNotification() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
    }
}
