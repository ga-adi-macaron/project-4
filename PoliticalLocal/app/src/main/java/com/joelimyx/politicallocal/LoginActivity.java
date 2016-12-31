package com.joelimyx.politicallocal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.*;
import com.firebase.ui.auth.BuildConfig;
import com.firebase.ui.auth.ui.ResultCodes;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.IdToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity{

    public static final int FIREBASE_SIGN_IN = 1;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mUser;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(v -> signIn());

        /*---------------------------------------------------------------------------------
        // Google Sign in
        ---------------------------------------------------------------------------------*/
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            mUser = firebaseAuth.getCurrentUser();
            if (mUser!=null){
                Toast.makeText(this,"Sign in successful", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    private void signIn(){
        AuthUI.IdpConfig googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).setPermissions(Arrays.asList(Scopes.PROFILE)).build();
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(Arrays.asList(googleIdp))
                        .setIsSmartLockEnabled(false)
                        .build(),
                FIREBASE_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == FIREBASE_SIGN_IN) {
            if (resultCode==RESULT_OK) {
                String token = IdpResponse.fromResultIntent(data).getIdpToken();
                firebaseAuthWithGoogle(token);

            } else if (resultCode == ResultCodes.RESULT_NO_NETWORK){
                Toast.makeText(this, "No Network Available", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED){
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String token){
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithCredential", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed: ");
        Intent intent = new Intent();
        intent.putExtra("back", true);
        setResult(RESULT_OK,intent);
        finish();
    }
}
