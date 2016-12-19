package com.example.jon.eventmeets;

import android.app.Dialog;
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
    private Button mLoginButton, mCreateAccount, mSkipLogin;
    private EditText mAccountName, mPassword, mHiddenConfirm;
    private TextView mHiddenConfirmPassword;
    private BaseLoginContract.Presenter mPresenter;
    private Dialog mDialog;

    public BaseLoginDialogFragment(BaseLoginContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public BaseLoginDialogFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.not_logged_in_dialog, container, true);
        mLoginButton = (Button)view.findViewById(R.id.login_prompt_button);
        mCreateAccount = (Button)view.findViewById(R.id.create_account_btn);
        mSkipLogin = (Button)view.findViewById(R.id.skip_login);

        mAccountName = (EditText)view.findViewById(R.id.username_edit);
        mPassword = (EditText)view.findViewById(R.id.password_edit);

        mHiddenConfirmPassword = (TextView)view.findViewById(R.id.hidden_confirm_new_password);
        mHiddenConfirm = (EditText)view.findViewById(R.id.hidden_confirm_edit);

        mLoginButton.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mSkipLogin.setOnClickListener(this);

        return view;
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
        return mDialog;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_prompt_button:
                mPresenter.checkLoginDetails(mAccountName.getText().toString(),mPassword.getText().toString());
                break;
            case R.id.create_account_btn:
                Toast.makeText(getActivity(), "create", Toast.LENGTH_SHORT).show();
                mHiddenConfirmPassword.setVisibility(View.VISIBLE);
                mHiddenConfirm.setVisibility(View.VISIBLE);
                mLoginButton.setVisibility(View.GONE);
                mCreateAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = mAccountName.getText().toString();
                        String password = mPassword.getText().toString();
                        String confirm = mHiddenConfirm.getText().toString();
                        mPresenter.onNewAccountRequested(username, password, confirm);
                    }
                });
                break;
            case R.id.skip_login:
                Toast.makeText(getActivity(), "skip", Toast.LENGTH_SHORT).show();
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
        }
    }
}
