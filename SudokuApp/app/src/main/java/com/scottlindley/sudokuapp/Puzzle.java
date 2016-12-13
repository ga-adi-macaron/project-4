package com.scottlindley.sudokuapp;

import java.util.List;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class Puzzle {
    private List<String> mKey;
    private String mDifficulty;
    
    public Puzzle(){

    }

    public Puzzle(List<String> key, String difficulty){
        mKey = key;
        mDifficulty = difficulty;
    }

    public List<String> getKey() {
        return mKey;
    }

    public void setKey(List<String> key) {
        mKey = key;
    }

    public String getDifficulty() {
        return mDifficulty;
    }

    public void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }
}
