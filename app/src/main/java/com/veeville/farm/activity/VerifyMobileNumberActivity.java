package com.veeville.farm.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.veeville.farm.R;
import com.veeville.farm.helper.FarmerDetails;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


/*
 * used to verify Farmer  mobile number with fibase authontication by sending OTP
 */
public class VerifyMobileNumberActivity extends AppCompatActivity implements TextWatcher, View.OnKeyListener {

    private GoogleSignInClient mGoogleSignInClient;
    private FloatingActionButton submit;
    private final String TAG = VerifyMobileNumberActivity.class.getSimpleName();
    private FirebaseAuth mFirebaseAuth;
    private EditText first, second, third, fourth, fifth, sixth;
    private String verifixationIdMain, code = "";

    //initializing google sign in and firebase auth codes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile_number);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!code.equals("")) {
                    if (code.toCharArray().length == 6) {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifixationIdMain, code);
                        signInWithPhoneAuthCredential(credential);
                    } else {
                        Toast.makeText(VerifyMobileNumberActivity.this, "please enter all 6 digit OTP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyMobileNumberActivity.this, "please enter code sent to above number", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mFirebaseAuth = FirebaseAuth.getInstance();
        setUpToolbar();
        FarmerDetails details = getIntent().getParcelableExtra("FarmerDetails");
        String number = details.mobileNumber;
        TextView farmerMobileNumber = findViewById(R.id.farmer_mobile_number);
        farmerMobileNumber.setText(number);
        authonticateMobileNumber("+91" + number);//here manually appending country code , if you want to change country give option to choose country andappend number with that country code
    }

    //settingup custom toolbar
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Mobile Number Verification");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    //after OTP sent to given Number  enable to enter OTP in those edit text
    private void enablePasswordEditTextViews() {

        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        third = findViewById(R.id.third);
        fourth = findViewById(R.id.fourth);
        fifth = findViewById(R.id.fifth);
        sixth = findViewById(R.id.sixth);

        first.setEnabled(true);
        second.setEnabled(true);
        third.setEnabled(true);
        fourth.setEnabled(true);
        fifth.setEnabled(true);
        sixth.setEnabled(true);

        first.addTextChangedListener(this);
        first.setOnKeyListener(this);
        second.addTextChangedListener(this);
        third.addTextChangedListener(this);
        fourth.addTextChangedListener(this);
        fifth.addTextChangedListener(this);
        sixth.addTextChangedListener(this);

    }


    //after Getting OTP sign proceed to login with mobile number
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyMobileNumberActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            Log.d(TAG, "onComplete: Mobile Number verification successful");
                            verfificationSuccess();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Log.e(TAG, "onComplete: invalid credentials");
                            }
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
    }

    private void verfificationSuccess() {
        Intent intent = new Intent(VerifyMobileNumberActivity.this, DashBoardActivity.class);
        startActivity(intent);
        finish();
    }


    //after getting 10 digit mobile number start sending OTP to that
    private void authonticateMobileNumber(String mobileNumber) {

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            //if on Verification completed without the OTP
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            //if verification got failed
            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Log.d(TAG, "onVerificationFailed: try again");
                } else if (e instanceof FirebaseTooManyRequestsException) {// if quota exhausted
                    Toast.makeText(VerifyMobileNumberActivity.this, "quota for this number exhausted try again after some time", Toast.LENGTH_SHORT).show();
                }

            }

            //on OTP sent to given Mobile Number and Farm credential Object
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: success");
                verifixationIdMain = verificationId;
                enablePasswordEditTextViews();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobileNumber,        // Phone number to verify
                40,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                VerifyMobileNumberActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "onTextChanged: i:" + i + "\ti1:" + i1 + "\ti2:" + i2);
        if (i == 0) {
            if (first.getText().hashCode() == charSequence.hashCode()) {
                second.requestFocus();
            } else if (second.getText().hashCode() == charSequence.hashCode()) {
                third.requestFocus();
            } else if (third.getText().hashCode() == charSequence.hashCode()) {
                fourth.requestFocus();
            } else if (fourth.getText().hashCode() == charSequence.hashCode()) {
                fifth.requestFocus();
            } else if (fifth.getText().hashCode() == charSequence.hashCode()) {
                sixth.requestFocus();
            } else {
                submit.requestFocus();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(VerifyMobileNumberActivity.this, "logout success", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (keyEvent.getAction() == KeyEvent.KEYCODE_DEL) {
            Log.d(TAG, "onKey: back space deleted");
        }
        return false;
    }
}
