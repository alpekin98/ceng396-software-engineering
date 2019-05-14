package com.example.alp.softwareproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.alp.softwareproject.ProjectContract.*;

public class RecyclerViewDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="Better Attendance.db";
    public static final int DATABASE_VERSION = 1;

    public RecyclerViewDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        return;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ProjectEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
