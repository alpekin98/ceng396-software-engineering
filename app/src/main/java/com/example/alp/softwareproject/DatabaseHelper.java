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

        if(count>0){
            return true;
        } else {
            return false;
        }
    }

    public String getTeacherName(String username,String password){

        db = openDatabase();

        String[] selectionArgs = {username,password};
        Cursor cursorTeacherName = db.rawQuery("SELECT Teacher_Name FROM Accounts WHERE Username=? AND Password=?",selectionArgs);

        int index;
        String TeacherName = new String();

        while(cursorTeacherName.moveToNext()) {
            index = cursorTeacherName.getColumnIndex("Teacher_Name");
            TeacherName = cursorTeacherName.getString(index);
        }
        return  TeacherName;
    }

    public String getLectureName(String LectureDay , String LectureTime , String TeacherName){

        db = openDatabase();
        String[] selectionArgs = {LectureDay,LectureTime,TeacherName};
        String LectureName ="---------------";

        Cursor cursorLectureName = db.rawQuery("SELECT Lecture_Name " +
                "FROM ((Schedule INNER JOIN Lectures ON Schedule.Sch_Lec_ID = Lectures.Lectures_ID) " +
                "INNER JOIN Accounts ON Schedule.Sch_Account_ID = Accounts.Accounts_ID) " +
                "WHERE Lecture_Day=? AND Lecture_Time=? AND Teacher_Name=? " +
                "GROUP BY Lecture_Time",selectionArgs);


        while(cursorLectureName.moveToNext()){
            int index = cursorLectureName.getColumnIndex("Lecture_Name");
            LectureName = cursorLectureName.getString(index);
        }

        Log.e("SELAMLARSELAMLARSELAMLAR" , LectureDay+" "+LectureTime+" "+TeacherName+" "+LectureName);

        return LectureName;
    }

    public String[] getLectures(String TeacherName){

        db = openDatabase();
        int i;

        String[] selectionArgs = {TeacherName};
        String[] Lectures = new String[6];

        for(i=0;i<6;i++)
            Lectures[i] = "----------";

        i=0;

        Cursor cursorLectures = db.rawQuery("SELECT Lecture_Name " +
                "FROM ((Schedule INNER JOIN Lectures ON Schedule.Sch_Lec_ID = Lectures.Lectures_ID) " +
                "INNER JOIN Accounts ON Schedule.Sch_Account_ID = Accounts.Accounts_ID) " +
                "WHERE Teacher_Name=? " +
                "GROUP BY Lecture_Name",selectionArgs);

        while(cursorLectures.moveToNext()){
            int index = cursorLectures.getColumnIndex("Lecture_Name");
            Lectures[i] = cursorLectures.getString(index);
            i++;
        }

        return Lectures;
    }
}
