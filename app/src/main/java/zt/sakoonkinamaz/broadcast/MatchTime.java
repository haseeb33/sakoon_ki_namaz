package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static zt.sakoonkinamaz.broadcast.PrayerTime.previousProfile;


/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class MatchTime extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentProfile = am.getRingerMode();
        am.setRingerMode(previousProfile);
        previousProfile = currentProfile;
        String name = intent.getStringExtra("slotName");
        callNotificationService(context, name);
        SharedPreferences pref = context.getSharedPreferences("SakoonKiNamaz", MODE_PRIVATE);
        Boolean isStart = pref.getBoolean("isStart", true);
        SharedPreferences.Editor editor = pref.edit();
        if(isStart){
            editor.putBoolean("isStart", false);
        } else {
            editor.putBoolean("isStart", true);
        }
        editor.commit();
        callPrayerTimeServiceAgain(context);
    }

    private void callPrayerTimeServiceAgain(Context context) {
        Intent startServiceAgain = new Intent(context, PrayerTime.class);
        context.startService(startServiceAgain);
    }

    public void setAlarm(Context context, Calendar calendar, String name) {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, MatchTime.class);
        i.putExtra("slotName", name);

        Toast.makeText(context, "Alaram is set: \n" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE), Toast.LENGTH_LONG).show();

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        } else {
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
        }
    }

    private void callNotificationService(Context context, String name) {
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("activeSlot", name);
        // Start the service, keeping the device awake while it is launching.
        context.startService(service);
    }


    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MatchTime.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
