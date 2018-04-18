package com.clevmania.medbay.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.clevmania.medbay.MainActivity;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.ui.auth.SignInActivity;

/**
 * Created by grandilo-lawrence on 12/18/17.
 */

public class SplashActivity extends AppCompatActivity {
    private static final int splash_timer = 3000;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(FirebaseUtils.getAuthenticationReference().getCurrentUser() != null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }
            }, splash_timer);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,SignInActivity.class));
                    finish();
                }
            }, splash_timer);

        }

    }
}
