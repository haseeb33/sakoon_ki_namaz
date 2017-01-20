package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.Calendar;

import static zt.sakoonkinamaz.broadcast.PrayerTime.previousProfile;


/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class MatchTime extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

        wl.acquire();

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentProfile = am.getRingerMode();
        am.setRingerMode(previousProfile);
        previousProfile = currentProfile;

        String name = intent.getStringExtra("slotName");

        callNotificationService(context, name);

        Intent startServiceAgain = new Intent(context, PrayerTime.class);
        context.stopService(startServiceAgain);
        wl.release();
    }

    private void callNotificationService(Context context, String name) {
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("activeSlot", name);
        // Start the service, keeping the device awake while it is launching.
        context.startService(service);

    }

    public void setAlarm(Context context, Calendar calendar, String name) {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, MatchTime.class);
        i.putExtra("slotName", name);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        }
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MatchTime.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
