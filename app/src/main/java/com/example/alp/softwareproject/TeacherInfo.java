package com.example.alp.softwareproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.TextView;

public class TeacherInfo extends AppCompatActivity {

    TextView tvTeacherName;
    String TeacherName;

    Button btnMonday;
    Button btnTuesday;
    Button btnWednesday;
    Button btnThursday;
    Button btnFriday;

    TextView Class1;
    TextView Class2;
    TextView Class3;
    TextView Class4;
    TextView Class5;
    TextView Class6;
    TextView Class7;
    TextView Class8;

    TextView Lecture1;
    TextView Lecture2;
    TextView Lecture3;
    TextView Lecture4;
    TextView Lecture5;
    TextView Lecture6;

    DatabaseHelper databaseHelper;
    String daySelected = "Pazartesi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);

        Bundle extras = getIntent().getExtras();
        tvTeacherName = findViewById(R.id.TeacherInfoTV);

        daySelected = "Perşembe";

        databaseHelper = new DatabaseHelper(TeacherInfo.this);

        btnMonday = findViewById(R.id.monday);
        btnTuesday = findViewById(R.id.tuesday);
        btnWednesday = findViewById(R.id.wednesday);
        btnThursday = findViewById(R.id.thursday);
        btnFriday = findViewById(R.id.friday);

        Class1 = findViewById(R.id.Class1);
        Class2 = findViewById(R.id.Class2);
        Class3 = findViewById(R.id.Class3);
        Class4 = findViewById(R.id.Class4);
        Class5 = findViewById(R.id.Class5);
        Class6 = findViewById(R.id.Class6);
        Class7 = findViewById(R.id.Class7);
        Class8 = findViewById(R.id.Class8);

        Lecture1 = findViewById(R.id.lecture1);
        Lecture2 = findViewById(R.id.lecture2);
        Lecture3 = findViewById(R.id.lecture3);
        Lecture4 = findViewById(R.id.lecture4);
        Lecture5 = findViewById(R.id.lecture5);
        Lecture6 = findViewById(R.id.lecture6);

        btnMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelected="Pazartesi";
                getSchedule(TeacherName,daySelected);
            }
        });

        btnTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelected="Salı";
                getSchedule(TeacherName,daySelected);
            }
        });

        btnWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelected="Çarşamba";
                getSchedule(TeacherName,daySelected);
            }
        });

        btnThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelected="Perşembe";
                getSchedule(TeacherName,daySelected);
            }
        });

        btnFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daySelected="Cuma";
                getSchedule(TeacherName,daySelected);
            }
        });


        TeacherName = extras.getString("TeacherName");
        tvTeacherName.setText(TeacherName);

        getSchedule(TeacherName,daySelected);
        getLectures(TeacherName);

    }

    void getLectures(String TeacherName){

        String Lectures[] = databaseHelper.getLectures(TeacherName);

        for(int i=0;i<Lectures.length;i++){
            if(i==1){
                Lecture1.setText(Lectures[0]);
            }
            else if(i==2){
                Lecture2.setText(Lectures[1]);
            }
            else if(i==3){
                Lecture3.setText(Lectures[2]);
            }
            else if(i==4){
                Lecture4.setText(Lectures[3]);
            }
            else if(i==5){
                Lecture5.setText(Lectures[4]);
            }
            else{
                Lecture6.setText(Lectures[5]);
            }
        }
    }

    void getSchedule(String TeacherName , String daySelected){
        for(int i=1;i<9;i++){

            String time = Integer.toString(i);
            String LectureNameTaken = databaseHelper.getLectureName(daySelected,time,TeacherName);

            if(i==1){
                Class1.setText(LectureNameTaken);
            }
            else if(i==2){
                Class2.setText(LectureNameTaken);
            }
            else if(i==3){
                Class3.setText(LectureNameTaken);
            }
            else if(i==4){
                Class4.setText(LectureNameTaken);
            }
            else if(i==5){
                Class5.setText(LectureNameTaken);
            }
            else if(i==6){
                Class6.setText(LectureNameTaken);
            }
            else if(i==7){
                Class7.setText(LectureNameTaken);
            }
            else{
                Class8.setText(LectureNameTaken);
            }
        }
    }
}
