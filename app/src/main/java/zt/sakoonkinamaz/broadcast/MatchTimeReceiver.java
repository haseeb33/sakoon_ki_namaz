package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;

import java.util.Calendar;

import zt.sakoonkinamaz.services.NotificationService;
import zt.sakoonkinamaz.services.PrayerTimeService;

import static zt.sakoonkinamaz.services.PrayerTimeService.previousProfile;


/***
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class MatchTimeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentProfile = am.getRingerMode();
        am.setRingerMode(previousProfile);
        previousProfile = currentProfile;
        String name = intent.getStringExtra("slotName");
        callNotificationService(context, name);

        if (name.equals("")) {
            previousProfile = AudioManager.RINGER_MODE_SILENT;
        }
    }

    public void handleTime(Context context, Calendar calendar, String name, int requestCode) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, MatchTimeReceiver.class);
        i.putExtra("slotName", name);

        PendingIntent startTime = PendingIntent.getBroadcast(context, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startTime); // Millisec * Second * Minute
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), startTime); // Millisec * Second * Minute
        }
    }

    private void callNotificationService(Context context, String name) {
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("activeSlot", name);
        context.startService(service);
    }

//    public void cancelAlarm(Context context) {
//        Intent intent = new Intent(context, MatchTime.class);
//        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(sender);
//    }
}
