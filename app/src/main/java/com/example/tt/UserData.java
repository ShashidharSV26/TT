package com.example.tt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class UserData extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="UserDataBase";
    private static final int DATABASE_VERSION=9;
    private static final String TABLE_NAME="selection";
    private static final String ID="id";
    private static final String CATEGORY="category";
    private static final String LANGUAGE="language";
    private static final String VIDEO_TYPE="videoType";
    private static final String VIDEO_NAME="videoName";
    private static final String VIDEO_START_TIME="startTime";
    private static final String VIDEO_END_TIME="endTime";

    public UserData(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      db.execSQL("CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CATEGORY+" TEXT,"+LANGUAGE+" TEXT,"+VIDEO_TYPE+" TEXT,"+VIDEO_NAME+" TEXT,"+VIDEO_START_TIME+" TEXT,"+VIDEO_END_TIME+" TEXT "+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

        onCreate(db);
    }

    public void addData(String category,String language,String videoType,String videoName,String startTime,String endTime){

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CATEGORY,category);
        values.put(LANGUAGE,language);
        values.put(VIDEO_TYPE,videoType);
        values.put(VIDEO_NAME,videoName);
        values.put(VIDEO_START_TIME,startTime);
        values.put(VIDEO_END_TIME,endTime);
        Log.d("DataBase","insertion"+category+language);
        db.insert(TABLE_NAME,null,values);
    }
}
