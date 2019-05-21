package com.example.alp.softwareproject;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {


    Button btnTakeAttendance;
    Button btnTeacherInfo;
    Button btnAttendanceList;
    TextView tvTeacherName;
    String TeacherName;
    String TeacherName2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Bundle extras = getIntent().getExtras();
        TeacherName = extras.getString("TeacherName");
        if(extras.getString("TeacherName") == null) {
            TeacherName=extras.getString("TeacherName2");
        }

        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        btnTeacherInfo = findViewById(R.id.btnTeacherInfo);
        btnAttendanceList = findViewById(R.id.btnAttendanceList);

        tvTeacherName = findViewById(R.id.UserName);

        tvTeacherName.setText("Welcome " + TeacherName);

        btnTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTakeAttendance();
            }
        });

        btnTeacherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTeacherInfo();
            }
        });

        btnAttendanceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAttendanceList();
            }
        });

    }

    public void openTakeAttendance(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("TeacherName" , TeacherName);
        startActivity(intent);
    }

    public void openTeacherInfo(){
        Intent intent = new Intent(this,TeacherInfo.class);
        intent.putExtra("TeacherName" , TeacherName);
        startActivity(intent);
    }

    public void openAttendanceList(){
        Intent intent = new Intent(this,AttendanceList.class);
        startActivity(intent);
    }
}
