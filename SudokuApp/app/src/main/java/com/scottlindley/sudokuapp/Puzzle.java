package com.scottlindley.sudokuapp;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class Puzzle {
    private JSONArray mKey;
    private String mDifficulty;
    
    public Puzzle(){

    }

    public Puzzle(JSONArray key, String difficulty){
        mKey = key;
        mDifficulty = difficulty;
    }

    public JSONArray getKey() {
        return mKey;
    }

    public void setKey(JSONArray key) {
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
        for (int i=0; i<mKey.length(); i++){
            try {
                arr[0] = Integer.parseInt(mKey.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arr;
    }
}
