package com.veevillefarm.vfarm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.farmer.FarmerRegistrationActivity;
import com.veevillefarm.vfarm.helper.AddContactToServerService;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;

import java.util.Objects;


/*
 * this activity is used to login user through google Social login
 * if success then proceed for farmer registration else stay him here only
 */
public class SignActivity extends AppCompatActivity implements View.OnClickListener {

    private final int RC_SIGN_IN = 108;
    private final String TAG = SignActivity.class.getSimpleName();
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logMessage("onCreate called");
        //making UI full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();
        logMessage("onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logMessage("onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logMessage("onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logMessage("onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logMessage("onDestroy called");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    //on selecting account for google signin
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //handle the result after user selects account
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            assert account != null;
            logMessage(account.getDisplayName());
            String email = account.getEmail();
            String photoUrl = "no photo";
            try {
                photoUrl = Objects.requireNonNull(account.getPhotoUrl().toString());
            } catch (Exception e) {
                logErrorMessage("handleSignInResult: " + e.toString());
            }
            uploadContactToServer(account.getDisplayName(), email, photoUrl);
            logMessage(email);
            storeCurrentUserEmail(email,account.getDisplayName());
            ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
            database.insertuserCredentials(email);
            Intent intent = new Intent(getApplicationContext(), FarmerRegistrationActivity.class);
            intent.putExtra("FarmerName", account.getDisplayName());
            intent.putExtra("FarmerEmail", email);

            startActivity(intent);
            finish();
        } catch (ApiException e) {
            logErrorMessage(e.toString());
            Toast.makeText(this, "retry again", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeCurrentUserEmail(String email,String name){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_email",email);
        editor.putString("user_name",name);
        editor.apply();
        editor.commit();
    }

    //use this log method to ebugg error message everywhere
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }


    //use this log method to ebugg message everywhere
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }

    //upload contact details to AWS sql server
    private void uploadContactToServer(String name, String email, String photoUrl) {

        Intent intent = new Intent(getApplicationContext(), AddContactToServerService.class);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String fcmToken = pref.getString("fcm_token",null);
        logMessage("uploadContactToServer: fcmTken:"+fcmToken);
        intent.putExtra("name", name);
        intent.putExtra("fcmToken",fcmToken);
        intent.putExtra("email", email);
        intent.putExtra("photoUrl", photoUrl);
        startService(intent);
    }

}
