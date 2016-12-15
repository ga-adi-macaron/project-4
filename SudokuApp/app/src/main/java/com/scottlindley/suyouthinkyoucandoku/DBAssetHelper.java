package com.scottlindley.suyouthinkyoucandoku;

import android.content.Context;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class DBAssetHelper extends SQLiteAssetHelper{
    public static final String DATA_BASE_NAME = "SudokuDB";
    public static final int VERSION_NUMBER = 1;

    public DBAssetHelper(Context context) {
        super(context,DATA_BASE_NAME, null,VERSION_NUMBER);
    }
}
