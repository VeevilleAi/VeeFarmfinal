package com.veevillefarm.vfarm.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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

import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.adapter.OneToOneChatAdapter;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatMediaType;
import com.veevillefarm.vfarm.helper.ChatMessage;
import com.veevillefarm.vfarm.helper.ChatMessageDatabase;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;
import com.veevillefarm.vfarm.helper.FCMMessage;
import com.veevillefarm.vfarm.helper.SendMessageToLambdaFunction;
import com.veevillefarm.vfarm.helper.UploadFileToAwsS3Class;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
 * it is for one to one chat as of now he can send only Text messages and Images
 * logic has to improve a lot
 * syncing messages has to improve
 * as of now images are taken from galley further we should option for camera also
 * has option to speech to text with different language
 */
public class OneToOneChatActivity extends AppCompatActivity implements UploadFileToAwsS3Class.FileUploadStatus,OneToOneChatAdapter.UpdateDownloadedImageInRecyclerview {

    private final String TAG = OneToOneChatActivity.class.getSimpleName();
    private List<Object> messages = new ArrayList<>();
    private OneToOneChatAdapter adapter;
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
    private String fcmToken;
    private Bitmap selectedImageBitMap;
    private long selectedImageId;
    private Uri selectedImageUri;

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
        intilizeAwsClient();

        fromAddress = getIntent().getStringExtra("from");
        toAddress = getIntent().getStringExtra("to");
        fcmToken = getIntent().getStringExtra("fcmToken");
        logMessage("onCreate: from:" + fromAddress + "\tto address:" + toAddress + "\tfcmToken:" + fcmToken);
        handler = new Handler();
        setUpToolbar();
        setUpChatMessageRecyclerview();
        handleSendindTextMesage();

    }

    private void sendMessageToAwsLambda(FCMMessage message) {

        SendMessageToLambdaFunction function = new SendMessageToLambdaFunction();
        try {
            function.sendFcmNotification(message);
            logMessage("sendMessageToAwsLambda:called");
        } catch (JSONException e) {
            e.printStackTrace();
            logErrorMessage("sendMessageToAwsLambda:" + e.toString());
        }
    }

    //check camera and external storage write  permission
    private boolean checkCameraPermission() {

        int permissionlocation = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.CAMERA);
        int permissionFileStorage = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ContextCompat.checkSelfPermission(OneToOneChatActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return permissionlocation == PackageManager.PERMISSION_GRANTED && permissionFileStorage == PackageManager.PERMISSION_GRANTED && readPermission == PackageManager.PERMISSION_GRANTED;
    }

    //if dont have camera and Storage  permission then ask for both here
    private void askCameraPermission() {
        String[] permissionarray = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(OneToOneChatActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(OneToOneChatActivity.this, "Enable Camera in App Settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(OneToOneChatActivity.this, permissionarray, CAMERA_REQUEST);
        }
    }

    //keep on fetching messages from local database which inserted recenlty from Server database
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addNewMessages();
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
//                    if (isImageUploadSuccess())
                    getImageFromGallery();
//                    else
//                        Toast.makeText(getApplicationContext(), "wait still image uploading", Toast.LENGTH_SHORT).show();
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
                    sendNotification(message, ChatMediaType.TYPE_TEXT);
                    addInputTextMessageToRecyclerview(message);
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


    private void sendNotification(String message, String messageType) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String fromFcmToken = pref.getString("fcm_token", null);
        String title = pref.getString("user_name", null);
        logMessage("name:" + title + "\tfcm token:" + fcmToken);

        FCMMessage fcmMessage = new FCMMessage(title, message, this.fcmToken, fromAddress, toAddress, messageType, fromFcmToken, selectedImageId);
        logMessage("awsId:"+selectedImageId);
        logMessage("sendNotification:" + message );
        sendMessageToAwsLambda(fcmMessage);
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

    private void setUpChatMessageRecyclerview() {
        chatMessageRecyclerview = findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatMessageRecyclerview.setLayoutManager(manager);
        messages = fetchAllMessages();
        adapter = new OneToOneChatAdapter(getApplicationContext(), messages,this);
        chatMessageRecyclerview.setAdapter(adapter);
        handler.postDelayed(runnable, 5000);

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


    //check for new message if available then add those to chat message recyclerview
    private void addNewMessages() {
        int previousSize = messages.size();
        List<Object> objects = fetchAllMessages();
        for (int i = previousSize; i < objects.size(); i++) {
            messages.add(objects.get(i));
        }
        adapter.notifyItemRangeChanged(previousSize, objects.size() - previousSize);
        chatMessageRecyclerview.scrollToPosition(messages.size() - 1);
    }


    //add new text messages to messages recyclerview
    private void addInputTextMessageToRecyclerview(String message) {
        messages.add(new ChatmessageDataClasses.InputTextMessage(message, 0));
        adapter.notifyItemInserted(messages.size() - 1);
        chatMessageRecyclerview.scrollToPosition(messages.size() - 1);
        insertMessageToLocalDatabase(message, fromAddress, toAddress);
    }

    //inserting text messages to local database
    private void insertMessageToLocalDatabase(String message, String from, String to) {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        long timestamp = System.currentTimeMillis() / 1000;
        database.insertChatTextMessage(timestamp, message);
        ChatMessage chatMessage = new ChatMessage(timestamp, ChatMediaType.TYPE_TEXT, from, to, timestamp, timestamp, message);
        database.insertChatMessage(chatMessage);
        //insertmessageToServerDatabase(chatMessage);
    }


    //fetch all messages from database in beginning
    private List<Object> fetchAllMessages() {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());

        //logMessage("fetchAllMessages: from:Address:" + fromAddress + "\ttoAddress:" + toAddress);
        return database.getChatMessages(fromAddress, toAddress);
    }


    //option menu with different language like kannada, hindi, marathi.....
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language, menu);
        return true;
    }


    //getting images url from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                assert selectedImageUri != null;
                selectedImageBitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                this.selectedImageUri = selectedImageUri;
                logMessage("selected Image URi:" + selectedImageUri.toString());
                uploadImagestoAwsS3(selectedImageUri);

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


    private void intilizeAwsClient() {
        AWSMobileClient.getInstance().initialize(this).execute();
    }

    // upload selected image to AWS mysql database as BLOB
    private void insertInputImage(Bitmap bitmap, Uri selectedImageUri) {


        try {
            long timestamp = System.currentTimeMillis() / 1000;
            String sourceFilePath = getPath(selectedImageUri);
            if (sourceFilePath != null) {
                File source = new File(sourceFilePath);
                if(source.exists()){
                    logMessage("source file exists");
                }
                String destionationDirectory = AppSingletonClass.folderPath;
                String destFileName = timestamp + ".jpg";
                File destination = new File(destionationDirectory, destFileName);
                boolean isNewFileCreated = destination.createNewFile();
                logMessage("is new file created :"+isNewFileCreated);
                copyToVeeFarmMediaLocation(source, destination);
                logMessage("copied successfully");
                addImageToRecyclerview(destFileName);
                logMessage("added to recyclerview");
                ChatMessage chatMessage = new ChatMessage(timestamp, ChatMediaType.TYPE_IMAGE, fromAddress, toAddress, timestamp, timestamp, selectedImageUri.toString());
                insertImageInToLocalStorage(timestamp, destFileName, chatMessage);
                logMessage("successfully inserted to local storage");
                compressAndSendImageinNotification(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logErrorMessage("insertInputImage:  "+e.toString());
        }

    }

    private void addImageToRecyclerview(String destFileName) {
        ChatmessageDataClasses.InputBitMapImage bitMapImage = new ChatmessageDataClasses.InputBitMapImage(selectedImageId, null, true, destFileName,null);
        messages.add(bitMapImage);
        adapter.notifyItemInserted(messages.size() - 1);
        chatMessageRecyclerview.scrollToPosition(messages.size() - 1);
    }


    private void copyToVeeFarmMediaLocation(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    private void compressAndSendImageinNotification(Bitmap bitmap) {
        try {
            ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
            byte[] imageData = null;
            int THUMBNAIL_SIZE = 100;
            int quality = 100;
            Bitmap imageBitmap = bitmap;
            while (THUMBNAIL_SIZE > 0) {
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                imageData = baos.toByteArray();
                THUMBNAIL_SIZE = THUMBNAIL_SIZE - 10;
                quality = quality - 10;
                String imgString = Base64.encodeToString(imageData, Base64.NO_WRAP);
                int length = imgString.toCharArray().length;
                logMessage("image string size:" + length);
                if (length < 3500) {
                    break;
                }

            }

            String imgString = Base64.encodeToString(imageData, Base64.NO_WRAP);
            logMessage("image string size:" + imgString.toCharArray().length);
            sendNotification(imgString, ChatMediaType.TYPE_IMAGE);
        } catch (Exception ex) {
            logErrorMessage(ex.toString());
        }
    }


    //inserting selected image into local database and
    private void insertImageInToLocalStorage(long imageId, String filePath, ChatMessage chatMessage) {
        ChatMessageDatabase database = new ChatMessageDatabase(getApplicationContext());
        ChatmessageDataClasses.InputBitMapImage data = new ChatmessageDataClasses.InputBitMapImage(imageId, null, true, filePath,null);
        database.insertInputImage(data);
        database.insertChatMessage(chatMessage);
    }


    //if user comeback then unbind service otherwise service will be active for ever
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void logMessage(String message) {
        AppSingletonClass.logMessage(TAG, message);
    }

    private void logErrorMessage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }


    private String getPath(Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        if (DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    String data =  cursor.getString(column_index);
                    cursor.close();
                    return data;
                }
            } catch (Exception e) {
                logErrorMessage(e.toString());
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void uploadImagestoAwsS3(Uri selectedImageUri) {

        logMessage("called uploadImagestoAwsS3");
        String path = getPath(selectedImageUri);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                UploadFileToAwsS3Class awsS3Class = new UploadFileToAwsS3Class(getApplicationContext(), this);
                selectedImageId = System.currentTimeMillis();
                awsS3Class.uploadImageFile(selectedImageId + ".jpg", file);
            } else {
                logMessage("file does not exist");
            }
        } else {
            logMessage("path does not exists");
        }
    }

    @Override
    public void fileuploafStatus(boolean isUploaded) {
        Toast.makeText(this, "upload status:" + isUploaded, Toast.LENGTH_SHORT).show();
        if (isUploaded) {
            logMessage("successfully uploaded and inserting to local storage called");
            insertInputImage(selectedImageBitMap, selectedImageUri);
        }
    }

    @Override
    public void fileUploadProgressStatus(int progress) {
        Toast.makeText(this, "progress:" + progress, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void isUploadCancleled(boolean trueOfFalse) {

    }


    @Override
    public void updateRecyclerview(long imgId) {
        logMessage("imag id to updated:"+imgId);
        addNewMessages();
    }
}
