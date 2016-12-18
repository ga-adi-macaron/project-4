package com.example.jon.eventmeets;

/**
 * Created by Jon on 12/16/2016.
 */

public interface BaseLoginContract {
    interface View {
        void displayLoginDialog();
        void startMainMenuActivity();
        void skipLogin();
    }

    interface Presenter {
        void onLoginPressed();
        void notifyLoginSuccess(boolean wasSuccessful);
        void onLoginSkipped();
        void checkLoginDetails(String username, String password);
        void onUserReturn();
        boolean onNewAccountRequested(String username, String password, String confirmPassword);
    }
}
