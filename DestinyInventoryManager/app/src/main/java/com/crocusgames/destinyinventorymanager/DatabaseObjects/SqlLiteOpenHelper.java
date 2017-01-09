package com.crocusgames.destinyinventorymanager.DatabaseObjects;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.crocusgames.destinyinventorymanager.ItemPreDefinitionObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Serkan on 20/12/16.
 */

public class SqlLiteOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "destinydb";
    public static final String ITEM_DEFINITIONS_TABLE_NAME = "DestinyInventoryItemDefinition";
    public static final String ITEM_BUCKET_DEFINITIONS_TABLE_NAME = "DestinyInventoryBucketDefinition";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_JSON = "json";

    public SqlLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static SqlLiteOpenHelper sInstance;

    public static SqlLiteOpenHelper getInstance(Context context) {
        if (sInstance == null)
            sInstance = new SqlLiteOpenHelper(context.getApplicationContext());
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //
    }

    public JSONObject getItemDefinitionObject(Long itemHash) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_JSON + " FROM " + ITEM_DEFINITIONS_TABLE_NAME + " WHERE "
                + COLUMN_ID + " & 0xFFFFFFFF " + " = " + itemHash;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
            JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
            cursor.close();
            return itemObjectDefinitionJson;
        } else {
            cursor.close();
            return null;
        }
    }

    public JSONObject getBucketDefinitionObject(Long bucketHash) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_JSON + " FROM " + ITEM_BUCKET_DEFINITIONS_TABLE_NAME + " WHERE "
                + COLUMN_ID + " & 0xFFFFFFFF " + " = " + bucketHash;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
            JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
            cursor.close();
            return itemObjectDefinitionJson;
        } else {
            cursor.close();
            return null;
        }
    }

    public List<JSONObject> getItemDefinitionObjectByList(List<ItemPreDefinitionObject> list) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        List<JSONObject> requestedObjects = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String itemHash = list.get(i).getItemHash();

            String query = "SELECT " + COLUMN_JSON + " FROM " + ITEM_DEFINITIONS_TABLE_NAME + " WHERE "
                    + COLUMN_ID + " & 0xFFFFFFFF " + " = " + itemHash;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
                JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
                requestedObjects.add(itemObjectDefinitionJson);
                cursor.close();
            } else {
                cursor.close();
                return null;
            }
        }
        return requestedObjects;
    }

    public List<JSONObject> getItemBucketDefinitionObjectByList(List<ItemPreDefinitionObject> list) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();
        List<JSONObject> requestedObjects = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String bucketHash = list.get(i).getBucketHash();

            String query = "SELECT " + COLUMN_JSON + " FROM " + ITEM_BUCKET_DEFINITIONS_TABLE_NAME + " WHERE "
                    + COLUMN_ID + " & 0xFFFFFFFF " + " = " + bucketHash;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
                JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
                requestedObjects.add(itemObjectDefinitionJson);
                cursor.close();
            } else {
                cursor.close();
                return null;
            }
        }
        return requestedObjects;
    }

    public HashMap<Long, JSONObject> getItemDefinitionObjectAllatOnce(String itemHashesCombined) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<Long, JSONObject> requestedObjects = new HashMap<>();

//        String query = "SELECT " + COLUMN_JSON + "," + COLUMN_ID + " FROM " + ITEM_DEFINITIONS_TABLE_NAME + " WHERE "
//                + COLUMN_ID + " & 0xFFFFFFFF" + " IN (" + itemHashesCombined + ")";

//        String query = "SELECT CASE WHEN " + COLUMN_ID + "<0 THEN " + COLUMN_ID +
//                "+4294967296 ELSE " + COLUMN_ID + " END AS " + COLUMN_ID +", " + COLUMN_JSON +
//                " FROM " + ITEM_DEFINITIONS_TABLE_NAME + " WHERE "
//                + COLUMN_ID + " IN (" + itemHashesCombined + ")";

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_JSON + " FROM " + ITEM_DEFINITIONS_TABLE_NAME +
                " WHERE " + COLUMN_ID + " IN (" + itemHashesCombined + ")";

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
                    JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
                    int itemHashInt = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    Long itemHashLong = getUnsignedInt(itemHashInt);
                    requestedObjects.put(itemHashLong, itemObjectDefinitionJson);
                    cursor.moveToNext();
                }
                cursor.close();
            } else {
                cursor.close();
                return null;
            }
        return requestedObjects;
    }

    public HashMap<Long, JSONObject> getBucketDefinitionObjectAllatOnce(String bucketHashesCombined) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<Long, JSONObject> requestedObjects = new HashMap<>();

//        String query = "SELECT " + COLUMN_JSON +"," + COLUMN_ID + " FROM " + ITEM_BUCKET_DEFINITIONS_TABLE_NAME + " WHERE "
//                + COLUMN_ID + " & 0xFFFFFFFF" + " IN (" + bucketHashesCombined + ")";

//        String query = "SELECT CASE WHEN " + COLUMN_ID + "<0 THEN " + COLUMN_ID +
//                "+4294967296 ELSE " + COLUMN_ID + " END AS " + COLUMN_ID +", " + COLUMN_JSON +
//                " FROM " + ITEM_BUCKET_DEFINITIONS_TABLE_NAME + " WHERE "
//                + COLUMN_ID + " IN (" + bucketHashesCombined + ")";

        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_JSON + " FROM " + ITEM_BUCKET_DEFINITIONS_TABLE_NAME +
                " WHERE " + COLUMN_ID + " IN (" + bucketHashesCombined + ")";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                byte[] itemObjectDefinitionByte = cursor.getBlob(cursor.getColumnIndex(COLUMN_JSON));
                JSONObject itemObjectDefinitionJson = new JSONObject(new String(itemObjectDefinitionByte));
                int bucketHashInt = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                Long bucketHashLong = getUnsignedInt(bucketHashInt);
                requestedObjects.put(bucketHashLong, itemObjectDefinitionJson);
                cursor.moveToNext();
            }
            cursor.close();
        } else {
            cursor.close();
            return null;
        }
        return requestedObjects;
    }

    public static long getUnsignedInt(int x) {
        return x & 0x00000000ffffffffL;
    }
}
