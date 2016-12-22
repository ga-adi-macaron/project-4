package com.example.jon.eventmeets;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.jon.eventmeets.model.BaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseLoginPresenter implements BaseLoginContract.Presenter {
    private BaseLoginContract.View mView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public BaseLoginPresenter(BaseLoginContract.View view) {
        mView = view;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

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
    public void checkLoginDetails(final String username, final String password) {
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mView.addAccountInfoToSharedPreferences(username, password);
                            notifyLoginSuccess(true);
                        }
                    }
                });
    }

    @Override
    public void onUserReturn(String username, String password) {
        if(username != null && password != null) {
            mAuth.signInWithEmailAndPassword(username, password);
            notifyLoginSuccess(true);
        }
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
                                String userKey = mAuth.getCurrentUser().getUid();
                                addUserToDatabase(userKey);
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

    private void addUserToDatabase(String userKey) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");
        BaseUser user = new BaseUser(userKey, "Jon");
        ref.child(userKey).setValue(user);
    }
}
