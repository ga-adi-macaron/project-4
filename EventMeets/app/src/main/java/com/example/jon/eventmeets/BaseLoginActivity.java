package com.example.jon.eventmeets;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jon.eventmeets.EventCategoryBrowser.EventBrowseViewActivity;

public class BaseLoginActivity extends AppCompatActivity implements BaseLoginContract.View, View.OnClickListener{
    private BaseLoginContract.Presenter mPresenter;

    private TextView mConfirm;
    private EditText mAccountName, mPassword, mConfirmPassword;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_login);

        findViewById(R.id.login_main_btn).setOnClickListener(this);
        findViewById(R.id.skip_login_btn).setOnClickListener(this);

        mPresenter = new BaseLoginPresenter(this);
    }

    @Override
    public void displayLoginDialog() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(new BaseLoginDialogFragment(), "login")
                .commit();
//        AlertDialog dialog = new AlertDialog.Builder(this)
//                .setView(R.layout.not_logged_in_dialog)
//                .setCancelable(false)
//                .create();
//
//        LayoutInflater.from(this).inflate(R.layout.not_logged_in_dialog, null, false);
//        //Login dialog buttons' click listeners
//        Button login = (Button)findViewById(R.id.login_prompt_button);
//                login.setOnClickListener(this);
//        Button skip = (Button)findViewById(R.id.skip_login_btn);
//                skip.setOnClickListener(this);
//        Button create = (Button)findViewById(R.id.create_account_btn);
//                create.setOnClickListener(this);
//
//        dialog.show();
//
//        //Getting references to EditTexts
//        mAccountName = (EditText)findViewById(R.id.username_edit);
//        mPassword = (EditText)findViewById(R.id.password_edit);
//
//        //Hidden password confirmation elements
//        mConfirmPassword = (EditText)findViewById(R.id.hidden_confirm_edit);
//        mConfirm = (TextView)findViewById(R.id.hidden_confirm_new_password);
    }

    @Override
    public void startMainMenuActivity() {
        Intent intent = new Intent(this, EventBrowseViewActivity.class);
        intent.putExtra("logged in", true);
        startActivity(intent);
    }

    @Override
    public void skipLogin() {
        Intent intent = new Intent(this, EventBrowseViewActivity.class);
        intent.putExtra("logged in", false);
        startActivity(intent);
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
            case R.id.login_prompt_button:
                mPresenter.checkLoginDetails(mAccountName.getText().toString(), mPassword.getText().toString());
                break;
            case R.id.create_account_btn:
                mConfirm.setVisibility(View.VISIBLE);
                mConfirmPassword.setVisibility(View.VISIBLE);
                break;
            default:
                Log.d("BaseLoginActivity", "onClick: extra listener: "+view.getId());
        }
    }
}
