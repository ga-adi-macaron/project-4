package com.example.jon.eventmeets;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseLoginPresenter implements BaseLoginContract.Presenter {
    private BaseLoginContract.View mView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mLoggedIn = true;
    private String mLoginError;

    public BaseLoginPresenter(BaseLoginContract.View view) {
        mView = view;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("firebase presenter", "onAuthStateChanged: "+user.getUid());
                } else {
                    Log.d("firebase presenter", "onAuthStateChanged: not logged in");
                }
            }
        };
    }

    @Override
    public void onLoginPressed() {
        mView.displayLoginDialog();
    }

    @Override
    public void notifyLoginSuccess(boolean wasSuccessful) {
        if (wasSuccessful) {
            Toast.makeText((BaseLoginActivity)mView, "Logged In Successfully", Toast.LENGTH_SHORT).show();
            mView.startMainMenuActivity();
        } else {
            Toast.makeText((BaseLoginActivity)mView, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoginSkipped() {
        mView.skipLogin();
    }

    @Override
    public void checkLoginDetails(String username, String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            notifyLoginSuccess(true);
                        }
                    }
                });
    }

    @Override
    public void onUserReturn() {
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null) {
//            mView.startMainMenuActivity();
//        }
    }

    @Override
    public void onNewAccountRequested(String username, String password, String confirmPassword) {

        if(password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                mView.notifyFragmentFailure();
                            } else {
                                mView.notifyFragmentSuccess();
                            }
                        }
                    });
        }
    }

    @Override
    public void addFirebaseListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void removeFirebaseListener() {
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onLoginError() {
        mLoginError = "Error";
    }

    private void onLoginTest() {
        mLoginError = "Success";
    }
}
