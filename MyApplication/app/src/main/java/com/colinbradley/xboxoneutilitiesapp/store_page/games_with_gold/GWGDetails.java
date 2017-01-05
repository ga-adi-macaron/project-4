package com.colinbradley.xboxoneutilitiesapp.store_page.games_with_gold;

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

public class GWGDetails extends AppCompatActivity {
    public static final String TAG = "GWGDetails";

    TextView mTitle, mOriginalPrice, mDescription, mDevName, mGenreView;
    ImageView mImageView, mPrevious, mNext;

    TextView mFreeWithGold, mGenre;
    ImageView mCrossout;
    ProgressBar mProgressBar;

    String mGameID, mGameTitle, mGameDescription, mGameDevName, mGamePrice;
    List<String> mImgURLs;
    List<String> mGenres;
    int currentIndex;
    int startIndex;
    int endIndex;

    AsyncTask<Void,Void,Void> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_with_gold_details);
        mImgURLs = new ArrayList<>();
        mGenres = new ArrayList<>();

        mProgressBar = (ProgressBar)findViewById(R.id.gwgd_progress_bar);

        mTitle = (TextView)findViewById(R.id.gwgd_title);
        mOriginalPrice = (TextView)findViewById(R.id.gwgd_price);
        mDescription = (TextView)findViewById(R.id.gwgd_description);
        mDevName = (TextView)findViewById(R.id.gwgd_dev_name);
        mImageView = (ImageView) findViewById(R.id.gwgd_main_img);
        mPrevious = (ImageView)findViewById(R.id.gwgd_previous_button);
        mNext = (ImageView)findViewById(R.id.gwgd_next_button);
        mGenreView = (TextView)findViewById(R.id.gwgd_genre_list);

        mGenre = (TextView)findViewById(R.id.gwgd_genres);
        mCrossout = (ImageView)findViewById(R.id.gwgd_crossout);
        mFreeWithGold = (TextView)findViewById(R.id.gwgd_free_w_gold);

        final Intent intent = getIntent();
        mGameID = intent.getStringExtra("id");

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

                    JSONArray availabilitiesArray = game.getJSONArray("Availabilities");
                    JSONObject obj = availabilitiesArray.getJSONObject(0);
                    JSONObject offerDisplayData = obj.getJSONObject("OfferDisplayData");
                    double priceAsInt = offerDisplayData.getDouble("listPrice");

                    mGamePrice = "$" + priceAsInt;

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
                mOriginalPrice.setText(mGamePrice);
                mDescription.setText(mGameDescription);
                mDevName.setText(mGameDevName);
                mGenreView.setText(mGenres.toString());
                fillImageView(mImgURLs);

                mProgressBar.setVisibility(View.INVISIBLE);

                mFreeWithGold.setVisibility(View.VISIBLE);
                mCrossout.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mGenre.setVisibility(View.VISIBLE);
                mDescription.setVisibility(View.VISIBLE);
                mDevName.setVisibility(View.VISIBLE);
                mGenreView.setVisibility(View.VISIBLE);
                mOriginalPrice.setVisibility(View.VISIBLE);
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
