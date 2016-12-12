package com.scottlindley.sudokuapp;

import java.util.List;

/**
 * Created by Scott Lindley on 12/11/2016.
 */

public class Puzzle {
    private List<Integer> mListKey;
    
    public Puzzle(){

    }

    public List<Integer> getListKey() {
        return mListKey;
    }

    public void setListKey(List<Integer> listKey) {
        mListKey = listKey;
    }
}
