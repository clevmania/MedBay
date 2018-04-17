package com.clevmania.medbay.ui.auth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.clevmania.medbay.MainActivity;
import com.clevmania.medbay.R;
import com.clevmania.medbay.firebase.FirebaseUtils;
import com.clevmania.medbay.model.ProfileModel;
import com.clevmania.medbay.ui.profile.ProfileManager;
import com.clevmania.medbay.utils.UiUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {
    private EditText email,password;
    private Button signUp, signIn, googleSignIn;
    private ProgressBar authIndicator;
    private TextView forgotPassword;
    private GoogleSignInOptions gso;
    private GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        configureGoogleSignIn();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authenticateUser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignUpForm();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetUsersPassword();
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

    }

    // Initialize views
    private void initViews(){
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        signIn = findViewById(R.id.btn_login);
        signUp = findViewById(R.id.tv_sign_up);
        forgotPassword = findViewById(R.id.tv_forgot_password);
        authIndicator = findViewById(R.id.pb_indicator);
        googleSignIn = findViewById(R.id.btn_google_sign_in);
    }

    // Validate Email address provided by user
    private boolean validateEmail() {
        String mail = email.getText().toString();

        if (mail.isEmpty()) {
            email.setError(getString(R.string.err_msg_email));
            return false;
        }
        if (!mail.isEmpty()) {
            if (!isValidEmail(mail)) {
                email.setError(getString(R.string.err_msg_invalid_email));
                requestFocus(email);
                return false;
            }
        }
        return true;
    }

    // Validate password provided by user
    private Boolean validatePassword() {
        if (password.getText().toString().trim().isEmpty()) {
            password.setError(getString(R.string.err_msg_password));
            requestFocus(password);
            return false;
        }

        return true;
    }

    // verify email address follows the specifications
    // of a valid email
    private Boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // returns focus to view
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // authenticate user
    private void authenticateUser(){
        if(!validateEmail())  return;
        if(!validatePassword()) return;
                authIndicator.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        FirebaseUtils.getAuthenticationReference()
                .signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            authIndicator.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            authIndicator.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Snackbar.make(signIn, "Authentication Failed.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
    }

    // reset user password
    private void resetUsersPassword(){
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle("Forgot Your Password");
        alertBox.setMessage("Don't worry, we'll send a reset link");
        final EditText resetMailText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        resetMailText.setLayoutParams(layoutParams);
        resetMailText.setHint("Your e-mail address");
        alertBox.setView(resetMailText);
        alertBox.setIcon(R.drawable.ic_reset);

        alertBox.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(resetMailText.getText().toString().isEmpty()){
                    UiUtils.showLongSnackBar(forgotPassword,"Email address is required");
                }else{
                    if(!isValidEmail(resetMailText.getText().toString())){
                        UiUtils.showLongSnackBar(forgotPassword,"Invalid E-mail address");
                    }
                    else{
                        FirebaseUtils.getAuthenticationReference()
                                .sendPasswordResetEmail(resetMailText.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                UiUtils.showLongSnackBar(forgotPassword,"Password reset link has been sent to "+
                                                        resetMailText.getText().toString());
                                            }else{
                                                UiUtils.showLongSnackBar(forgotPassword,"Failed to send reset link, perhaps mail isn't registered");
                                            }
                                    }
                                });
                    }
                }
            }

        });

        alertBox.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertBox.show();
    }

    // sign up form for users without google account
    private void launchSignUpForm(){
        startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
        finish();
    }

    // Configure Google sign in options
    private void configureGoogleSignIn(){
         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         googleApiClient = new GoogleApiClient.Builder(SignInActivity.this)
                 .enableAutoManage(SignInActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                     @Override
                     public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                     }
                 })
                 .addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
    }

    // Fire up the Google sign in api
    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // retrieve the result from api call and authenticate user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                // retrieve user account details from google
//                String idToken = googleSignInAccount.getIdToken();
//                String name = googleSignInAccount.getDisplayName();
//                String mail = googleSignInAccount.getEmail();
//                String photoUri = googleSignInAccount.getPhotoUrl().toString();
                firebaseAuthWithGoogle(googleSignInAccount);
            }else{
                 // Todo Sign in failed updateUI()
                Log.e(SignInActivity.class.getSimpleName(), "Login Unsuccessful");
//                Toast.makeText(this, “Login Unsuccessful”, Toast.LENGTH_SHORT).show();

            }

        }

    }

    // retrieve google account details of user
    // and signs user into firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        FirebaseUtils.getAuthenticationReference().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = FirebaseUtils.getAuthenticationReference().getCurrentUser();
                            updateUserProfile(user);
                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            UiUtils.showLongSnackBar(googleSignIn, "Authentication Failed");
//                            updateUserProfile(null);
                        }

                    }
                });
    }

    public void signOutUser(Context context) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                    }
                });

        FirebaseUtils.getAuthenticationReference().signOut();

        Intent logOutIntent = new Intent(context, SignInActivity.class);
        logOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logOutIntent);

    }

    private void updateUserProfile(FirebaseUser userDetails){
        // Save user profile via sharedPreference
        new ProfileManager(this).setUserDetails(userDetails.getDisplayName(),
                userDetails.getEmail(),userDetails.getPhoneNumber(),userDetails.getPhotoUrl().toString());

        // Save user details to firebase database
        FirebaseUtils.getProfileReference(userDetails.getUid())
                .setValue(new ProfileModel(userDetails.getDisplayName(),userDetails.getEmail(),
                        userDetails.getPhotoUrl().toString(),userDetails.getPhoneNumber()));
    }


}
