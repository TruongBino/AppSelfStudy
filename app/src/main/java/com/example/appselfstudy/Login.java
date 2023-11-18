package com.example.appselfstudy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private LinearLayout layoutSignUp;
    private TextView textViewSignUp;

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textViewSignUp = findViewById(R.id.tvSignUp);

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

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
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
    }

    private void onClickLogin() {
        String strEmail = edtEmail.getText().toString().trim();
        String strPassword = edtPassword.getText().toString().trim();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        progressDialog.show();
        auth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent= new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(Login.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void initUi(){

        progressDialog = new ProgressDialog(this);

        layoutSignUp = findViewById(R.id.layout_signUp);
        edtEmail = findViewById(R.id.edt_Email);
        edtPassword = findViewById(R.id.edt_Password);
        btnLogin = findViewById(R.id.btn_Login);
    }
}