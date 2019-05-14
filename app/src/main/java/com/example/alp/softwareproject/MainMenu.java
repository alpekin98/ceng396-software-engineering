package com.example.alp.softwareproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {


    Button btnTakeAttendance;
    Button btnTeacherInfo;
    Button btnAttendanceList;
    TextView tvTeacherName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnTakeAttendance = findViewById(R.id.btnTakeAttendance);
        btnTeacherInfo = findViewById(R.id.btnTeacherInfo);
        btnAttendanceList = findViewById(R.id.btnAttendanceList);

        tvTeacherName = findViewById(R.id.UserName);

        Bundle extras = getIntent().getExtras();
        String username = null;
        String TeacherName = null;
        if(extras!=null){
            username = extras.getString("Username");
            TeacherName = extras.getString("TeacherName");
            if(TeacherName.length()>20){
                tvTeacherName.setText("Welcome " + username);
            }else {
                tvTeacherName.setText("Welcome " + TeacherName);
            }

        }

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
        startActivity(intent);
    }

    public void openTeacherInfo(){
        Intent intent = new Intent(this,TeacherInfo.class);
        startActivity(intent);
    }

    public void openAttendanceList(){
        Intent intent = new Intent(this,AttendanceList.class);
        startActivity(intent);
    }

}
