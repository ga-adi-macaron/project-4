package com.joelimyx.politicallocal.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Joe on 12/16/16.
 */

public class DBAssetHelper extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "politician.db";
    public static final int DATABASE_VERSION = 1;

    public DBAssetHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
