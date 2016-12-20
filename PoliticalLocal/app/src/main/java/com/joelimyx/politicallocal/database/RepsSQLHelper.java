package com.joelimyx.politicallocal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.joelimyx.politicallocal.reps.MyReps;
import com.joelimyx.politicallocal.reps.gson.congress.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joe on 12/16/16.
 */

public class RepsSQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "politician.db";
    private static final String REPS_TABLE_NAME = "reps_table";
    private static final int DATABASE_VERSION = 6;

    private static final String COL_BIO_ID = "bio_id";
    private static final String COL_C_ID = "c_id";
    private static final String COL_NAME= "name";
    private static final String COL_PARTY= "party";
    private static final String COL_PHONE= "phone";
    private static final String COL_EMAIL = "email";
    private static final String COL_WEBSITE = "website";
    private static final String COL_TWITTER = "twitter";
    private static final String COL_CHAMBER = "chamber";
    private static final String COL_DISTRICT_CLASS = "district_class";
    public static final String COL_FILE_NAME = "file_name";

    private static final String[] BASIC_COLUMNS = {COL_NAME,COL_PARTY};
    private static final String CREATE_TABLE =
            "CREATE TABLE "+ REPS_TABLE_NAME+"("+
                    COL_BIO_ID+" TEXT PRIMARY KEY, "+
                    COL_C_ID+" TEXT, "+
                    COL_NAME+" TEXT, "+
                    COL_PARTY+" TEXT, "+
                    COL_PHONE+" TEXT, "+
                    COL_EMAIL+" TEXT, "+
                    COL_WEBSITE+" TEXT, "+
                    COL_TWITTER+" TEXT, "+
                    COL_CHAMBER +" TEXT, "+
                    COL_FILE_NAME+" TEXT, "+
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

    public void addRep(Result legislator, String fileName){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_BIO_ID,legislator.getBioguideId());
        values.put(COL_C_ID,legislator.getCrpId());

        String name = legislator.getFirstName();
        //Add Middle name
        if (legislator.getMiddleName()!=null){
            name+=" "+legislator.getMiddleName();
        }
        values.put(COL_NAME,name+" "+legislator.getLastName());
        values.put(COL_PARTY,legislator.getParty());
        values.put(COL_PHONE,legislator.getPhone());
        values.put(COL_EMAIL,legislator.getOcEmail());
        values.put(COL_WEBSITE,legislator.getWebsite());
        values.put(COL_TWITTER,legislator.getTwitterId());

        //Add district or class
        if (legislator.getDistrict()==null) {
            values.put(COL_DISTRICT_CLASS, legislator.getSenateClass());
        }else{
            values.put(COL_DISTRICT_CLASS, (Double) legislator.getDistrict());
        }

        values.put(COL_CHAMBER,legislator.getChamber());
        values.put(COL_FILE_NAME, fileName+".jpg");

        db.insert(REPS_TABLE_NAME,null,values);
        db.close();
    }

    public List<MyReps> getRepsList(){
        SQLiteDatabase db = getReadableDatabase();
        List<MyReps> myRepsList = new ArrayList<>();
        Cursor cursor = db.query(REPS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COL_CHAMBER);
        if (cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                myRepsList.add(new MyReps(
                        cursor.getString(cursor.getColumnIndex(COL_BIO_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_C_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_PARTY)),
                        cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COL_WEBSITE)),
                        cursor.getString(cursor.getColumnIndex(COL_TWITTER)),
                        cursor.getString(cursor.getColumnIndex(COL_CHAMBER)),
                        cursor.getInt(cursor.getColumnIndex(COL_DISTRICT_CLASS)),
                        cursor.getString(cursor.getColumnIndex(COL_FILE_NAME))
                        ));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return myRepsList;
    }
    public MyReps getMyRepByID(String id){
        SQLiteDatabase db = getReadableDatabase();
        MyReps myReps = null;
        Cursor cursor = db.query(REPS_TABLE_NAME,
                null,
                COL_BIO_ID+" LIKE ?",
                new String[]{id},
                null,
                null,
                null,
                "1");
        if (cursor.moveToFirst()){
            myReps = new MyReps(
                    cursor.getString(cursor.getColumnIndex(COL_BIO_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_C_ID)),
                    cursor.getString(cursor.getColumnIndex(COL_NAME)),
                    cursor.getString(cursor.getColumnIndex(COL_PARTY)),
                    cursor.getString(cursor.getColumnIndex(COL_PHONE)),
                    cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(COL_WEBSITE)),
                    cursor.getString(cursor.getColumnIndex(COL_TWITTER)),
                    cursor.getString(cursor.getColumnIndex(COL_CHAMBER)),
                    cursor.getInt(cursor.getColumnIndex(COL_DISTRICT_CLASS)),
                    cursor.getString(cursor.getColumnIndex(COL_FILE_NAME)));
        }
        cursor.close();
        return myReps;
    }
}
