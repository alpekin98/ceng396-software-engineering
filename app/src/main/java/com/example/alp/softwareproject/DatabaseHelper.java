package com.example.alp.softwareproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Better Attendance.db";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_PATH = "/data/data/com.example.alp.softwareproject/databases/";

    private final String USER_TABLE_ACCOUNTS = "Accounts";

    private final Context context;
    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME , null , DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void createDB(){
        boolean dbExist = checkDbExist();

        if(!dbExist){
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDbExist(){
        SQLiteDatabase sqLiteDatabase = null;
        String path = DATABASE_PATH + DATABASE_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READONLY);

        if(sqLiteDatabase != null){
            sqLiteDatabase.close();
            return true;
        }
        return false;
    }

    private void copyDatabase(){
        try{
            InputStream inputStream = context.getAssets().open(DATABASE_NAME);
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] b = new byte[1024];
            int length;

            while ((length=inputStream.read(b))>0){
                outputStream.write(b,0,length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private SQLiteDatabase openDatabase(){
        String path = DATABASE_PATH + DATABASE_NAME;
        db = SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.OPEN_READWRITE);
        return db;
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public boolean checkUserExist(String username,String password){
        String[] columns = {"Username","Password"};
        db = openDatabase();
        String selection = "Username=? and Password=?";
        String[] selectionArgs = {username,password};

        Cursor cursor = db.query(USER_TABLE_ACCOUNTS,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();

        cursor.close();
        close();

        /**
        Log.e("LELELELELELELELELEL","(" + selectionArgs[0] + ")" + "SelectionArgs username");
        Log.e("LELELELELELELELELE","(" + selectionArgs[1] + ")" + "SelectionArgs Password");
        Log.e("LELELELELLELELELELE", Integer.toString(count));
        */

        if(count>0){
            return true;
        } else {
            return false;
        }
    }

    public String getTeacherName(String username,String password){

        db = openDatabase();
        String selection = "Username=? and Password=?";
        String TeacherName;
        String[] columns = {"Teacher_Name"};
        String[] selectionArgs = {username,password};
        Cursor cursor = db.query(USER_TABLE_ACCOUNTS,columns,selection,selectionArgs,null,null,null);
        Log.e("LELELELELLELELELELE", "cursor.getCount" + cursor.getCount());
        Log.e("LELELELELEEEEEEEEEEEEE", "cursor.getColumnIndex()" +cursor.getColumnIndex("Teacher_Name"));
        // cursor.getString(cursor.getColumnIndexOrThrow(  "Teacher_Name"));

        TeacherName = cursor.toString();

        cursor.close();
        close();
        return TeacherName;
    }
}
