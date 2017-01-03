package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DealWithGoldDetails extends AppCompatActivity {
    public static final String TAG = "DWGDetails";

    TextView mTitle, mOriginalPrice, mNewPrice, mDescription, mDevName, mGenreView;
    ImageView mImageView, mPrevious, mNext;

    TextView mGenre;
    ImageView mCrossout;
    ProgressBar mProgressBar;

    String mGameID, mGameTitle, mGameDescription, mGameDevName, mGameOldPrice, mGameNewPrice;
    List<String> mImgURLs;
    List<String> mGenres;
    int currentIndex;
    int startIndex;
    int endIndex;

    AsyncTask<Void,Void,Void> mTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_with_gold_details);
        mImgURLs = new ArrayList<>();
        mGenres = new ArrayList<>();

        mProgressBar = (ProgressBar)findViewById(R.id.dwgd_progress_bar);

        mTitle = (TextView)findViewById(R.id.dwgd_title);
        mOriginalPrice = (TextView)findViewById(R.id.dwgd_old_price);
        mNewPrice = (TextView)findViewById(R.id.dwgd_new_price);
        mDescription = (TextView)findViewById(R.id.dwgd_description);
        mDevName = (TextView)findViewById(R.id.dwgd_dev_name);
        mGenreView = (TextView)findViewById(R.id.dwgd_genre_list);
        mImageView = (ImageView)findViewById(R.id.dwgd_main_img);
        mNext = (ImageView)findViewById(R.id.dwgd_next_button);
        mPrevious = (ImageView)findViewById(R.id.dwgd_previous_button);

        mGenre = (TextView)findViewById(R.id.dwgd_genres);
        mCrossout = (ImageView)findViewById(R.id.dwgd_crossout);

        Intent intent = getIntent();
        mGameID = intent.getStringExtra("id");
        mGameOldPrice = intent.getStringExtra("oldp");
        mGameNewPrice = intent.getStringExtra("newp");

        mTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .headers(MainActivity.mHeaders)
                        .url("https://xboxapi.com/v2/game-details/" + mGameID)
                        .build();

                try {
                    Response response = client.newCall(request).execute();

                    JSONObject responseObj = new JSONObject(response.body().string());
                    JSONArray item = responseObj.getJSONArray("Items");
                    JSONObject game = item.getJSONObject(0);

                    mGameTitle = game.getString("Name");
                    mGameDescription = game.getString("Description");
                    mGameDevName = game.getString("DeveloperName");

                    JSONArray imgArray = game.getJSONArray("Images");
                    for (int i = 0; i < imgArray.length(); i++){
                        String img = imgArray.getJSONObject(i).getString("Url");
                        mImgURLs.add(img);
                        Log.d(TAG, "doInBackground: added img #" + mImgURLs.size() + "  url --- " + img);
                    }

                    JSONArray genreArray = game.getJSONArray("Genres");
                    for (int i = 0; i < genreArray.length(); i++){
                        String genre = genreArray.getJSONObject(i).getString("Name");
                        mGenres.add(genre);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mTitle.setText(mGameTitle);
                mOriginalPrice.setText(mGameOldPrice);
                mNewPrice.setText(mGameNewPrice);
                mDescription.setText(mGameDescription);
                mDevName.setText(mGameDevName);
                mGenreView.setText(mGenres.toString());
                fillImageView(mImgURLs);

                mProgressBar.setVisibility(View.INVISIBLE);

                mCrossout.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mGenre.setVisibility(View.VISIBLE);
                mDescription.setVisibility(View.VISIBLE);
                mDevName.setVisibility(View.VISIBLE);
                mGenreView.setVisibility(View.VISIBLE);
                mOriginalPrice.setVisibility(View.VISIBLE);
                mNewPrice.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.VISIBLE);
                mNext.setVisibility(View.VISIBLE);
                mPrevious.setVisibility(View.VISIBLE);

            }
        }.execute();

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex >= endIndex){
                    //do nothing
                }else {
                    nextImage();
                }
            }
        });

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex <= startIndex){
                    //do nothing
                }else {
                    previousImage();
                }
            }
        });
    }

    public void fillImageView(List<String> list){
        startIndex = 0;
        endIndex = list.size()-1;
        currentIndex = 0;
        Picasso.with(this).load(mImgURLs.get(currentIndex)).into(mImageView);
        currentIndex++;
    }

    public void nextImage(){
        currentIndex++;
        Picasso.with(this).load(mImgURLs.get(currentIndex)).into(mImageView);
    }

    public void previousImage(){
        currentIndex--;
        Picasso.with(this).load(mImgURLs.get(currentIndex)).into(mImageView);
    }
}
