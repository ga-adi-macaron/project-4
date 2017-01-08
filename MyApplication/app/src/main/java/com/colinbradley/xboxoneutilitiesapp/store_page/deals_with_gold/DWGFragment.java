package com.colinbradley.xboxoneutilitiesapp.store_page.deals_with_gold;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colinbradley.xboxoneutilitiesapp.MainActivity;
import com.colinbradley.xboxoneutilitiesapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by colinbradley on 1/3/17.
 */

public class DWGFragment extends Fragment implements DWGAdapter.OnItemSelectedListener {
    public static final String TAG = "DWGFragment";
    public static final String DWG_LIST_KEY = "dwg";

    DWGAdapter mAdapter;
    RecyclerView mRV;
    ArrayList<DealWithGold> mDWGList;
    AsyncTask<Void,Void,Void> mTask;
    ProgressBar mProgressBar;
    TextView mText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDWGList = new ArrayList<>();
        View v = inflater.inflate(R.layout.fragment_deals_with_gold, container, false);

        mProgressBar = (ProgressBar)v.findViewById(R.id.dwg_progressbar);
        mText = (TextView)v.findViewById(R.id.dwg_text);

        if (savedInstanceState != null){
            mDWGList = savedInstanceState.getParcelableArrayList(DWG_LIST_KEY);
            mProgressBar.setVisibility(View.INVISIBLE);
        }else {
            mTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    OkHttpClient client = new OkHttpClient();
                    Log.d(TAG, "doInBackground: client made");

                    Request request = new Request.Builder()
                            .headers(MainActivity.mHeaders)
                            .url("https://xboxapi.com/v2/xboxone-gold-lounge")
                            .build();

                    try {
                        Response response = client.newCall(request).execute();

                        JSONObject responseObj = new JSONObject(response.body().string());

                        JSONArray dwgList = responseObj.getJSONArray("DealsWithGold");

                        for (int i = 0; i < dwgList.length(); i++) {
                            String title = dwgList.getJSONObject(i).getString("TitleName");
                            String originalPrice = dwgList.getJSONObject(i).getString("ListPriceText");
                            String newPrice = dwgList.getJSONObject(i).getString("DiscountedPriceText");
                            String imgURL = dwgList.getJSONObject(i).getString("TitleImage");
                            String gameID = dwgList.getJSONObject(i).getString("ID");

                            String ogPrice = "$" + originalPrice.substring(1);
                            String nPrice = "$" + newPrice.substring(1);

                            DealWithGold game = new DealWithGold(title, ogPrice, imgURL, gameID, nPrice);
                            mDWGList.add(game);
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    mAdapter.notifyDataSetChanged();
                    if (mDWGList.size() == 0){
                        mText.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }.execute();
        }

        mRV = (RecyclerView)v.findViewById(R.id.dwg_recyclerview);
        mRV.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new DWGAdapter(mDWGList, DWGFragment.this, getContext());
        mRV.setAdapter(mAdapter);
        return v;
    }

    public void nothingOnSale(){
        mText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(DWG_LIST_KEY, mDWGList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(String id, String oldp, String newp) {
        Intent intent = new Intent(getContext(), DWGDetails.class);
        intent.putExtra("id", id);
        intent.putExtra("oldp", oldp);
        intent.putExtra("newp", newp);
        startActivity(intent);
    }
}
