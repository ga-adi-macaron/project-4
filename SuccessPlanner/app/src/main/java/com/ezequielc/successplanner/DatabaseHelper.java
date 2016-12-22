package com.ezequielc.successplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ezequielc.successplanner.models.Affirmation;
import com.ezequielc.successplanner.models.DailyData;
import com.ezequielc.successplanner.models.Goal;
import com.ezequielc.successplanner.models.Schedule;

/**
 * Created by student on 12/21/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "successplanner.db";

    public static final String DAILY_DATA_TABLE = "daily_data_list";
    public static final String COL_ID = "id";
    public static final String COL_DATE = "date";

    public static final String GOALS_TABLE = "goals_list";
    public static final String COL_GOALS = "goals";

    public static final String AFFIRMATIONS_TABLE = "affirmations_list";
    public static final String COL_AFFIRMATIONS = "affirmations";

    public static final String SCHEDULE_TABLE = "schedule_list";
    public static final String COL_SCHEDULE = "schedule";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static DatabaseHelper mInstance;

    private static DatabaseHelper getInstance(Context context){
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context.getApplicationContext());
        } return mInstance;
    }

    public static final String CREATE_DAILY_DATA_TABLE =
            "CREATE TABLE " + DAILY_DATA_TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DATE + " TEXT)";

    public static final String CREATE_GOALS_TABLE =
            "CREATE TABLE " + GOALS_TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DATE + " TEXT, " +
                    COL_GOALS + " TEXT)";

    public static final String CREATE_AFFIRMATIONS_TABLE =
            "CREATE TABLE " + AFFIRMATIONS_TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DATE + " TEXT, " +
                    COL_AFFIRMATIONS + " TEXT)";

    public static final String CREATE_SCHEDULE_TABLE =
            "CREATE TABLE " + SCHEDULE_TABLE + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_DATE + " TEXT, " +
                    COL_SCHEDULE + " TEXT)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DAILY_DATA_TABLE);
        db.execSQL(CREATE_GOALS_TABLE);
        db.execSQL(CREATE_AFFIRMATIONS_TABLE);
        db.execSQL(CREATE_SCHEDULE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DAILY_DATA_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GOALS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + AFFIRMATIONS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
        onCreate(db);
    }

    public void insertGoals(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, goal.getDate());
        values.put(COL_GOALS, goal.getGoal());
        long id = db.insert(GOALS_TABLE, null, values);
        goal.setID(id);
        db.close();
    }

    public void insertAffirmations(Affirmation affirmation){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, affirmation.getDate());
        values.put(COL_AFFIRMATIONS,affirmation.getAffirmation());
        long id = db.insert(AFFIRMATIONS_TABLE, null, values);
        affirmation.setID(id);
        db.close();
    }

    public void insertSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATE, schedule.getDate());
        values.put(COL_SCHEDULE,schedule.getSchedule());
        long id = db.insert(SCHEDULE_TABLE, null, values);
        schedule.setID(id);
        db.close();
    }
}
