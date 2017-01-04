package com.korbkenny.peoplesplaylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.korbkenny.peoplesplaylist.objects.User;

public class LoginActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static final String SHARED_PREF = "MySettings";
    public static final String LOGGEDIN = "LoggedIn";

    private EditText signupEmail, signupPassword, loginEmail, loginPassword;
    private RelativeLayout layoutSignup, layoutLogin, layoutAlready;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseUserReference;

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

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    finish();
                }
            }
        };

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseUserReference = mFirebaseDatabase.getReference("Users");

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
    protected void onPause() {
        super.onPause();
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
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    sUserSingleton.setUser(dataSnapshot.getValue(User.class));

                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(LOGGEDIN,true);
                                    editor.commit();

                                    finish();
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
                                    ME.setUserImage("https://firebasestorage.googleapis.com/v0/b/peoplesplaylist-9c5d9.appspot.com/o/userplaceholder.png?alt=media&token=69ff913e-2eb2-46b5-a210-390e69ddac31");
                                    sUserSingleton.setUser(ME);
                                    mDatabaseUserReference.child(userId).setValue(ME);

                                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(LOGGEDIN,true);
                                    editor.commit();

                                    finish();
                                    Intent intent = new Intent(LoginActivity.this, ColoringActivity.class);
                                    startActivity(intent);
                                    finish();
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
        } else {
            signupEmail.setError(null);
        }

        String password = signupPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            signupPassword.setError("Required.");
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
        } else {
            loginEmail.setError(null);
        }

        String password = loginPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("Required.");
            valid = false;
        } else {
            loginPassword.setError(null);
        }
        return valid;
    }
}

