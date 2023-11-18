package com.example.appselfstudy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserActivity extends AppCompatActivity {
    private ImageView imgAvatar;
    private TextView tvName,tveEmail;
    private CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initUi();
        showUserInformation();

    }
    private void initUi(){
        cardView = findViewById(R.id.card_view);
        imgAvatar = cardView.findViewById(R.id.img_uploadAvatar);
        tvName = findViewById(R.id.tv_name);
        tveEmail = findViewById(R.id.tv_email);
    }
    private  void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();
        if(name==null){
            tvName.setVisibility(View.GONE);
        }else{
            tvName.setVisibility(View.VISIBLE);
            tvName.setText(name);
        }
        tveEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable._userdefaultlogo).into(imgAvatar);
    }

}