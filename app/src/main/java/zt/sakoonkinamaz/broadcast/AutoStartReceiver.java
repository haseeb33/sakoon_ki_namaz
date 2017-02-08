package zt.sakoonkinamaz.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import zt.sakoonkinamaz.services.PrayerTimeService;

/***
 * Created by Haseeb Bhai on 1/19/2017.
 */

public class AutoStartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PrayerTimeService.class);
        context.startService(i);
    }
}
