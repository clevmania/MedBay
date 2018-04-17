package com.clevmania.medbay.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.clevmania.medbay.MainActivity;
import com.clevmania.medbay.R;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.model.ProfileModel;
import com.clevmania.medbay.ui.profile.ProfileManager;
import com.clevmania.medbay.utils.UiUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText newUserMail, newUserPassword, newUserName;
    private Button login,createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginForm();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserAccount();
            }
        });
    }

    private void initViews(){
        newUserMail = findViewById(R.id.et_email);
        newUserPassword = findViewById(R.id.et_password);
        newUserName = findViewById(R.id.et_username);
        login = findViewById(R.id.btn_login);
        createAccount = findViewById(R.id.btn_create_account);
    }

    private void createUserAccount(){
        FirebaseUtils.getAuthenticationReference().createUserWithEmailAndPassword(newUserMail.getText().toString(),
                newUserPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUserProfile(FirebaseUtils.getAuthenticationReference().getCurrentUser());
                    startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                    finish();
                }else{
                    UiUtils.showLongSnackBar(createAccount, "Account could not be setup");

                }
            }
        });
    }

    private void launchLoginForm(){
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void updateUserProfile(FirebaseUser userDetails){
        // save user Uid
        new ProfileManager(this).setUid(userDetails.getUid());

        // Save user profile via sharedPreference
        new ProfileManager(this).setUserDetails(userDetails.getDisplayName(),
                userDetails.getEmail(),userDetails.getPhoneNumber(),userDetails.getPhotoUrl().toString());

        // Save user details to firebase database
        FirebaseUtils.getProfileReference(userDetails.getUid())
                .setValue(new ProfileModel(userDetails.getDisplayName(),userDetails.getEmail(),
                        userDetails.getPhotoUrl().toString(),userDetails.getPhoneNumber()));
    }

}
