package com.joelimyx.politicallocal.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Joe on 12/16/16.
 */

public class RepsSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "politician.db";
    public static final String REPS_TABLE_NAME = "reps_table";
    public static final int DATABASE_VERSION = 1;

    public static final String COL_BIO_ID = "bio_id";
    public static final String COL_C_ID = "c_id";
    public static final String COL_NAME= "name";
    public static final String COL_PARTY= "party";
    public static final String COL_PHONE= "phone_#";
    public static final String COL_POSITION = "position";
    public static final String COL_DISTRICT_CLASS = "district_class";

    public static final String[] BASIC_COLUMNS = {COL_NAME,COL_PARTY};
    public static final String CREATE_TABLE =
            "CREATE TABLE "+ REPS_TABLE_NAME+"("+
                    COL_BIO_ID+" INTEGER PRIMARY KEY, "+
                    COL_C_ID+" INTEGER, "+
                    COL_NAME+" TEXT, "+
                    COL_PARTY+" TEXT, "+
                    COL_PHONE+" TEXT, "+
                    COL_POSITION+" TEXT, "+
                    COL_DISTRICT_CLASS+" INTEGER)";

    private static RepsSQLHelper sInstance;

    public static RepsSQLHelper getInstance(Context context){
        if (sInstance ==null){
            sInstance = new RepsSQLHelper(context);
        }
        return sInstance;
    }

    private RepsSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + REPS_TABLE_NAME);
        this.onCreate(db);
    }
}
