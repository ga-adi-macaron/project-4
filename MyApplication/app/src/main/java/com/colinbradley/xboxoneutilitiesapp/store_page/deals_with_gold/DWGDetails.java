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

import com.colinbradley.xboxoneutilitiesapp.FullscreenImageActivity;
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

public class DWGDetails extends AppCompatActivity {
    public static final String TAG = "DWGDetails";
    public static final String IMG_LIST_KEY = "imgList";
    public static final String GENRE_LIST_KEY = "genreList";
    public static final String ID_KEY = "id";
    public static final String TITLE_KEY = "title";
    public static final String DESCRIPTION_KEY = "description";
    public static final String DEV_KEY = "dev";
    public static final String OLD_PRICE_KEY = "old";
    public static final String NEW_PRICE_KEY = "new";
    public static final String CURRENT_INDEX_KEY = "curIndex";

    TextView mTitle, mOriginalPrice, mNewPrice, mDescription, mDevName, mGenreView;
    ImageView mImageView, mPrevious, mNext;

    TextView mGenre;
    ImageView mCrossout;
    ProgressBar mProgressBar;

    String mGameID, mGameTitle, mGameDescription, mGameDevName, mGameOldPrice, mGameNewPrice;
    ArrayList<String> mImgURLs;
    ArrayList<String> mGenres;
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

        if (savedInstanceState != null){
            mGameTitle = savedInstanceState.getString(TITLE_KEY);
            mGameDescription = savedInstanceState.getString(DESCRIPTION_KEY);
            mGameDevName = savedInstanceState.getString(DEV_KEY);
            mGameID = savedInstanceState.getString(ID_KEY);
            mGameNewPrice = savedInstanceState.getString(NEW_PRICE_KEY);
            mGameOldPrice = savedInstanceState.getString(OLD_PRICE_KEY);
            mImgURLs = savedInstanceState.getStringArrayList(IMG_LIST_KEY);
            mGenres = savedInstanceState.getStringArrayList(GENRE_LIST_KEY);
            currentIndex = savedInstanceState.getInt(CURRENT_INDEX_KEY);
            currentIndex--;
            fillViews();
        }else {
            currentIndex = 0;
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
                        for (int i = 0; i < imgArray.length(); i++) {
                            String img = imgArray.getJSONObject(i).getString("Url");
                            mImgURLs.add(img);
                            Log.d(TAG, "doInBackground: added img #" + mImgURLs.size() + "  url --- " + img);
                        }

                        JSONArray genreArray = game.getJSONArray("Genres");
                        for (int i = 0; i < genreArray.length(); i++) {
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
                    fillViews();
                }
            }.execute();
        }

        checkIndex(currentIndex,startIndex,endIndex);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentIndex >= endIndex){
                    //do nothing
                }else {
                    nextImage();
                    checkIndex(currentIndex,startIndex,endIndex);
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
                    checkIndex(currentIndex,startIndex,endIndex);
                }
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), FullscreenImageActivity.class);
                i.putExtra("url", mImgURLs.get(currentIndex));
                startActivity(i);
            }
        });
    }

    public void checkIndex(int currentIndex, int startIndex, int endIndex){
        if (currentIndex == startIndex){
            mPrevious.setAlpha(0.2f);
        }else if (currentIndex == endIndex){
            mNext.setAlpha(0.2f);
        }else if (currentIndex < endIndex && currentIndex > startIndex){
            mPrevious.setAlpha(1.0f);
            mNext.setAlpha(1.0f);
        }
    }

    public void fillViews(){
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(IMG_LIST_KEY, mImgURLs);
        outState.putStringArrayList(GENRE_LIST_KEY, mGenres);
        outState.putString(DESCRIPTION_KEY, mGameDescription);
        outState.putString(DEV_KEY, mGameDevName);
        outState.putString(ID_KEY, mGameID);
        outState.putString(TITLE_KEY, mGameTitle);
        outState.putString(NEW_PRICE_KEY, mGameNewPrice);
        outState.putString(OLD_PRICE_KEY, mGameOldPrice);
        outState.putInt(CURRENT_INDEX_KEY, currentIndex);
        super.onSaveInstanceState(outState);
    }

    public void fillImageView(List<String> list){
        startIndex = 0;
        endIndex = list.size()-1;
        Picasso.with(this).load(mImgURLs.get(currentIndex)).into(mImageView);
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
