package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import zt.sakoonkinamaz.R;

/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class MatchTime extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

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

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
            wl.acquire();
            // AudioManager is controlling the profile mode
//        AudioManager am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.azaan);
            mediaPlayer.start();
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
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MatchTime.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
