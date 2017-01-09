package com.crocusgames.destinyinventorymanager.DatabaseObjects;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Serkan on 20/12/16.
 */

public class AssetHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "destinydb";
    private static final int DATABASE_VERSION = 2;

    public AssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
