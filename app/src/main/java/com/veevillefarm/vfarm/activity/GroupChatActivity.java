package com.veevillefarm.vfarm.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatContact;
import com.veevillefarm.vfarm.helper.ChatMessage;
import com.veevillefarm.vfarm.helper.InsertMessageToDatabase;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {

    private final String TAG = OneToOneChatActivity.class.getSimpleName();
    private List<Object> messages = new ArrayList<>();
    private RecyclerView chatMessageRecyclerview;
    private String fromAddress, toAddress;
    private Handler handler;
    private static final int CAMERA_REQUEST = 1888;
    private final static int REQUESTCODE_FOR_LOCATION = 100, REQUESTCODE_FOR_AUDIORECORD = 200;
    private static final int REQ_CODE_SPEECH_INPUT = 10;
    private String selectedlanguage_id_mic = "en-US", inputLanguageId = "en";
    private Toolbar toolbar;
    private EditText inputtextmessage;
    private int SELECT_PICTURE = 101;
    private boolean isServiceConnected = false;
    private  String fcmToken;

    //update selected language for voice to text default will be english
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.english_id:
                selectedlanguage_id_mic = "en-US";
                inputLanguageId = "en";
                toolbar.setSubtitle("English");
                break;
            case R.id.hindi_id:
                selectedlanguage_id_mic = "hi-IN";
                inputLanguageId = "hi";
                toolbar.setSubtitle("हिंदी");
                break;
            case R.id.kannada_id:
                selectedlanguage_id_mic = "kn-IN";
                inputLanguageId = "kn";
                toolbar.setSubtitle("ಕನ್ನಡ");
                break;
            case R.id.bengali_id:
                selectedlanguage_id_mic = "bn-IN";
                inputLanguageId = "bn";
                toolbar.setSubtitle("বাঙালি");
                break;
            case R.id.tamil_id:
                selectedlanguage_id_mic = "ta-IN";
                inputLanguageId = "ta";
                toolbar.setSubtitle("தமிழ்");
                break;
            case R.id.telugu_id:
                selectedlanguage_id_mic = "te-IN";
                inputLanguageId = "te";
                toolbar.setSubtitle("తెలుగు");
                break;
            case R.id.gujarati_id:
                selectedlanguage_id_mic = "gu-IN";
                inputLanguageId = "gu";
                toolbar.setSubtitle("ગુજરાતી");
                break;
            case R.id.urdu_id:
                selectedlanguage_id_mic = "ur-IN";
                inputLanguageId = "ur";
                toolbar.setSubtitle("Urdu");
                break;
            case R.id.marathi_id:
                selectedlanguage_id_mic = "mr-IN";
                inputLanguageId = "mr";
                toolbar.setSubtitle("मराठी");
                break;
            default:
                onBackPressed();
        }
        logMessage("onOptionsItemSelected: " + inputLanguageId);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat);
        //set from and to address
        fromAddress = getIntent().getStringExtra("from");
        toAddress = getIntent().getStringExtra("to");
        fcmToken = getIntent().getStringExtra("fcmToken");
        logMessage("onCreate: from:" + fromAddress + "\tto address:" + toAddress + "\tfcmToken:"+fcmToken);
        handler = new Handler();
        setUpToolbar();
        setUpChatMessageRecyclerview();
        handleSendindTextMesage();

    }

    //check camera and external storage write  permission
    private boolean checkCameraPermission() {

        int permissionlocation = ContextCompat.checkSelfPermission(GroupChatActivity.this, Manifest.permission.CAMERA);
        int permissionFileStorage = ContextCompat.checkSelfPermission(GroupChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionlocation == PackageManager.PERMISSION_GRANTED && permissionFileStorage == PackageManager.PERMISSION_GRANTED;
    }

    //if dont have camera and Storage  permission then ask for both here
    private void askCameraPermission() {
        String[] permissionarray = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this, "Enable Camera in App Settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, permissionarray, CAMERA_REQUEST);
        }
    }

    //keep on fetching messages from local database which inserted recenlty from Server database
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(runnable, 5000);
                }
            });
        }
    };

    void getImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    void handleSendindTextMesage() {

        final FloatingActionButton captureImage = findViewById(R.id.camera_fab);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()) {
                        getImageFromGallery();
                } else {
                    askCameraPermission();
                }
            }
        });

        inputtextmessage = findViewById(R.id.input_text_field);
        final FloatingActionButton actionButton = findViewById(R.id.action_button);

        inputtextmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    actionButton.setImageResource(R.drawable.ic_send_white);
                    captureImage.setVisibility(View.GONE);
                } else {
                    captureImage.setVisibility(View.VISIBLE);
                    actionButton.setImageResource(R.drawable.ic_mic_white);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (messages.size() > 0)
            chatMessageRecyclerview.scrollToPosition(messages.size() - 1);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputtextmessage.getText().toString();
                if (!message.equals("")) {
                    inputtextmessage.setText("");
                } else {
                    if (checkPermissionRECORD_AUDIO()) {
                        promptSpeechInput();
                    } else {
                        askPermissionRECORD_AUDIO();
                    }
                }
            }
        });
    }

    private boolean checkPermissionRECORD_AUDIO() {

        int permissionaudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionaudio == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermissionRECORD_AUDIO() {
        String[] permissionarray = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Enable Access to Media Storage and Recording in App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, permissionarray, REQUESTCODE_FOR_AUDIORECORD);
        }
    }

    private void setUpToolbar() {
        toolbar = findViewById(R.id.my_toolbar);
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("name");
                List<ChatContact> contacts = getIntent().getParcelableArrayListExtra("contacts");
                Intent intent = new Intent(getApplicationContext(),ShowGroupMembersActivity.class);
                intent.putParcelableArrayListExtra("contacts", (ArrayList<? extends Parcelable>) contacts);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpChatMessageRecyclerview() {
        chatMessageRecyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatMessageRecyclerview.setLayoutManager(manager);
        //messages = fetchAllMessages();
        //chatMessageRecyclerview.setAdapter(adapter);
        //handler.postDelayed(runnable, 5000);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE_FOR_LOCATION:
                break;
            case REQUESTCODE_FOR_AUDIORECORD:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    promptSpeechInput();
                }
                break;
            case CAMERA_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGallery();
                }

            default:
        }
    }

    private void promptSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedlanguage_id_mic);
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }










    //option menu with different language like kannada, hindi, marathi.....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return true;
    }

    //upload text message to aws mysql database
    private void insertmessageToServerDatabase(ChatMessage message) {

        Intent intent = new Intent(getApplicationContext(), InsertMessageToDatabase.class);
        intent.putExtra("ChatMessage", message);
        startService(intent);
    }

    //getting images url from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                assert selectedImageUri != null;
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //stopRecording();
                    inputtextmessage.setText(result.get(0));
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    assert inputMethodManager != null;
                                    boolean result = inputMethodManager.showSoftInput(inputtextmessage, InputMethodManager.SHOW_IMPLICIT);
                                    logMessage("onResume: result:" + result);
                                }
                            });
                        }
                    }, 200);
                }
                break;
        }

    }







    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }



}
