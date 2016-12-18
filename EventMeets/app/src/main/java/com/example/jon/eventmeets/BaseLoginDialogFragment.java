package com.example.jon.eventmeets;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.not_logged_in_dialog, container, true);
        mLoginButton = (Button)view.findViewById(R.id.login_prompt_button);
        mCreateAccount = (Button)view.findViewById(R.id.create_account_btn);
        mSkipLogin = (Button)view.findViewById(R.id.skip_login);

        mLoginButton.setOnClickListener(this);
        mCreateAccount.setOnClickListener(this);
        mSkipLogin.setOnClickListener(this);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

//        mLoginButton = (Button)dialog.findViewById(R.id.login_prompt_button);
//        mCreateAccount = (Button)dialog.findViewById(R.id.create_account_btn);
//        mSkipLogin = (Button)dialog.findViewById(R.id.skip_login_btn);
//
//        mAccountName = (EditText)dialog.findViewById(R.id.username_edit);
//        mPassword = (EditText)dialog.findViewById(R.id.password_edit);
//
//        mHiddenConfirm = (EditText)dialog.findViewById(R.id.hidden_confirm_edit);
//        mHiddenConfirmPassword = (TextView)dialog.findViewById(R.id.hidden_confirm_new_password);

        dialog.show();

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();

        lp.gravity = Gravity.CENTER;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;

        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);

        return dialog;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        mLoginButton.setOnClickListener(this);
//        mCreateAccount.setOnClickListener(this);
//        mSkipLogin.setOnClickListener(this);
//    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch(id) {
            case R.id.login_prompt_button:
                Toast.makeText(getActivity(), "login", Toast.LENGTH_SHORT).show();
                break;
            case R.id.create_account_btn:
                Toast.makeText(getActivity(), "create", Toast.LENGTH_SHORT).show();
                break;
            case R.id.skip_login:
                Toast.makeText(getActivity(), "skip", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), "Extra listener", Toast.LENGTH_SHORT).show();
        }
    }
}
