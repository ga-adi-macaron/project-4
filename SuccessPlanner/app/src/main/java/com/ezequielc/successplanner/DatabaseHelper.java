package com.ezequielc.successplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ezequielc.successplanner.models.Affirmation;
import com.ezequielc.successplanner.models.Goal;
import com.ezequielc.successplanner.models.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 12/21/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "successplanner.db";

    // TODO: REMOVE THIS TABLE
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

    public static DatabaseHelper getInstance(Context context){
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

    public List<Goal> getGoalsForDate(String currentDate){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{COL_ID, COL_DATE, COL_GOALS};
        String selection = COL_DATE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(currentDate)};

        Cursor cursor = db.query(GOALS_TABLE, columns, selection, selectionArgs, null, null, null, null);

        List<Goal> goalList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                String goal = cursor.getString(cursor.getColumnIndex(COL_GOALS));

                Goal goals = new Goal(id, date, goal);
                goalList.add(goals);

                cursor.moveToNext();
            }
        }
        return goalList;
    }

    public List<Affirmation> getAffirmationsForDate(String currentDate){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{COL_ID, COL_DATE, COL_AFFIRMATIONS};
        String selection = COL_DATE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(currentDate)};

        Cursor cursor = db.query(AFFIRMATIONS_TABLE, columns, selection, selectionArgs, null, null, null, null);

        List<Affirmation> affirmationList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                String affirmation = cursor.getString(cursor.getColumnIndex(COL_AFFIRMATIONS));

                Affirmation affirmations = new Affirmation(id, date, affirmation);
                affirmationList.add(affirmations);

                cursor.moveToNext();
            }
        }
        return affirmationList;
    }

    public List<Schedule> getScheduleForDate(String currentDate){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = new String[]{COL_ID, COL_DATE, COL_SCHEDULE};
        String selection = COL_DATE + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(currentDate)};

        Cursor cursor = db.query(SCHEDULE_TABLE, columns, selection, selectionArgs, null, null, null, null);

        List<Schedule> scheduleList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
                String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                String schedule = cursor.getString(cursor.getColumnIndex(COL_SCHEDULE));

                Schedule schedules = new Schedule(id, date, schedule);
                scheduleList.add(schedules);

                cursor.moveToNext();
            }
        }
        return scheduleList;
    }

    public void deleteGoal(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(goal.getID())};
        db.delete(GOALS_TABLE, selection, selectionArgs);
        db.close();
    }

    public void deleteAffirmations(Affirmation affirmation){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(affirmation.getID())};
        db.delete(AFFIRMATIONS_TABLE, selection, selectionArgs);
        db.close();
    }

    public void deleteSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(schedule.getID())};
        db.delete(SCHEDULE_TABLE, selection, selectionArgs);
        db.close();
    }

    public void updateGoal(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(goal.getID())};
        ContentValues values = new ContentValues();
        values.put(COL_GOALS, goal.getGoal());
        db.update(GOALS_TABLE, values, selection, selectionArgs);
        db.close();
    }

    public void updateAffirmation(Affirmation affirmation){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(affirmation.getID())};
        ContentValues values = new ContentValues();
        values.put(COL_AFFIRMATIONS, affirmation.getAffirmation());
        db.update(AFFIRMATIONS_TABLE, values, selection, selectionArgs);
        db.close();
    }

    public void updateSchedule(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();
        String selection = COL_ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(schedule.getID())};
        ContentValues values = new ContentValues();
        values.put(COL_SCHEDULE, schedule.getSchedule());
        db.update(SCHEDULE_TABLE, values, selection, selectionArgs);
        db.close();
    }
}
