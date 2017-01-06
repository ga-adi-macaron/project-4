package com.example.jon.eventmeets;

/**
 * Created by Jon on 12/16/2016.
 */

public interface BaseLoginContract {
    interface View {
        
        void displayLoginDialog();

        void startMainMenuActivity();

        void notifyFragmentSuccess();

        void notifyFragmentFailure(String reason);

        void checkSharedPreferences();

        void addAccountInfoToSharedPreferences(String username, String password);

        void sendLoginErrorToFragment(String error);
    }

    interface Presenter {
        void onLoginPressed();

        void notifyLoginSuccess(boolean wasSuccessful);

        void checkLoginDetails(String username, String password);

        void onUserReturn(String username, String password);

        void onNewAccountRequested(String username, String password, String confirmPassword, String firstName, String lastName);

        void addFirebaseListener();

        void removeFirebaseListener();
    }
}
