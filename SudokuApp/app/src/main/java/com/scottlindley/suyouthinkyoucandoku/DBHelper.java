package com.scottlindley.suyouthinkyoucandoku;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott Lindley on 12/12/2016.
 */

public class DBHelper extends SQLiteOpenHelper{
    private static final String TAG = "DBHelper";
    public static final String DATABASE_NAME = DBAssetHelper.DATA_BASE_NAME;
    public static final int VERSION_NUMBER = 1;

    public static final String PUZZLE_TABLE = "Puzzle";
    public static final String STATS_TABLE = "Stats";

    public static final String COL_KEY = "key";
    public static final String COL_DIFFICULTY = "difficulty";
    public static final String COL_HIGHSCORE = "highscore";
    public static final String COL_BEST_TIME = "besttime";
    public static final String COL_RACES_WON = "raceswon";
    public static final String COL_RACES_LOST = "raceslost";

    public static final String CREATE_PUZZLE_TABLE =
            "CREATE TABLE "+PUZZLE_TABLE+" ("+
                    COL_KEY+" TEXT, "+
                    COL_DIFFICULTY+" TEXT)";
    public static final String CREATE_STATS_TABLE =
            "CREATE TABLE "+STATS_TABLE+" ("+
                    COL_HIGHSCORE+" INTEGER, "+
                    COL_RACES_WON+" INTEGER, "+
                    COL_RACES_LOST+" INTEGER, "+
                    COL_BEST_TIME+" INTEGER)";

    private static DBHelper sInstance;
    private Context mContext;

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        mContext = context;
    }

    public static DBHelper getInstance(Context context){
        if (sInstance == null){
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_PUZZLE_TABLE);
        sqLiteDatabase.execSQL(CREATE_STATS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+PUZZLE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+STATS_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    /**
     * Listens for a broadcasted intent from the Puzzle Refresh Service
     * Once received, it first look to see how many puzzles have been sent.
     * The method then loops through the puzzles and creates a new puzzle object
     * for each iteration of the loop.
     * The puzzles are then sent off to the {@link #replacePuzzles(List)} method.
     */
    public void setUpBroadcastReceiver(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PUZZLE_TABLE, null, null);
        db.close();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String difficulty = intent.getStringExtra(PuzzleRefreshService.DIFFICULTIES_INTENT_KEY);
                try {
                    JSONArray jarray =
                            new JSONArray(intent.getStringExtra(PuzzleRefreshService.KEYS_INTENT_KEY));
                    ArrayList<Integer> intKey = new ArrayList<>();
                    for (int i=0; i<jarray.length(); i++){
                        intKey.add(jarray.getInt(i));
                    }
//                    removePuzzle(difficulty);
                    addPuzzle(new Puzzle(
                            intKey,
                            difficulty));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(receiver, new IntentFilter(PuzzleRefreshService.PUZZLE_REFRESH_SERVICE));
    }

    /**
     * @return returns a list of all puzzles stored locally
     */
    public List<Puzzle> getAllPuzzles(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null, null, null, null, null, null);
        List<Puzzle> puzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        return puzzles;
    }

    /**
     * @return returns all easy puzzles
     */
    public List<Puzzle> getAllEasyPuzzles(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"easy"}, null, null, null);
        List<Puzzle> easyPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        return easyPuzzles;
    }

    /**
     * @return randomly returns one easy puzzle
     */
    public Puzzle getEasyPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"easy"}, null, null, null);
        List<Puzzle> easyPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        int randomIndex = (int) (Math.random()*(easyPuzzles.size()));
        if(easyPuzzles!=null && !easyPuzzles.isEmpty()) {
            return easyPuzzles.get(randomIndex);
        }
        return null;
    }

    /**
     * @return returns all medium puzzles
     */
    public List<Puzzle> getAllMediumPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"medium"}, null, null, null);
        List<Puzzle> mediumPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        return mediumPuzzles;
    }

    /**
     * @return randomly returns one medium puzzle
     */
    public Puzzle getMediumPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"medium"}, null, null, null);
        List<Puzzle> mediumPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        int randomIndex = (int) (Math.random()*(mediumPuzzles.size()-1));
        if(mediumPuzzles!=null && !mediumPuzzles.isEmpty()) {
            return mediumPuzzles.get(randomIndex);
        }
        return null;
    }

    /**
     * @return returns all hard puzzles
     */
    public List<Puzzle> getAllHardPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"hard"}, null, null, null);
        List<Puzzle> hardPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        return hardPuzzles;
    }

    /**
     * @return randomly returns one hard puzzle
     */
    public Puzzle getHardPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"hard"}, null, null, null);
        List<Puzzle> hardPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        int randomIndex = (int) (Math.random()*(hardPuzzles.size()));
        if (hardPuzzles!=null && !hardPuzzles.isEmpty()) {
            return hardPuzzles.get(randomIndex);
        }
        return null;
    }

    /**
     * @return returns all expert puzzles
     */
    public List<Puzzle> getALLExpertPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"%expert%"}, null, null, null);
        List<Puzzle> expertPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        return expertPuzzles;
    }

    /**
     * @return randomly returns one expert puzzle
     */
    public Puzzle getExpertPuzzle(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                PUZZLE_TABLE, null,
                COL_DIFFICULTY+" LIKE ?", new String[]{"expert"}, null, null, null);
        List<Puzzle> expertPuzzles = getPuzzlesOutOfCursor(cursor);
        cursor.close();
        int randomIndex = (int) (Math.random()*(expertPuzzles.size()));
        if (expertPuzzles!=null && !expertPuzzles.isEmpty()) {
            return expertPuzzles.get(randomIndex);
        }
        return null;
    }

    /**
     * @return returns the single row of stats
     */
    public Stats getStats(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(
                STATS_TABLE, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            Stats stats = new Stats(
                cursor.getInt(cursor.getColumnIndex(COL_HIGHSCORE)),
                cursor.getInt(cursor.getColumnIndex(COL_RACES_WON)),
                cursor.getInt(cursor.getColumnIndex(COL_RACES_LOST)),
                cursor.getInt(cursor.getColumnIndex(COL_BEST_TIME)));
            cursor.close();
            return stats;
        }
        cursor.close();
        return null;
    }



    public void replacePuzzles(List<Puzzle> puzzles) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PUZZLE_TABLE, null, null);
        for (Puzzle puzzle: puzzles) {
            ContentValues values = new ContentValues();
            values.put(COL_DIFFICULTY, puzzle.getDifficulty());
            JSONArray keyArr = puzzle.getKeyJSONArray();
            values.put(COL_KEY, keyArr.toString());
            db.insert(PUZZLE_TABLE, null, values);
        }
        db.close();
    }

    public void addPuzzle(Puzzle puzzle){
        Log.d(TAG, "addPuzzle: ");
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DIFFICULTY, puzzle.getDifficulty());
        JSONArray keyArr = puzzle.getKeyJSONArray();
        values.put(COL_KEY, keyArr.toString());
        db.insert(PUZZLE_TABLE, null, values);
        db.close();
    }

    public void removePuzzle(String difficulty){
        SQLiteDatabase db = getWritableDatabase();
        Puzzle puzzle;
        switch (difficulty){
            case "easy":
                puzzle = getEasyPuzzle();
                break;
            case "medium":
                puzzle = getMediumPuzzle();
                break;
            case "hard":
                puzzle = getHardPuzzle();
                break;
            case "expert":
                puzzle = getExpertPuzzle();
                break;
            default:
                puzzle = null;
        }
        if(puzzle!=null) {
            db.delete(
                    PUZZLE_TABLE,
                    COL_KEY + " = ?",
                    new String[]{puzzle.getKeyJSONArray().toString()});
        }
    }

    public void updateHighScore(int highscore){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_HIGHSCORE, highscore);
        db.update(
                STATS_TABLE, values, null, null);
        db.close();
    }

    public void updateRacesWon(int racesWon){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RACES_WON, racesWon);
        db.update(
                STATS_TABLE, values, null, null);
        db.close();
    }

    public void updateRacesLost(int racesLost){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_RACES_LOST, racesLost);
        db.update(
                STATS_TABLE, values, null, null);
        db.close();
    }

    public void updateBestTime(int bestTime){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BEST_TIME, bestTime);
        db.update(
                STATS_TABLE, values, null, null);
        db.close();
    }

    /**
     * A helper method to consolidate code in the above 'get' methods
     *
     * @param cursor
     * @return a list of puzzles with the data stored in the given cursor
     */

    public List<Puzzle> getPuzzlesOutOfCursor(Cursor cursor) {
        if (cursor.moveToFirst()) {
            List<Puzzle> puzzles = new ArrayList<>();
            while (!cursor.isAfterLast()) {
                try {
                    JSONArray jsonArray =
                            new JSONArray(cursor.getString(cursor.getColumnIndex(COL_KEY)));
                    List<Integer> intKey = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        intKey.add(jsonArray.getInt(i));
                    }
                    puzzles.add(new Puzzle(
                            intKey,
                            cursor.getString(cursor.getColumnIndex(COL_DIFFICULTY))
                    ));
                    cursor.moveToNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return puzzles;
        }
        return null;
    }
}
