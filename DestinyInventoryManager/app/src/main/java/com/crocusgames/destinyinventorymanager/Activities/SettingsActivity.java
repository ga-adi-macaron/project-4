package com.crocusgames.destinyinventorymanager.Activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.crocusgames.destinyinventorymanager.AppConstants;
import com.crocusgames.destinyinventorymanager.R;
import com.crocusgames.destinyinventorymanager.SyncAdapterObjects.SyncAdapterCancelledReceiver;

import static com.crocusgames.destinyinventorymanager.AppConstants.GRIND_MODE_STATUS;
import static com.crocusgames.destinyinventorymanager.AppConstants.USER_PREFERENCES;

public class SettingsActivity extends AppCompatActivity {
    private Account mAccount;
    private SwitchCompat mSwitchButton;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAccount = createSyncAccount(this);

        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isGrinding = sharedPreferences.getBoolean(GRIND_MODE_STATUS, false);

        mSwitchButton = (SwitchCompat) findViewById(R.id.sync_switch);
        TextView logoutButton = (TextView) findViewById(R.id.button_logout);

        if (isGrinding) {
            mSwitchButton.setChecked(true);
        }

        mSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(GRIND_MODE_STATUS, true);
                    editor.commit();
                    ContentResolver.removePeriodicSync(mAccount, AppConstants.AUTHORITY, Bundle.EMPTY);
                    ContentResolver.requestSync(mAccount, AppConstants.AUTHORITY, Bundle.EMPTY);
                    ContentResolver.setSyncAutomatically(mAccount, AppConstants.AUTHORITY,true);
                    ContentResolver.addPeriodicSync(
                            mAccount,
                            AppConstants.AUTHORITY,
                            Bundle.EMPTY,
                            300);
                    showGrindNotification();
                } else {
                    editor.putBoolean(GRIND_MODE_STATUS, false);
                    editor.commit();
                    ContentResolver.removePeriodicSync(mAccount, AppConstants.AUTHORITY, Bundle.EMPTY);
                    cancelGrindNotification();
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.clear();
                editor.commit();
                ContentResolver.removePeriodicSync(mAccount, AppConstants.AUTHORITY, Bundle.EMPTY);
                finish();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mSwitchButton.setChecked(false);
            }
        };

    }

    public static Account createSyncAccount(Context context) {
        Account newAccount = new Account(
                AppConstants.ACCOUNT, AppConstants.ACCOUNT_TYPE);

        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
        } else {
        }
        return newAccount;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) // Press Back Icon
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showGrindNotification() {
        Intent deleteIntent = new Intent(this, SyncAdapterCancelledReceiver.class);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // unique request code. Store this if you want to cancel it.

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_settings_black_30dp);
        builder.setContentTitle("Grind Mode On");
        builder.setContentText("Engrams are being automatically transferred to your Vault.");
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setPriority(Notification.PRIORITY_HIGH);
        builder.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Turn Grind Mode Off", pendingIntentCancel);
        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Engrams are being automatically transferred to your Vault.");
        bigText.setBigContentTitle("Grind Mode On");
        builder.setStyle(bigText);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    public void cancelGrindNotification() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(USER_PREFERENCES,
                Context.MODE_PRIVATE);
        boolean isGrinding = sharedPreferences.getBoolean(GRIND_MODE_STATUS, false);
        if (isGrinding) {
            mSwitchButton.setChecked(true);
        } else {
            mSwitchButton.setChecked(false);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((mReceiver),
                new IntentFilter(AppConstants.NOTIF_CANCELLED)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }
}
