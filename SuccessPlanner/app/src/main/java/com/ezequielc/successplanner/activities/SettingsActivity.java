package com.ezequielc.successplanner.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.ezequielc.successplanner.DatabaseHelper;
import com.ezequielc.successplanner.R;

public class SettingsActivity extends AppCompatActivity {
    public static final String PREFERENCES = "preferences";
    public static final String RECEIVE_QUOTE_SWITCH = "receiveQuoteSwitch";

    Switch mReceiveQuotes;
    TextView mDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");

        mReceiveQuotes = (Switch) findViewById(R.id.get_quote);
        mDeleteAll = (TextView) findViewById(R.id.delete_all_entries);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        boolean isQuoteSwitched = sharedPreferences.getBoolean(RECEIVE_QUOTE_SWITCH, true);

        // Switch for Receiving Quote
        mReceiveQuotes.setChecked(isQuoteSwitched);
        mReceiveQuotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(RECEIVE_QUOTE_SWITCH, mReceiveQuotes.isChecked());
                editor.commit();
            }
        });

        // Deletes all entries in database
        mDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingsActivity.this)
                        .setMessage("Delete All Entries?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
                                databaseHelper.deleteAll();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setCancelable(false)
                        .create().show();
            }
        });
    }
}
