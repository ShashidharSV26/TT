package com.example.tt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class StartFrom extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="StartFromDataBase";
    private static final int DATABASE_VERSION=4;
    private static final String TABLE_NAME="videostoped";
    private static final String ID="id";
    private static final String VIDEO_NAME="movieName";
    private static final String DURATION="duration";

    public StartFrom(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+VIDEO_NAME+" TEXT,"+DURATION+" TEXT "+")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    public void addDuration(String videoName,String duration){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(VIDEO_NAME,videoName);
        values.put(DURATION,duration);
        db.insert(TABLE_NAME,null,values);
    }

    public String getDuration(String videoName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DURATION + " FROM " + TABLE_NAME + " WHERE " + VIDEO_NAME + "=?", new String[]{videoName});

        String duration = null;
        if (cursor.moveToFirst()) {
            duration = cursor.getString(cursor.getColumnIndex(DURATION));
        }

        cursor.close();
        return duration;
    }

    public String isMovieNamePresent(String movieName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + VIDEO_NAME + "=?", new String[]{movieName});

        boolean isPresent = cursor.moveToFirst();
        cursor.close();

        return isPresent ? "yes" : "no";
    }

    public void saveOrUpdateDuration(String videoName, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(VIDEO_NAME, videoName);
        values.put(DURATION, duration);

        int rowsAffected = db.update(TABLE_NAME, values, VIDEO_NAME + "=?", new String[]{videoName});

        if (rowsAffected == 0) {
            db.insert(TABLE_NAME, null, values);
        }
    }


    private MutableLiveData<Long> durationLiveData = new MutableLiveData<>();

    public LiveData<Long> getDurationLiveData(String videoName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT duration FROM " + TABLE_NAME + " WHERE " + VIDEO_NAME + "=?", new String[]{videoName});

        // Create a Runnable to handle the query result
        Runnable queryRunnable = new Runnable() {
            @Override
            public void run() {
                long durationMS;
                // Check if the cursor has data and move to the first row
                if (cursor.moveToFirst()) {
                    String duration = cursor.getString(cursor.getColumnIndex(DURATION));
                    durationMS = Long.parseLong(duration);
                } else {
                    // Set a default value (e.g., 0) if the data is not found
                    durationMS = 0;
                }
                cursor.close();

                // Update the LiveData with the retrieved duration
                durationLiveData.postValue(durationMS);
            }
        };

        // Execute the Runnable in a separate thread (you can use AsyncTask or ThreadPoolExecutor as well)
        new Thread(queryRunnable).start();

        // Return the LiveData to observe the duration result
        return durationLiveData;
    }



}
