package com.example.jon.eventmeets;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BaseLoginActivity extends AppCompatActivity implements BaseLoginContract.View{
    private Button mLoginButton;
    private BaseLoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_login);

        mLoginButton = (Button)findViewById(R.id.login_main_btn);

        mPresenter = new BaseLoginPresenter(this);
    }

    @Override
    public void displayLoginDialog() {

    }

    @Override
    public void startMainMenuActivity() {

    }

    @Override
    public void skipLogin() {

    }
}
