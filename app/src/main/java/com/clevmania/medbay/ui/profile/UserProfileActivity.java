package com.clevmania.medbay.ui.profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clevmania.medbay.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView username, email, phoneNumber;
    private ProfileManager profileManager;
    private CircleImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        populateUserProfile();
    }

    private void initViews(){
        userImage = findViewById(R.id.civ_user);
        profileManager = new ProfileManager(this);
        username = findViewById(R.id.tv_user_name);
        email = findViewById(R.id.tv_user_email);
        phoneNumber = findViewById(R.id.tv_user_mobile);
    }

    private void populateUserProfile(){
        username.setText(profileManager.getUserName());
        email.setText(profileManager.getUserMail());
        phoneNumber.setText(profileManager.getUserMobile());
        if(profileManager.getUserImg() != null){
            Picasso.with(this).load(profileManager.getUserImg()).into(userImage);
        }
    }

}
