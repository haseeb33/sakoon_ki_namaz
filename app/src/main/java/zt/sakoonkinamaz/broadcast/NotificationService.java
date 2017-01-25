package zt.sakoonkinamaz.broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import zt.sakoonkinamaz.R;
import zt.sakoonkinamaz.activity.MainActivity;

/**
 * Created by Haseeb Bhai on 1/19/2017.
 */

public class NotificationService extends Service {

    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 3333;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = intent.getStringExtra("activeSlot");
        SharedPreferences pref = getSharedPreferences("SakoonKiNamaz", MODE_PRIVATE);
        Boolean isStart = pref.getBoolean("isStart", true);
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire();
        showNotification(name);
//        if (isStart) {
//            showNotification(name);
//        } else {
//            hideNotification();
//        }
        wl.release();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotification(String name) {

        mNotificationManager = (NotificationManager) this.
                getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
//
        /* Use Bellow commented code to enable notification using support library
        * Add google play services in dependency and this work for all Api levels upto API 4 (version 4)
        * */
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setContentTitle(getString(R.string.notification_title))
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(name + " time it is!"))
//                .setContentText(name + " time it is!");
//        mBuilder.setContentIntent(contentIntent);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
            Notification noti = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(name + " time it is!")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(contentIntent)
                    .getNotification();
            mNotificationManager.notify(NOTIFICATION_ID, noti);

        } else {
            Notification noti = new Notification.Builder(this)
                    .setContentTitle(getString(R.string.notification_title))
                    .setContentText(name + " time it is!")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentIntent(contentIntent)
                    .build();
            mNotificationManager.notify(NOTIFICATION_ID, noti);

        }
//        mNotificationManager.notify(NOTIFICATION_ID, mBuilder);

    }

    public void hideNotification() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION_ID);
        stopSelf();
    }
}
