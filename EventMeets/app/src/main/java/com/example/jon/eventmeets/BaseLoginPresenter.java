package com.example.jon.eventmeets;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.jon.eventmeets.model.AvailablePlayer;
import com.example.jon.eventmeets.model.RegularPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Jon on 12/16/2016.
 */

public class BaseLoginPresenter implements BaseLoginContract.Presenter {
    private BaseLoginContract.View mView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mFirst, mLast, mToken;
    private FirebaseUser mUser;

    public BaseLoginPresenter(BaseLoginContract.View view) {
        mView = view;
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
                if(mUser != null) {
                    UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mFirst+" "+mLast)
                            .build();
                    mUser.updateProfile(request);
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
            mView.startMainMenuActivity();
        } else {
            Toast.makeText((BaseLoginActivity)mView, "Invalid Credentials", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void checkLoginDetails(final String username, final String password) {
        if(username != null&&username.length() > 0&&password != null&&password.length() > 0) {
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                mView.sendLoginErrorToFragment("invalid");
                            } else {
                                mView.addAccountInfoToSharedPreferences(username,password);
                                notifyLoginSuccess(true);
                            }
                        }
                    });
        } else {
            mView.sendLoginErrorToFragment("empty");
        }
    }

    @Override
    public void onUserReturn(String username, String password) {
        if(username != null && password != null) {
            mAuth.signInWithEmailAndPassword(username, password);
            notifyLoginSuccess(true);
        }
    }

    @Override
    public void onNewAccountRequested(String username, String password, String confirmPassword, String firstName, String lastName) {

        if(username.length() > 0&&password.length() > 0&&firstName.length() > 0&&lastName.length() > 0) {
            if (password.equals(confirmPassword)) {
                requestAccount(username, password, firstName, lastName);
            } else {
                mView.sendLoginErrorToFragment("passwords");
            }
        } else {
            mView.sendLoginErrorToFragment("empty");
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

    private void addUserToDatabase(String userKey, String first, String last) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("users");
        RegularPlayer player = new RegularPlayer(userKey, first);
        ref.child(userKey).setValue(player);
    }

    private void requestAccount(final String username, final String password, String first, String last) {
        mFirst = first;
        mLast = last;
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            mView.notifyFragmentFailure(task.getResult().toString());
                        } else {
                            mView.notifyFragmentSuccess();
                            String userKey = mAuth.getCurrentUser().getUid();
                            addUserToDatabase(userKey, mFirst, mLast);
                            mView.addAccountInfoToSharedPreferences(username, password);
                        }
                    }
                });
    }
}
