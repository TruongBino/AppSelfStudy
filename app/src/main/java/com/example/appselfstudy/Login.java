package com.example.appselfstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Login extends AppCompatActivity {
    private LinearLayout layoutSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        initListener();
    }

    private void initListener() {
        layoutSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });
    }

    private void initUi(){
        layoutSignUp = findViewById(R.id.layout_signUp);
    }
}