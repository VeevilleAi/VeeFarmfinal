package com.veeville.farm.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.veeville.farm.R;
import com.veeville.farm.adapter.OneToOneChatAdapter;
import com.veeville.farm.helper.ChatMessage;
import com.veeville.farm.helper.ChatMessageDatabase;
import com.veeville.farm.helper.ChatmessageDataClasses;
import com.veeville.farm.helper.InputImageClass;
import com.veeville.farm.helper.InsertMessageToDatabase;
import com.veeville.farm.helper.UploadImageService;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OneToOneChatActivity extends AppCompatActivity {

    private int currentImagePosition;
    private final String TAG = OneToOneChatActivity.class.getSimpleName();
    private List<Object> messages = new ArrayList<>();
    private OneToOneChatAdapter adapter;
    private RecyclerView chatMessageRecyclerview;
    private String fromAddress,toAddress;
    private Handler handler;
    private static final int CAMERA_REQUEST = 1888;
    private final static int REQUESTCODE_FOR_LOCATION = 100, REQUESTCODE_FOR_AUDIORECORD = 200;
    private static final int REQ_CODE_SPEECH_INPUT = 10;
    private String selectedlanguage_id_mic = "en-US", inputLanguageId = "en";
    private Toolbar toolbar;
    private EditText inputtextmessage;
    private int SELECT_PICTURE = 101;

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
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_to_one_chat);

        handler = new Handler();

        //set from and to address
        fromAddress = getIntent().getStringExtra("from");
        toAddress = getIntent().getStringExtra("to");
        Log.d(TAG, "onCreate: from:"+fromAddress+"\tto address:"+toAddress);
        setUpToolbar();
        setUpChatMessageRecyclerview();
        handleSendindTextMesage();
    }

    private boolean checkCameraPermission() {

        int permissionlocation = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.CAMERA);
        int permissionFileStorage = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionlocation == PackageManager.PERMISSION_GRANTED && permissionFileStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission() {
        String[] permissionarray = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(OneToOneChatActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(OneToOneChatActivity.this, "Enable Camera in App Settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(OneToOneChatActivity.this, permissionarray, CAMERA_REQUEST);
        }
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addNewMessages();
                    handler.postDelayed(runnable,5000);
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

        FloatingActionButton camera_fab = findViewById(R.id.camera_fab);
        camera_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCameraPermission()) {
                    if(isImageUploadSuccess())
                    getImageFromGallery();
                    else Toast.makeText(getApplicationContext(), "wait still image uploading", Toast.LENGTH_SHORT).show();
                } else {
                    askCameraPermission();
                }
            }
        });

        inputtextmessage = findViewById(R.id.input_text_field);
        final FloatingActionButton actionButton = findViewById(R.id.action_button);
        final FloatingActionButton captureImage = findViewById(R.id.camera_fab);

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
                    addInputTextMessage(message);
                }else {
                    if(checkPermissionRECORD_AUDIO()){
                        promptSpeechInput();
                    }else {
                        askPermissionRECORD_AUDIO();
                    }
                }
            }
        });
    }
    private boolean checkPermissionRECORD_AUDIO() {

        int permissionaudio = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.RECORD_AUDIO);
        int permissionStorage = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionaudio == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermissionRECORD_AUDIO() {
        String[] permissionarray = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(OneToOneChatActivity.this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(OneToOneChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Enable Access to Media Storage and Recording in App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(OneToOneChatActivity.this, permissionarray, REQUESTCODE_FOR_AUDIORECORD);
        }
    }
    private void setUpToolbar() {
        toolbar = findViewById(R.id.my_toolbar);
        String name = getIntent().getStringExtra("name");
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setUpChatMessageRecyclerview(){
        chatMessageRecyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatMessageRecyclerview.setLayoutManager(manager);
        messages = fetchAllMessages();
        adapter = new OneToOneChatAdapter(messages);
        chatMessageRecyclerview.setAdapter(adapter);
        handler.postDelayed(runnable,5000);

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


    private void addNewMessages(){
        int previousSize = messages.size();
        List<Object> objects = fetchAllMessages();
        for (int i = previousSize; i < objects.size(); i++) {
            messages.add(objects.get(i));
        }
        adapter.notifyItemRangeChanged(previousSize,objects.size()-previousSize);
        chatMessageRecyclerview.scrollToPosition(messages.size()-1);
    }


    private void addInputTextMessage(String message){
        messages.add(new ChatmessageDataClasses.InputTextMessage(message,0));
        adapter.notifyItemInserted(messages.size()-1);
        chatMessageRecyclerview.scrollToPosition(messages.size()-1);
        insertMessage(message,fromAddress,toAddress);
    }

    private void insertMessage(String message,String from,String to){
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        long timestamp = System.currentTimeMillis()/1000;
        ChatMessage chatMessage = new ChatMessage("",from,to,message,timestamp);
        database.insertChatMessage(chatMessage);
        insertmessageToServerDatabase(message,from,to,timestamp);
    }



    private long getDateToTimestamp(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    private List<Object> fetchAllMessages(){
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        List<ChatMessage> chatMessages = database.getChatMessages(fromAddress,toAddress);
        List<Object> messages = new ArrayList<>();
        long timestamp = 0;
        if(chatMessages.size()>0)
        timestamp = chatMessages.get(0).timestamp;
        ChatmessageDataClasses.DateInMessage dateInMessage =new ChatmessageDataClasses.DateInMessage(getDate(timestamp));
        messages.add(dateInMessage);
        for (ChatMessage message:chatMessages) {


            if(timestamp-getDateToTimestamp(message.timestamp)>(24*60*60*1000)){
                timestamp = message.timestamp;
                messages.add(new ChatmessageDataClasses.DateInMessage(getDate(timestamp)));
            }

            if(message.from.equals(fromAddress)){
                ChatmessageDataClasses.InputTextMessage textMessage = new ChatmessageDataClasses.InputTextMessage(message.message,0);
                messages.add(textMessage);
            }else {
                ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message.message,0);
                messages.add(textMessage);

            }

        }
        return messages;
    }

    private String getDate(long time) {

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
            return "Today";
        } else {
            return DateFormat.format("dd-MMM-yyyy", cal).toString();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.languagemenu,menu);
        return true;
    }

    private void insertmessageToServerDatabase(String message, String fromAddress, String toAddress, long timestamp){

        Log.d(TAG, "insertmessageToServerDatabase: message:"+message+"\t from :"+fromAddress+"\tto:"+toAddress);
        Intent intent = new Intent(getApplicationContext(), InsertMessageToDatabase.class);
        intent.putExtra("fromAddress",fromAddress);
        intent.putExtra("toAddress",toAddress);
        intent.putExtra("chatMessage",message);
        intent.putExtra("timestamp",timestamp);
        startService(intent);
    }    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

//            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
//            assert bitmap != null;
//            String stringImage = getStringImage(bitmap);
//            alertDialogForImageWithText(stringImage);
//            tookPicture = true;

//        }
//        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//
//            String imageString = getBase64String();
//            long timestamp = System.currentTimeMillis();
//            InputImageClass inputImageClass = new InputImageClass(imageString, false, timestamp);
//            chatMessages.add(inputImageClass);
//            adapter.notifyItemInserted(chatMessages.size() - 1);
//            chatrecyclerview.smoothScrollToPosition(chatMessages.size() - 1);
//            ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(getApplicationContext());
//            helperFunctions.insertInputImage(imageString);
//
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                assert selectedImageUri != null;
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                insertInputImage(bm,selectedImageUri);

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
                                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    assert inputMethodManager != null;
                                    boolean result = inputMethodManager.showSoftInput(inputtextmessage, InputMethodManager.SHOW_IMPLICIT);
                                    Log.d(TAG, "onResume: result:"+result);
                                }
                            });
                        }
                    },200);
                }
                break;
        }

    }

    private UploadImageService uploadImageServiceTemp;
    private boolean isServiceConnected = false;

    private void insertInputImage(Bitmap bitmap,Uri selectedImageUri){

        ChatmessageDataClasses.InputBitMapImage bitMapImage = new ChatmessageDataClasses.InputBitMapImage(bitmap,false);
        messages.add(bitMapImage);
        currentImagePosition = messages.size()-1;
        adapter.notifyItemInserted(messages.size()-1);
        chatMessageRecyclerview.scrollToPosition(messages.size()-1);
        final Intent uploadImageService = new Intent(getApplicationContext(), UploadImageService.class);
        uploadImageService.putExtra("imageUri",selectedImageUri.toString());
        startService(uploadImageService);
        bindService = new Intent(getApplicationContext(),UploadImageService.class);
        bindService(bindService,serviceConnection,Context.BIND_AUTO_CREATE);

    }

    Intent bindService;
    private Boolean isImageUploadSuccess(){
        if(isServiceConnected){
            boolean success = uploadImageServiceTemp.isImageUploadSuccess();
            if(success) {
                unbindService(serviceConnection);
                return true;
            }else {
                return false;
            }
        }else {
            return true;
        }
    }
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isServiceConnected = true;
            UploadImageService.MyBinder binder = (UploadImageService.MyBinder) iBinder;
            uploadImageServiceTemp = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceConnected =  false;
        }
    };
}
