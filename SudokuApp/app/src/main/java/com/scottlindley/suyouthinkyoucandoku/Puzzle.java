package com.scottlindley.suyouthinkyoucandoku;

import java.util.List;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class Puzzle {
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
}
