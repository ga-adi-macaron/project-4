package com.korbkenny.peoplesplaylist;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.korbkenny.peoplesplaylist.coloring.ColoringActivity;
import com.korbkenny.peoplesplaylist.maps.MapsActivity;
import com.korbkenny.peoplesplaylist.objects.User;

public class LoginActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String SHARED_PREF = "MySettings";
    public static final String LOGGEDIN = "LoggedIn";

    private EditText signupEmail, signupPassword, loginEmail, loginPassword;
    private RelativeLayout layoutSignup, layoutLogin, layoutAlready;
    private CardView loadingCard;
    private TextView loadingText;
    private int signUp = 0;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseUserReference, mUserRef;

    private UserSingleton sUserSingleton;
    private User ME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //
        //                     Setup
        //
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        sUserSingleton = UserSingleton.getInstance();
        ME = new User();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        loadingCard = (CardView) findViewById(R.id.loading_login_card);
        loadingText = (TextView) findViewById(R.id.loading_login_text);

        createAuthListener();

        mDatabaseUserReference = mFirebaseDatabase.getReference("Users");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRidOfLoadingScreen();
            }
        }, 3000);

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //
        //                     Views
        //
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        signupEmail = (EditText)findViewById(R.id.signup_username);
        signupPassword = (EditText)findViewById(R.id.signup_password);
        loginEmail = (EditText)findViewById(R.id.login_username);
        loginPassword = (EditText)findViewById(R.id.login_password);

        layoutSignup = (RelativeLayout)findViewById(R.id.create_layout);
        layoutLogin = (RelativeLayout)findViewById(R.id.login_layout);
        layoutAlready = (RelativeLayout)findViewById(R.id.sign_in_layout);

        Button signUpButton = (Button)findViewById(R.id.signup_button);
        Button logInButton = (Button)findViewById(R.id.login_button);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(signupEmail.getText().toString(),signupPassword.getText().toString());
            }
        });


        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        //
        //           Change to Login Screen
        //
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        layoutAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSignup.setVisibility(View.GONE);
                layoutAlready.setVisibility(View.GONE);
                layoutLogin.setVisibility(View.VISIBLE);

                loginEmail.requestFocus();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(loginEmail.getText().toString(),loginPassword.getText().toString());
            }
        });
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //               Auth Listeners
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //                Sign in/Sign up
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    public void signIn(String email, String password){
        if (!validateLoginForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                        }
                        if (task.isSuccessful()){
                            String userId = task.getResult().getUser().getUid();
                            mDatabaseUserReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(final DataSnapshot dataSnapshot) {
                                    new AsyncTask<Void,Void,Void>(){

                                        @Override
                                        protected Void doInBackground(Void... voids) {
                                            sUserSingleton.setUser(dataSnapshot.getValue(User.class));
                                            return null;
                                        }

                                        @Override
                                        protected void onPostExecute(Void aVoid) {
                                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putBoolean(LOGGEDIN,true);
                                            editor.commit();
                                        }
                                    }.execute();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }
                });
    }

    public void createAccount(final String email, String password){
        if (!validateSignupForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                        }
                        if(task.isSuccessful()){
                            new AsyncTask<Void,Void,String>(){
                                @Override
                                protected String doInBackground(Void... voids) {
                                    return task.getResult().getUser().getUid();
                                }

                                @Override
                                protected void onPostExecute(String userId) {
                                    ME.setId(userId);
                                    ME.setUserName(email);
                                    ME.setUserImage("null");
                                    ME.setPlaylistCount(0);
                                    sUserSingleton.setUser(ME);
                                    mDatabaseUserReference.child(userId).setValue(ME);

                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(LOGGEDIN,true);
                                    editor.commit();
                                }
                            }.execute();
                        }
                    }
                });
    }



    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //
    //               Validate Forms
    //
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private boolean validateSignupForm() {
        boolean valid = true;

        String email = signupEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            signupEmail.setError("Required.");
            valid = false;
        } else if (!isEmailValid(email)){
            signupEmail.setError("Not a Valid Email.");
            valid = false;
        } else {
            signupEmail.setError(null);
        }

        String password = signupPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            signupPassword.setError("Required.");
            valid = false;
        } else if (!isPasswordValid(password)) {
            signupPassword.setError("Too Short.");
            valid = false;
        } else {
            signupPassword.setError(null);
        }
        return valid;
    }

    private boolean validateLoginForm() {
        boolean valid = true;

        String email = loginEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Required.");
            valid = false;
        } else if (!isEmailValid(email)){
            loginEmail.setError("Not a Valid Email.");
            valid = false;
        } else {
            loginEmail.setError(null);
        }

        String password = loginPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Required.");
            valid = false;
        } else if (!isPasswordValid(password)) {
            loginPassword.setError("Too Short.");
            valid = false;
        } else {
            loginPassword.setError(null);
        }
        return valid;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    private void createAuthListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    mUserRef = mFirebaseDatabase.getReference("Users").child(user.getUid());
                    mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                new AsyncTask<Void,Void,Void>(){
                                    @Override
                                    protected Void doInBackground(Void... voids) {
                                        ME = dataSnapshot.getValue(User.class);
                                        return null;
                                    }

                                    @Override
                                    protected void onPostExecute(Void aVoid) {
                                        sUserSingleton.setUser(ME);
                                            Intent intent = new Intent(LoginActivity.this, ColoringActivity.class);
                                            startActivity(intent);
                                            finish();
                                    }
                                }.execute();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    getRidOfLoadingScreen();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void getRidOfLoadingScreen(){
        if(loadingCard.getVisibility()==View.VISIBLE) {
            ValueAnimator ani = ValueAnimator.ofFloat(1f, 0f);
            ani.setDuration(700);
            ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    loadingCard.setAlpha((float) animation.getAnimatedValue());
                    loadingText.setAlpha((float) animation.getAnimatedValue());
                    if (loadingCard.getAlpha() == 0f || loadingText.getAlpha() == 0f) {
                        loadingCard.setVisibility(View.GONE);
                        loadingText.setVisibility(View.GONE);
                    }
                }
            });
            ani.start();
        }
    }
}


