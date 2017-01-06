package com.crocusgames.destinyinventorymanager.SyncAdapterObjects;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.crocusgames.destinyinventorymanager.AppConstants.GRIND_MODE_STATUS;
import static com.crocusgames.destinyinventorymanager.AppConstants.NOTIF_CANCELLED;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;

/**
 * Created by Serkan on 04/01/17.
 */

public class SyncAdapterCancelledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(GRIND_MODE_STATUS, false);
        editor.commit();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
        Intent intentNotifCancelled = new Intent(NOTIF_CANCELLED);
        broadcaster.sendBroadcast(intentNotifCancelled);

        Log.d(TAG, "onReceive: SYNC CANCELLED BY NOTIFICATION");
    }
}
