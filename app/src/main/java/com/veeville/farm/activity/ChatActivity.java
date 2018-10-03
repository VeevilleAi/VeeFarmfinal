package com.veeville.farm.activity;

import  android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatMessagesHelperFunctions;
import com.veeville.farm.tensorflow.ClassifierActivity;
import com.veeville.farm.R;
import com.veeville.farm.adapter.BotAdapter;
import com.veeville.farm.adapter.QuickReplyAdapter;
import com.veeville.farm.helper.ChatBotDatabase;
import com.veeville.farm.helper.ChatmessageDataClasses;
import com.veeville.farm.helper.InputImageClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIDataService;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class ChatActivity extends AppCompatActivity implements QuickReplyAdapter.QuickReplyOption, ProcessBotResponse.ProcessedResult {

    private RecyclerView chatrecyclerview;
    private FloatingActionButton actionButton;
    private EditText inputtextmessage;
    private BotAdapter adapter;
    private SensGConnection connection;
    private int REQ_CODE_SPEECH_INPUT = 10;
    private List<Object> chatMessages = new ArrayList<>();
    private String TAG = "ChatActivity";
    private Toolbar toolbar;
    private FloatingActionButton captureImage;
    private static final int CAMERA_REQUEST = 1888;
    private String selectedlanguage_id_mic = "en-US", selectedLanguage = "English", inputLanguageId = "en";
    private final int REQUESTCODE_FOR_LOCATION = 100, REQUESTCODE_FOR_AUDIORECORD = 200;
    private Uri filePathImage = null;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private final String ACCESS_TOKEN = "c47e8cf9778f45f9a7ca89742d8e9311";
    private AIDataService aiDataService;
    boolean isvQnaEnabled = false;
    private SwitchCompat aSwitch;
    private ArrayList<Object> vQnaList;
    boolean tookPicture = false;
    Handler handler = new Handler();
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
            case R.id.sens_g_connection:
                dialogToAskIpOfSensG();
                break;
            default:
                onBackPressed();
        }
        return true;
    }

    void dialogToAskIpOfSensG() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_layout_for_ip_of_sensg, null);
        builder.setView(view);
        final EditText ipAddress = view.findViewById(R.id.ip_of_sensg);
        ipAddress.requestFocus();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert inputMethodManager != null;
                        boolean result = inputMethodManager.showSoftInput(ipAddress, InputMethodManager.SHOW_IMPLICIT);
                        Log.d(TAG, "onResume: result:" + result);
                    }
                });
            }
        }, 200);

        Button submit = view.findViewById(R.id.submit);
        Button cancel = view.findViewById(R.id.cancel);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IP = ipAddress.getText().toString();
                if (!IP.equals("")) {
                    dialog.dismiss();
                    connection = new SensGConnection(IP, getApplicationContext());
                    connection.sendRequest();
                } else {
                    Toast.makeText(getApplicationContext(), "please enter ip address", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.languagemenu, menu);
        return true;
    }

    void getImageFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    private void loadAllMessages() {
        ChatBotDatabase chatBotDatabase = new ChatBotDatabase(getApplicationContext());
        List list = chatBotDatabase.fetchAllMessages();
        if (list.size() > 0) {
            chatMessages.addAll(list);
        } else {
            addIntroductionMessage();
        }
    }


    void setUpRecyclerview() {
        chatrecyclerview = findViewById(R.id.chatrecyclerviewid);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatrecyclerview.setLayoutManager(linearLayoutManager);
        adapter = new BotAdapter(chatMessages, getApplicationContext(), this);
        chatrecyclerview.setAdapter(adapter);
        runWhileLoop();
    }


    private void addIntroductionMessage() {

        String message = " Select one of the options below or ask me a question to get started ";
        ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message);
        List<String> selectableMenu = new ArrayList<>();
        selectableMenu.add("What disease affects fruits?");
        selectableMenu.add("How to plant tree ?");
        selectableMenu.add("What is the weather like tomorrow?");
        selectableMenu.add("How much is the price of Vegetables?");
        ChatmessageDataClasses.OptionMenu optionMenu = new ChatmessageDataClasses.OptionMenu(selectableMenu);
        chatMessages.add(textMessage);
        chatMessages.add(optionMenu);
        adapter.notifyDataSetChanged();

    }


    void setUpToolbar() {
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Veeville");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        toolbar.setSubtitle(selectedLanguage);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }


    void handleSendindTextMesage() {

        inputtextmessage = findViewById(R.id.input_text_field);
        actionButton = findViewById(R.id.action_button);
        inputtextmessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    actionButton.setImageResource(R.drawable.ic_send_black_24dp);
                    captureImage.setVisibility(View.GONE);
                } else {
                    captureImage.setVisibility(View.VISIBLE);
                    actionButton.setImageResource(R.drawable.ic_mic_none_black_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (chatMessages.size() > 0)
            chatrecyclerview.scrollToPosition(chatMessages.size() - 1);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = inputtextmessage.getText().toString();
                if (message.equals("")) {
                    if (checkPermissionRECORD_AUDIO()) {
                        promptSpeechInput();
                    } else {
                        askPermissionRECORD_AUDIO();
                    }
                } else {
                    insertTextMessage(message, true);
                    if (!isvQnaEnabled) {
                        translateText(message);
                    } else {
                        sendQuestionForUploadedImage(message);
                    }
                    inputtextmessage.setText("");
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppSingletonClass.mFirebaseAuth = FirebaseAuth.getInstance();
        setUpToolbar();
        setUpRecyclerview();
        loadAllMessages();
        handleSendindTextMesage();

        captureImage = findViewById(R.id.camera_fab);
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCameraPermission()) {
                    alertDialogForChoosingCameraOrGalleryForImage();

                } else {
                    askCameraPermission();
                }
            }
        });
        aSwitch = findViewById(R.id.switch_id);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (tookPicture) {
                    isvQnaEnabled = b;
                } else {
                    aSwitch.setChecked(false);
                }
                Log.d(TAG, "onCheckedChanged: ischecked:" + b);

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean checkPermissionRECORD_AUDIO() {

        int permissionaudio = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.RECORD_AUDIO);
        int permissionStorage = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionaudio == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermissionRECORD_AUDIO() {
        String[] permissionarray = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Enable Access to Media Storage and Recording in App Settings", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ChatActivity.this, permissionarray, 123);
        }
    }

    private void sendQuestionForUploadedImage(String userQuery) {

        String url = "http://54.201.152.162:5000/vqa";
        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("q", userQuery);
            requestBody.put("image_id", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: " + response.toString());
                    JSONArray jsonArray = response.getJSONArray("results");
                    StringBuilder buffer = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        buffer.append(jsonArray.getJSONArray(i).getString(0)).append(":");
                        buffer.append(jsonArray.getJSONArray(i).getDouble(1)).append("\n");
                    }
                    insertTextMessage(buffer.toString(), false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);

    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    public void selectedMessage(String message) {
        chatMessages.remove(chatMessages.size() - 1);
        adapter.notifyItemRemoved(chatMessages.size() - 1);
        insertTextMessage(message, true);
        requestToDialogFlow(message);
    }

    @Override
    public void insertDiseaseNames(String diseaseName) {
        insertTextMessage(diseaseName, true);
        insertResponseMessage(diseaseName);

    }

    void insertResponseMessage(String disease) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sun burn & Canker Diseases", "You have to cover the stem portion with Bordeaux paint after plants start shedding leaves.");

        hashMap.put("alernania leaf spot", "Spray Dormant oil (TSO) 2% OR Horticulture mineral oil (HMO) 2-3 Liters in 200 liters water\n" +
                "\n" +
                "Please avoid this if the temperature is below 5 degree C & above 30 degree C.");
        hashMap.put("apple anthracnose", "Apply First split dose of Nitrogen @350 gm per tree");

        hashMap.put("apple coddling moth", "Apply First split dose of Nitrogen @350 gm per tree");

        hashMap.put("Green Tip Stage", "Spray Dodine 65% WP @ 0.075% or Captan 50% WP @ 2.5 kg per hectare in 750 - 1000 Liters of water\n" +
                "\n" +
                "Note:  OnlyIf scab was present in previous 3 years");
        insertTextMessage(hashMap.get(disease), true);
    }

    @Override
    public void insertImage(String qNaQuesry, String imageLink) {

        chatMessages.remove(chatMessages.size() - 1);
        adapter.notifyItemRemoved(chatMessages.size() - 1);

        ChatmessageDataClasses.InputImageMessage inputImageMessage = new ChatmessageDataClasses.InputImageMessage(imageLink);
        chatMessages.add(inputImageMessage);
        adapter.notifyItemInserted(chatMessages.size() - 1);
        ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
        database.insertChats("inputimagelink", imageLink);
        requestToQnAmaker(qNaQuesry);
        Log.d(TAG, "insertImage: success till here");

    }

    private void requestToQnAmaker(final String question) {
        logMesage("question sending to QnAMaker :" + question);
        String url = "https://appleqna.azurewebsites.net/qnamaker/knowledgebases/c59c0050-9297-4300-9f17-26f7ed0f7e1e/generateAnswer";
        JSONObject body = new JSONObject();
        try {
            body.put("question", question);
        } catch (JSONException e) {
            e.printStackTrace();
            logMesage("cought " + e.toString() + " while forming body for QnAMaker");
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logMesage("Response from QnA Maker :" + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("answers");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String answer = object.getString("answer");
                    logMesage("answer for given Question :" + question + " is " + answer);
                    translateTextToInputLanguage(answer, inputLanguageId);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "EndpointKey 2ea9dc0c-9463-46ba-8888-415e4a22cc91");
                return map;
            }
        };
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }


    @Override
    public void result(List list) {

        int previousPosition = chatMessages.size() - 1;
        chatMessages.addAll(list);
        adapter.notifyItemRangeChanged(previousPosition, chatMessages.size() - 1);
        chatrecyclerview.scrollToPosition(chatMessages.size() - 1);
    }


    private class MyAsyncTask extends AsyncTask<AIRequest, Void, AIResponse> {
        @Override
        protected AIResponse doInBackground(AIRequest... requests) {
            final AIRequest request = requests[0];
            try {
                return aiDataService.request(request);
            } catch (AIServiceException e) {
                Log.e(TAG, "doInBackground:" + e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            if (aiResponse != null) {
                Result result = aiResponse.getResult();
                processResult(result);
            } else {
                Log.d(TAG, "onPostExecute: airesponse is null");
            }
        }
    }


    private void processResult(Result result) {

        ProcessBotResponse processBotResponse = new ProcessBotResponse(result, ChatActivity.this, this, inputLanguageId);
        processBotResponse.getResultBack();

    }


    private void requestToDialogFlow(String query) {

        final AIConfiguration config = new AIConfiguration(ACCESS_TOKEN, AIConfiguration.SupportedLanguages.English, AIConfiguration.RecognitionEngine.System);
        aiDataService = new AIDataService(getApplicationContext(), config);
        AIRequest aiRequest = new AIRequest();
        aiRequest.setQuery(query);
        new MyAsyncTask().execute(aiRequest);
        handler.postDelayed(runnable,3000);

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

    private void alertDialogForImageWithText(final String imageData) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        View view = getLayoutInflater().inflate(R.layout.visualqna_dialog_with_question, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText query = view.findViewById(R.id.edittext);

        ImageView capturedImage = view.findViewById(R.id.image);
        byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        capturedImage.setImageBitmap(decodedByte);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        assert inputMethodManager != null;
                        boolean result = inputMethodManager.showSoftInput(query, InputMethodManager.SHOW_IMPLICIT);
                        Log.d(TAG, "onResume: result:" + result);
                    }
                });
            }
        }, 200);
        Button send = view.findViewById(R.id.send);
        Button cancel = view.findViewById(R.id.cancel);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userQuery = query.getText().toString();
                if (!userQuery.equals("")) {
                    isvQnaEnabled = true;
                    aSwitch.setChecked(true);
                    dialog.cancel();
                    vQnaList = new ArrayList<>();
                    ChatmessageDataClasses.InputImageMessage message = new ChatmessageDataClasses.InputImageMessage(imageData);
                    vQnaList.add(message);
                    ChatmessageDataClasses.InputTextMessage inputTextMessage = new ChatmessageDataClasses.InputTextMessage(userQuery);
                    vQnaList.add(inputTextMessage);
                    ChatmessageDataClasses.VisualQnA visualQnA = new ChatmessageDataClasses.VisualQnA(vQnaList);
                    chatMessages.add(visualQnA);
                    adapter.notifyItemInserted(chatMessages.size() - 1);
                    chatrecyclerview.scrollToPosition(chatMessages.size() - 1);
                    uploadImageNew("data:image/jpeg;base64," + imageData, userQuery);

                    ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(getApplicationContext());
                    helperFunctions.insertInputImage(imageData);
                    helperFunctions.insertInputTextMessage(userQuery);
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            assert bitmap != null;
            String stringImage = getStringImage(bitmap);
            alertDialogForImageWithText(stringImage);
            Log.d(TAG, "onActivityResult: base64 string:" + stringImage);
            tookPicture = true;


        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            String imageString = getBase64String();
            InputImageClass inputImageClass = new InputImageClass(imageString, false);
            chatMessages.add(inputImageClass);
            adapter.notifyItemInserted(chatMessages.size() - 1);
            chatrecyclerview.smoothScrollToPosition(chatMessages.size() - 1);
            ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(getApplicationContext());
            helperFunctions.insertInputImage(imageString);

        }
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            try {
                assert selectedImageUri != null;
                Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImageUri));
                String stringImage = getStringImage(bm);
                alertDialogForImageWithText(stringImage);
                Log.d(TAG, "onActivityResult: base64 string:" + stringImage);
                tookPicture = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputtextmessage.setText(result.get(0));
                }
                break;
        }
    }


    private boolean checkCameraPermission() {

        int permissionlocation = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.CAMERA);
        int permissionFileStorage = ContextCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionlocation == PackageManager.PERMISSION_GRANTED && permissionFileStorage == PackageManager.PERMISSION_GRANTED;
    }

    private void askCameraPermission() {
        String[] permissionarray = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this, Manifest.permission.CAMERA)) {
            Toast.makeText(ChatActivity.this, "Enable Camera in App Settings", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(ChatActivity.this, permissionarray, 124);
        }
    }

    private void translateTextToInputLanguage(String translatingText, String targetLanguage) {
        AppSingletonClass.logDebugMessage(TAG, "translate text to given language : Query:" + translatingText + "\t language code:" + targetLanguage);
        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAeN43km6hD8UHAUaFEZ8j9iCYFk8puvuE";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("q", translatingText);
            jsonObject.put("target", targetLanguage);
        } catch (Exception e) {
            logMesage("cought " + e.toString() + " while forming body for translating text ");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logMesage("response from google translater :" + response.toString());
                try {
                    Log.d(TAG, "response:" + response.toString());
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    logMesage("translated text by google :" + translatedText);
                    insertTextMessage(translatedText, false);
                } catch (JSONException e) {
                    logErrormeesage("cought " + e.toString() + " while processing json data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrormeesage("got error for google translate " + error.toString());

            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    private void translateText(String translatingText) {

        AppSingletonClass.logDebugMessage(TAG, "Translating text to English:" + translatingText);
        String targetLanguage = "en";
        logMesage("translating input text to english :" + translatingText);
        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAeN43km6hD8UHAUaFEZ8j9iCYFk8puvuE";
        Log.d(TAG, "translateText: " + translatingText);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("q", translatingText);
            jsonObject.put("target", targetLanguage);
        } catch (Exception e) {
            logErrormeesage("cought " + e.toString() + " while forming json body");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logMesage("response from google translate :" + response.toString());
                try {
                    Log.d(TAG, "response:" + response.toString());
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    logMesage("translated text by google is :" + translatedText);
                    requestToDialogFlow(translatedText);
                } catch (JSONException e) {
                    logErrormeesage("cought " + e.toString() + " while processing json data");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrormeesage("error message by google translater is :" + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUESTCODE_FOR_LOCATION:
                break;
            case REQUESTCODE_FOR_AUDIORECORD:
                break;
            default:
        }
    }

    private String getBase64String() {

        Bitmap bitmap = BitmapFactory.decodeFile(filePathImage.toString());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    private void logMesage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void logErrormeesage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }

    private void uploadImageNew(final String encodedstring, final String question) {
        Log.d(TAG, "uploadImageNew: question:" + question);
        Log.d(TAG, "uploadImageNew: image id:" + encodedstring + "\t question:" + question);
        String url = "http://54.201.152.162:5000/vqa";

        final JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("image_id", encodedstring);
            requestBody.put("q", question);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse: " + response);
                    JSONArray jsonArray = response.getJSONArray("results");
                    StringBuilder buffer = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        buffer.append(jsonArray.getJSONArray(i).getString(0)).append(":");
                        buffer.append(jsonArray.getJSONArray(i).getDouble(1)).append("\n");
                    }
                    insertTextMessage(buffer.toString(), false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void alertDialogForChoosingCameraOrGalleryForImage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_to_choose_image_from_gallery_r_camera, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        ImageView disease = view.findViewById(R.id.disease);
        ImageView gallery = view.findViewById(R.id.gallery);
        ImageView weedDetection = view.findViewById(R.id.camera_for_disease_detection);

        disease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                AppSingletonClass.MODEL_FILE = "file:///android_asset/retrained_graph_disease_1.pb";
                AppSingletonClass.LABEL_FILE = "file:///android_asset/retrained_labels_disease_1.txt";
                AppSingletonClass.TOOL_BAR_TITLE = "Deletect Disease";
                startActivity(intent);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getImageFromGallery();
            }
        });
        weedDetection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AppSingletonClass.MODEL_FILE = "file:///android_asset/retrained_graph_weeds.pb";
                AppSingletonClass.LABEL_FILE = "file:///android_asset/retrained_labels_weeds.txt";
                AppSingletonClass.TOOL_BAR_TITLE = "Deletect Weed";
                Intent intent = new Intent(getApplicationContext(), ClassifierActivity.class);
                startActivity(intent);

            }
        });

    }

    private void insertTextMessage(String textMessage, boolean isInputTextMessage) {

        if (!isvQnaEnabled) {
            if (isInputTextMessage) {
                ChatmessageDataClasses.InputTextMessage inputTextMessage = new ChatmessageDataClasses.InputTextMessage(textMessage);
                chatMessages.add(inputTextMessage);
            } else {
                ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(textMessage);
                chatMessages.add(message);
            }

            adapter.notifyItemInserted(chatMessages.size() - 1);
        } else {
            if (isInputTextMessage) {
                ChatmessageDataClasses.InputTextMessage inputTextMessage = new ChatmessageDataClasses.InputTextMessage(textMessage);
                vQnaList.add(inputTextMessage);
            } else {
                ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(textMessage);
                vQnaList.add(message);
            }
            ChatmessageDataClasses.VisualQnA visualQnA = new ChatmessageDataClasses.VisualQnA(vQnaList);
            chatMessages.remove(chatMessages.size() - 1);
            adapter.notifyItemRemoved(chatMessages.size() - 1);
            chatMessages.add(visualQnA);
            adapter.notifyItemInserted(chatMessages.size() - 1);
        }
        chatrecyclerview.scrollToPosition(chatMessages.size() - 1);
        //insert to Database

        ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(getApplicationContext());
        if (isInputTextMessage) {
            helperFunctions.insertInputTextMessage(textMessage);
        } else {
            helperFunctions.insertResponseTextMessage(textMessage);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ChatBotDatabase database = new ChatBotDatabase(getApplicationContext());
            boolean isRegistered = database.isUserRegistered();
            if(!isRegistered) {
                adapter.setJumbleSentence(true);
                adapter.notifyDataSetChanged();
                handler.postDelayed(runnableTemp,1000);


            }else {
                adapter.setJumbleSentence(false);
            }
        }
    };

    Runnable runnableTemp = new Runnable() {
        @Override
        public void run() {
            adapter.setJumbleSentence(false);
        }
    };

    void runWhileLoop(){
        handler.postDelayed(runnable,3000);
    }

}