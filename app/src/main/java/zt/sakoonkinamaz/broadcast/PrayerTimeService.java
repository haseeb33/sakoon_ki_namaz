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

/***
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class PrayerTimeService extends Service {


    public static int previousProfile = AudioManager.RINGER_MODE_SILENT;

    private MatchTimeReceiver time = new MatchTimeReceiver();
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
        previousProfile = AudioManager.RINGER_MODE_SILENT;
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

        for(int i = 0; i < beanArray.size(); i++){
            if(currentTime >= beanArray.get(i).getStartTime()){
                count++;
                if(currentBean == null && beanArray.get(i).getStartTime() != -1){
                    currentBean = beanArray.get(i);
                }
                if(currentBean != null && count == beanArray.size())
                {
                    String name = currentBean.getName();
                    Calendar startCalender = setOnCalender(currentBean.getStartTime());
                    startCalender.set(Calendar.DAY_OF_YEAR, date+1);
                    time.handleTime(this, startCalender, name, 0);

                    name = "";
                    Calendar endCalender = setOnCalender(currentBean.getEndTime());
                    endCalender.set(Calendar.DAY_OF_YEAR, date+1);
                    time.handleTime(this, endCalender, name, 1);
//                    setMobileSilent(currentBean, date+1);
                }
            } else {
                currentBean = beanArray.get(i);
                String name = currentBean.getName();
                Calendar startCalender = setOnCalender(currentBean.getStartTime());
                time.handleTime(this, startCalender, name, 0);

                name = "";
                Calendar endCalender = setOnCalender(currentBean.getEndTime());
                time.handleTime(this, endCalender, name, 1);

//                setMobileSilent(currentBean, date);
                break;
            }
        }
    }

//    private void setMobileSilent(Bean b, int date){
//        if ( b.getEndTime() > 0 ) {
//
//            String name = b.getName();
//            Calendar startCalender = setOnCalender(b.getStartTime());
//            startCalender.set(Calendar.DAY_OF_YEAR, date);
//            time.handleTime(this, startCalender, name, 0);
//
//            if (b.getStartTime() > b.getEndTime()) {
//                date+=1;
//            }
//
//            name = "";
//            Calendar endCalender = setOnCalender(b.getEndTime());
//            endCalender.set(Calendar.DAY_OF_YEAR, date);
//            time.handleTime(this, endCalender, name, 1);
//        }
//    }

    private Calendar setOnCalender(long time){
        int minute = (int) ((time / (1000 * 60)) % 60);
        int hour = (int) ((time / (1000 * 60 * 60)) % 24);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }
}
