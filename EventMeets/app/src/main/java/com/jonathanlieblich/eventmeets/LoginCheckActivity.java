package com.jonathanlieblich.eventmeets;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoginCheckActivity extends AppCompatActivity {
    private boolean isConnected = false;

    // TODO: Check SharedPreferences for network connection
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);

    }

    // TODO: isConnected ? attempt login : show error
}
