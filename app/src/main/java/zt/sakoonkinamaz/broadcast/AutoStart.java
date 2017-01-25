package zt.sakoonkinamaz.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 *
 * Created by Haseeb Bhai on 1/19/2017.
 */

public class AutoStart extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, PrayerTime.class);
        context.startService(i);
    }
}
