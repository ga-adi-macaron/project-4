package com.lieblich.jon.playme;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jon on 12/18/2016.
 */

public class BaseLoginDialogFragment extends DialogFragment implements View.OnClickListener{
    private Button mLoginButton, mCreateAccount;
    private EditText mAccountName, mPassword, mHiddenConfirm, mFirstName, mLastName;
    private TextView mHiddenConfirmPassword, mFirst, mLast;
    private BaseLoginContract.Presenter mPresenter;
    private Dialog mDialog;
    private View mLayout;
    private String mName;

    public void setPresenter(BaseLoginContract.Presenter presenter, String name) {
        mPresenter = presenter;
        mName = name;
    }

    public BaseLoginDialogFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mLayout =  inflater.inflate(R.layout.not_logged_in_dialog, container, true);
        mLoginButton = (Button)mLayout.findViewById(R.id.login_prompt_button);
        mCreateAccount = (Button)mLayout.findViewById(R.id.create_account_btn);

        mAccountName = (EditText)mLayout.findViewById(R.id.username_edit);
        mPassword = (EditText)mLayout.findViewById(R.id.password_edit);

        mFirst = (TextView)mLayout.findViewById(R.id.first_name_text);
        mLast = (TextView)mLayout.findViewById(R.id.last_name_text);
        mHiddenConfirmPassword = (TextView)mLayout.findViewById(R.id.hidden_confirm_new_password);
        mHiddenConfirm = (EditText)mLayout.findViewById(R.id.hidden_confirm_edit);
        mFirstName = (EditText)mLayout.findViewById(R.id.first_name);
        mLastName = (EditText)mLayout.findViewById(R.id.last_name);

        mLoginButton.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);

        return mLayout;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = super.onCreateDialog(savedInstanceState);

        mDialog.show();

        WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();

        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        mDialog.getWindow().setAttributes(lp);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return mDialog;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_prompt_button:
                mDialog.setContentView(R.layout.logging_in_progress);
                mPresenter.checkLoginDetails(mAccountName.getText().toString(),mPassword.getText().toString(),mName);
                break;
            case R.id.create_account_btn:
                mHiddenConfirmPassword.setVisibility(View.VISIBLE);
                mHiddenConfirm.setVisibility(View.VISIBLE);
                mFirst.setVisibility(View.VISIBLE);
                mFirstName.setVisibility(View.VISIBLE);
                mLast.setVisibility(View.VISIBLE);
                mLastName.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                mCreateAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = mAccountName.getText().toString();
                        String password = mPassword.getText().toString();
                        String confirm = mHiddenConfirm.getText().toString();
                        String firstName = mFirstName.getText().toString();
                        String lastName = mLastName.getText().toString();
                        mDialog.setContentView(R.layout.logging_in_progress);
                        mPresenter.onNewAccountRequested(username, password, confirm,firstName ,lastName);
                    }
                });
                break;
            default:
                Toast.makeText(getActivity(), "Extra listener", Toast.LENGTH_SHORT).show();
        }
    }

    private void revertOnClickListener(View view) {
        view.setOnClickListener(this);
        mLoginButton.setVisibility(View.VISIBLE);
    }

    void showAccountCreationResult(String result) {
        if(result.equals("success")) {
            mPresenter.notifyLoginSuccess(true);
            revertOnClickListener(mCreateAccount);
        } else {
            mHiddenConfirm.setError(result);
            mDialog.setContentView(mLayout);
        }
    }

    void displayError(String error) {
        switch(error) {
            case "passwords":
                mPassword.setError("Passwords Must Match");
                mDialog.setContentView(mLayout);
                break;
            case "empty":
                mAccountName.setError("All Fields Required");
                mDialog.setContentView(mLayout);
                break;
            case "invalid":
                mAccountName.setError("Invalid Username and/or Password");
                mDialog.setContentView(mLayout);
                break;
            default:
                mDialog.setContentView(mLayout);
                Toast.makeText(getContext(), "Whoops", Toast.LENGTH_SHORT).show();
        }
    }
}
