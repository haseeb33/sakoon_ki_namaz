package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.PowerManager;
import android.widget.Toast;

import java.util.Calendar;


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
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        String name = intent.getStringExtra("name");
        callNotificationService(context, name);

//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Intent startServiceIntent = new Intent(context, PrayerTime.class);
//            context.startService(startServiceIntent);
//        }
//        if(intent != null ) {
//            String name = intent.getStringExtra("name");
//            boolean flagStartEnd = intent.getBooleanExtra("isStart", true);
//            long databaseId = intent.getLongExtra("databaseID", 0);
//            int profile = intent.getIntExtra("currentProfile", 100);
//            PrayerTime prayerTime = new PrayerTime();
//            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//
//            if (flagStartEnd){
//                int currentProfile = am.getRingerMode();
//                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//                prayerTime.MoveToNextAndStart(true, databaseId, currentProfile);
//                prayerTime.showNotification(name);
//            } else {
//                am.setRingerMode(profile);
//                prayerTime.hideNotification();
//            }

            // AudioManager is controlling the profile mode
//        AudioManager am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            //For Normal mode
//        if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
//            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//        }
////        else if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
////            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
////        }
//        else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
//            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//        }

//        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
            wl.release();
//        }
    }

    private void callNotificationService(Context context, String name) {
        Intent service = new Intent(context, NotificationService.class);
        service.putExtra("activeSlot", name);
        // Start the service, keeping the device awake while it is launching.
        context.startService(service);

    }

    public void setAlarm(Context context, int hour, int min, String name, boolean isStart, long dbId, int profile) {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);

        Intent i = new Intent(context, MatchTime.class);
        i.putExtra("name", name);
        i.putExtra("isStart", isStart);
        i.putExtra("databaseID", dbId);
        i.putExtra("currentProfile", profile);

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MatchTime.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
