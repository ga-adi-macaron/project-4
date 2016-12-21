package com.korbkenny.peoplesplaylist;

import android.content.Intent;
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
import com.korbkenny.peoplesplaylist.coloring.ColoringActivity;

public class LoginActivity extends AppCompatActivity{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText signupEmail, signupPassword, loginEmail, loginPassword;
    private RelativeLayout layoutSignup, layoutLogin, layoutAlready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signUpButton = (Button)findViewById(R.id.signup_button);
        Button logInButton = (Button)findViewById(R.id.login_button);

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

        signupEmail = (EditText)findViewById(R.id.signup_username);
        signupPassword = (EditText)findViewById(R.id.signup_password);
        loginEmail = (EditText)findViewById(R.id.login_username);
        loginPassword = (EditText)findViewById(R.id.login_password);

        layoutSignup = (RelativeLayout)findViewById(R.id.create_layout);
        layoutLogin = (RelativeLayout)findViewById(R.id.login_layout);
        layoutAlready = (RelativeLayout)findViewById(R.id.sign_in_layout);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(signupEmail.getText().toString(),signupPassword.getText().toString());
            }
        });

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

    public void signIn(String email, String password){
        if (!validateSignupForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Failed to sign in", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createAccount(String email, String password){
        if (!validateSignupForm()) {
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                        }
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, ColoringActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

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

