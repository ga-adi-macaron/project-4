package com.colinbradley.xboxoneutilitiesapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.colinbradley.xboxoneutilitiesapp.profile_page.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final Headers mHeaders = new Headers.Builder()
            .add("X-AUTH", "ad7d4e8efc551d21733549f7d2b6e13969714ba0")
            .build();

    EditText mEditText;
    Button mStartButton;

    AsyncTask<Void,Void,String> mTask;


    String mGamertag;
    String mXUID;
    String mGamerscore;
    String mProfilePic;
    String mAccountTier;
    String mURLforPreferedColor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mEditText = (EditText) findViewById(R.id.users_gt_input);
        mStartButton = (Button) findViewById(R.id.start_button);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGamertag = mEditText.getText().toString();
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    mTask = new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            OkHttpClient client = new OkHttpClient();

                            Request request = new Request.Builder()
                                    .url("https://xboxapi.com/v2/xuid/" + mGamertag)
                                    .headers(mHeaders)
                                    .build();
                            Log.d(TAG, "doInBackground: built Request:  " + request.toString());

                            try {
                                Response response = client.newCall(request).execute();
                                Log.d(TAG, "doInBackground: recieved Response:  " + response.body().toString());

                                mXUID = response.body().string();
                                Log.d(TAG, "doInBackground: XUID ---- " + mXUID);
                                return null;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(String aVoid) {
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            intent.putExtra("xuid", mXUID);
                            startActivity(intent);
                        }

                    }.execute();
                } else {
                    Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }





}
