package com.example.jon.eventmeets;

import android.widget.EditText;

/**
 * Created by Jon on 12/16/2016.
 */

public interface BaseLoginContract {
    interface View {
        void displayLoginDialog();

        void startMainMenuActivity();

        void skipLogin();

        void notifyFragmentSuccess();

        void notifyFragmentFailure();

        void checkSharedPreferences();

        void addAccountInfoToSharedPreferences(String username, String password);
    }

    interface Presenter {
        void onLoginPressed();

        void notifyLoginSuccess(boolean wasSuccessful);

        void onLoginSkipped();

        void checkLoginDetails(String username, String password);

        void onUserReturn(String username, String password);

        void onNewAccountRequested(String username, String password, String confirmPassword);

        void addFirebaseListener();

        void removeFirebaseListener();

        void onLoginError();
    }
}
