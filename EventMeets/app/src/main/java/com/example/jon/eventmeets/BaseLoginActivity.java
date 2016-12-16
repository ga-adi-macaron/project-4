package com.example.jon.eventmeets;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BaseLoginActivity extends AppCompatActivity {
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_login);

        mLoginButton = (Button)findViewById(R.id.login_main_btn);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginPrompt();
            }
        });
    }

    public void showLoginPrompt() {
        AlertDialog dialog = new AlertDialog.Builder(getApplicationContext())
                .setView(R.layout.not_logged_in_dialog)
                .create();
        dialog.getButton(R.id.login_prompt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        dialog.show();
    }
}
