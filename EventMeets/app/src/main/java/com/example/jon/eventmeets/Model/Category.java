package com.example.jon.eventmeets.Model;

/**
 * Created by Jon on 12/16/2016.
 */

public class Category {
    private String mType;
    private SubCategory[] mSubCategories;

    public Category(String type) {
        mType = type;
    }

    public Category(String type, SubCategory[] subs) {
        mType = type;
        mSubCategories = subs;
    }
}
