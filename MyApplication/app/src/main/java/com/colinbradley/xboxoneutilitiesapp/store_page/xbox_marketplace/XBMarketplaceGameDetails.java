package com.colinbradley.xboxoneutilitiesapp.store_page.xbox_marketplace;

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

public class XBMarketplaceGameDetails extends AppCompatActivity {
    public static final String TAG = "XBMarketplaceGameDetail";

    TextView mTitle, mGameDev, mDescription, mPrice, mGenreListView;
    ImageView mImageView, mNextButton, mPreviousButton;

    TextView mGenre;

    String mGameID;
    String mGameTitle;
    String mGameDescription;
    String mGameDevName;
    String mGamePrice;
    List<String> mImgURLs;
    List<String> mGenres;

    ProgressBar mProgressBar;

    int startIndex;
    int endIndex;
    int currentIndex;

    AsyncTask<Void,Void,Void> mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xbmarketplace_game_details);

        mImgURLs = new ArrayList<>();
        mGenres = new ArrayList<>();

        mProgressBar = (ProgressBar)findViewById(R.id.xbmgd_progress_bar);

        mTitle = (TextView)findViewById(R.id.xbmgd_title);
        mGameDev = (TextView)findViewById(R.id.xbmgd_dev_name);
        mDescription = (TextView)findViewById(R.id.xbmgd_description);
        mPrice = (TextView)findViewById(R.id.xbmgd_price);
        mGenreListView = (TextView)findViewById(R.id.xbmgd_genre_list);
        mImageView = (ImageView)findViewById(R.id.xbmgd_main_img);
        mNextButton = (ImageView)findViewById(R.id.xbmgd_next_button);
        mPreviousButton = (ImageView)findViewById(R.id.xbmgd_previous_button);

        mGenre = (TextView)findViewById(R.id.xbmgd_genres);

        Intent intent = getIntent();
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
                    if (availabilitiesArray.length() == 0){
                        mGamePrice = "N/A";
                    }else {
                        JSONObject obj = availabilitiesArray.getJSONObject(0);
                        JSONObject offerDisplayData = obj.getJSONObject("OfferDisplayData");
                        double priceAsInt = offerDisplayData.getDouble("listPrice");

                        mGamePrice = "$" + priceAsInt;
                    }

                    JSONArray imgArray = game.getJSONArray("Images");
                    for (int i = 0; i < imgArray.length(); i++){
                        String img = imgArray.getJSONObject(i).getString("Url");
                        Log.d(TAG, "doInBackground: added img #" +  "  url -- " + img);

                        mImgURLs.add(img);
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
                mGameDev.setText(mGameDevName);
                mDescription.setText(mGameDescription);
                mPrice.setText(mGamePrice);
                mGenreListView.setText(mGenres.toString());
                fillImageView(mImgURLs);

                mProgressBar.setVisibility(View.INVISIBLE);

                mGenre.setVisibility(View.VISIBLE);
                mGenreListView.setVisibility(View.VISIBLE);
                mPrice.setVisibility(View.VISIBLE);
                mDescription.setVisibility(View.VISIBLE);
                mGameDev.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.VISIBLE);
                mNextButton.setVisibility(View.VISIBLE);
                mPreviousButton.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.VISIBLE);
            }
        }.execute();

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex >= endIndex){
                    //do nothing
                }else {
                    nextImage();
                }
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
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
