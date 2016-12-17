package com.example.jon.eventmeets;

import java.util.regex.Pattern;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseLoginPresenter implements BaseLoginContract.Presenter {
    private BaseLoginContract.View mView;


    public BaseLoginPresenter(BaseLoginContract.View view) {
        mView = view;
    }

    @Override
    public void onLoginPressed(String username, String password) {
        if(Pattern.matches("|A-Z|a-z|0-9", username)) {

        }
    }

    @Override
    public void notifyLoginSuccess(boolean wasSuccessful) {

    }

    @Override
    public void onLoginSkipped() {

    }
}
