package zt.sakoonkinamaz.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.widget.Toast;

import zt.sakoonkinamaz.R;

/**
 * Created by Haseeb Bhai on 1/15/2017.
 */

public class MatchTime extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();
        // AudioManager is controlling the profile mode
        AudioManager am= (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //For Normal mode
        if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT){
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
//        else if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
//            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
//        }
        else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }

//        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
        wl.release();

    }
    public void setAlarm(Context context) {
        AlarmManager am =(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, MatchTime.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60, pi); // Millisec * Second * Minute
    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, MatchTime.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
