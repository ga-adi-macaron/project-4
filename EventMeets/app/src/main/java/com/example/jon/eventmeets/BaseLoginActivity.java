package com.example.jon.eventmeets;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.jon.eventmeets.main_menu.MainMenuView;

public class BaseLoginActivity extends AppCompatActivity implements BaseLoginContract.View, View.OnClickListener{
    private BaseLoginContract.Presenter mPresenter;
    private BaseLoginDialogFragment mFragment;

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.addFirebaseListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.removeFirebaseListener();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_login);

        findViewById(R.id.login_main_btn).setOnClickListener(this);
        findViewById(R.id.skip_login_btn).setOnClickListener(this);

        mPresenter = new BaseLoginPresenter(this);

        mPresenter.onUserReturn();
    }

    @Override
    public void displayLoginDialog() {
        FragmentManager manager = getSupportFragmentManager();
        mFragment = new BaseLoginDialogFragment(mPresenter);

        manager.beginTransaction()
                .add(mFragment, "login")
                .commit();
    }

    @Override
    public void startMainMenuActivity() {
        Intent intent = new Intent(this, MainMenuView.class);
        intent.putExtra("logged in", true);
        startActivity(intent);
        ActivityCompat.finishAffinity(this);
    }

    @Override
    public void skipLogin() {
        Intent intent = new Intent(this, MainMenuView.class);
        intent.putExtra("logged in", false);
        startActivity(intent);
    }

    @Override
    public void notifyFragmentSuccess() {
        mFragment.showAccountCreationResult("success");
    }

    @Override
    public void notifyFragmentFailure() {
        mFragment.showAccountCreationResult("failure");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_main_btn:
                mPresenter.onLoginPressed();
                break;
            case R.id.skip_login_btn:
                mPresenter.onLoginSkipped();
                break;
            default:
                Log.d("BaseLoginActivity", "onClick: extra listener: "+view.getId());
        }
    }
}
