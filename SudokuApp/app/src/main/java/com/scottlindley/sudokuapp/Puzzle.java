package com.scottlindley.sudokuapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class Puzzle {
    private static final String TAG = "Puzzle";
    private List<Integer> mKey;
    private String mDifficulty;
    
    public Puzzle(){

    }

    public Puzzle(List<Integer> key, String difficulty){
        mKey = key;
        mDifficulty = difficulty;
    }

    public List<Integer> getKey() {
        return mKey;
    }

    public void setKey(List<Integer> key) {
        mKey = key;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }

    /**
     * Local and remote storage works best when the data is in Json String form.
     * This method converts that String key into an integer array that is more convenient
     * for the puzzle activity.
     * @return
     */
    public int[] getKeyIntArray(){
        int[] arr = new int[81];
        for (int i=0; i<mKey.size(); i++){
            arr[i] = mKey.get(i);
        }
        return arr;
    }

    public JSONArray getKeyJSONArray(){
        try {
            return new JSONArray(getKeyIntArray());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
