package com.example.alp.softwareproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {

    Button btnLogin;
    Button btnForgotPassword;
    EditText etEmail;
    EditText etPassword;
    String Username;
    String Password;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        databaseHelper = new DatabaseHelper(LoginScreen.this);

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInfoPage();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Username = etEmail.getText().toString();
                Password = etPassword.getText().toString();

                String TeacherName=null;

                boolean isExist = databaseHelper.checkUserExist(Username,Password);

                TeacherName = databaseHelper.getTeacherName(Username,Password);


                if(isExist){
                    Intent intent = new Intent(LoginScreen.this,MainMenu.class);
                    intent.putExtra("TeacherName" , TeacherName);
                    startActivity(intent);
                }else{
                    etEmail.setText(null);
                    etPassword.setText(null);
                    Toast.makeText(LoginScreen.this,"Login Failed. Invalid ID or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openInfoPage(){
        Intent intent = new Intent(this,InfoPage.class);
        startActivity(intent);
    }
}
