package com.example.appselfstudy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

import java.net.URI;

public class MainActivity extends AppCompatActivity {
 private ImageButton btn_SignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_SignOut = findViewById(R.id.btnSignOut);
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Đăng xuất người dùng
                FirebaseAuth.getInstance().signOut();
                // Sau khi đăng xuất, chuyển người dùng đến màn hình đăng nhập hoặc màn hình chính khác
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
                finish(); // Đóng Activity hiện tại
            }
        });
    }
}