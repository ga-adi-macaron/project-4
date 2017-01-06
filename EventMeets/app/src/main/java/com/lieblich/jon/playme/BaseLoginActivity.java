package com.lieblich.jon.playme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.lieblich.jon.playme.main_menu.MainMenuView;

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

        mPresenter = new BaseLoginPresenter(this);

        checkSharedPreferences();
    }

    @Override
    public void displayLoginDialog() {
        FragmentManager manager = getSupportFragmentManager();
        mFragment = new BaseLoginDialogFragment();
        mFragment.setPresenter(mPresenter);

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
    public void notifyFragmentSuccess() {
        mFragment.showAccountCreationResult("success");
    }

    @Override
    public void notifyFragmentFailure(String reason) {
        mFragment.showAccountCreationResult(reason);
    }

    @Override
    public void checkSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        if(preferences.getString("username", null) != null && preferences.getString("password", null) != null) {
            mPresenter.onUserReturn(preferences.getString("username", null), preferences.getString("password", null));
        }
    }

    @Override
    public void addAccountInfoToSharedPreferences(String username, String password) {
        SharedPreferences preferences = getSharedPreferences("account", MODE_PRIVATE);
        preferences.edit().putString("username", username).putString("password", password).apply();
    }

    @Override
    public void sendLoginErrorToFragment(String error) {
        mFragment.displayError(error);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_main_btn:
                mPresenter.onLoginPressed();
                break;
            default:
                Log.d("BaseLoginActivity", "onClick: extra listener: "+view.getId());
        }
    }
}
