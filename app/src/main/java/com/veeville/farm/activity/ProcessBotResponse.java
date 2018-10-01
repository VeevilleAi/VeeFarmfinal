package com.veeville.farm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatBotDatabase;
import com.veeville.farm.helper.ChatMessagesHelperFunctions;
import com.veeville.farm.helper.ChatmessageDataClasses;
import com.veeville.farm.helper.Countries;
import com.veeville.farm.helper.DatabaseCredentials;
import com.veeville.farm.helper.Fruit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ai.api.model.ResponseMessage;
import ai.api.model.Result;

import static com.veeville.farm.helper.AppSingletonClass.mFirebaseAuth;
import static com.veeville.farm.helper.AppSingletonClass.verifixationIdMain;

/**
 * Created by Prashant C on 18/07/18.
 */
public class ProcessBotResponse {
    private Result result;
    private Context context;
    private List<Object> messages = new ArrayList<>();
    private String selectedLanguage;
    private String TAG = "ProcessBotResponse";
    private ProcessedResult processedResult;


    ProcessBotResponse(Result result, Context context, ProcessedResult processedResult, String selectedLanguage) {
        this.result = result;
        this.context = context;
        this.processedResult = processedResult;
        this.selectedLanguage = selectedLanguage;
    }


    //adding reply message from Dialogflow to recyclerview
    private void addSpeechToMessage(String speech) {
        ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(speech);
        ChatMessagesHelperFunctions chatMessagesHelperFunctions = new ChatMessagesHelperFunctions(context);
        chatMessagesHelperFunctions.insertResponseTextMessage(speech);
        messages.add(message);
    }

    //got the result from after translating to given language
    private void translate(String speech) {
        addSpeechToMessage(speech);
        getPayload();
        if (!result.isActionIncomplete())
            performAction();
        else {
            Log.d(TAG, "getSpeechResponse: action param incomplete");
            addMessagesToRecyclerview();
        }
    }

    void getResultBack() {
        String speech = result.getFulfillment().getSpeech();
        translateTextToSelectedLanguage(speech);
    }

    // performing task based on action
    private void performAction() {
        String action = result.getAction();
        Log.d(TAG, "performOnAction: action complete:" + action);
        switch (action) {
            case "how.to.grow.vegetable":
                handleVideoIntents();
                break;
            case "how.to.grow.fruit":
                handleVideoIntents();
                break;
            case "price.fruit":
                removeResponseMessage();
                getPriceOfFruitANdVegetable();
                break;
            case "price.vegetable":
                removeResponseMessage();
                getPriceOfFruitANdVegetable();
                break;
            case "weather":
                removeResponseMessage();
                processWeatherData();
                break;
            case "action.humidity":
                addSensorData(action);
                break;
            case "action.SoilPH":
                addSensorData(action);
                break;
            case "action.soil.temperature":
                addSensorData(action);
                break;
            case "action.light":
                addSensorData(action);
                break;
            case "action.soil.moisture":
                addSensorData(action);
                break;
            case "microsoft.qnamaker":
                formQuestionForQnaMaker();
                break;
            case "country":
                AppSingletonClass.isOTPsent = false;
                addMessagesToRecyclerview();
            case "country-name.mobile-number":
                handleMobileNumberAuthontication();
                break;
            default:
                addMessagesToRecyclerview();
                break;
        }
    }

    private void removeResponseMessage() {

        for (Object o : messages) {
            if (o instanceof ChatmessageDataClasses.ResponseTextMessage) {
                boolean isRemoved = messages.remove(o);
                Log.d(TAG, "removeResponseMessage: " + isRemoved);
            }
        }

    }

    private void handleMobileNumberAuthontication() {

        if (!AppSingletonClass.isOTPsent) {
            HashMap<String, JsonElement> map = result.getParameters();
            if (map.containsKey("country") && map.containsKey("number")) {
                String country = map.get("country").getAsString();
                String mobNumber = map.get("number").getAsString();
                Log.d(TAG, "handleMobileNumberAuthontication: country:" + country + "\tnumber:" + mobNumber);
                Countries countries = new Countries();
                String countryCode = null;
                List<Countries> countriesList = countries.getCountries();
                for (Countries var : countriesList) {
                    if (var.countryName.equals(country)) {
                        countryCode = var.countryCode;
                    }
                }
                if (countryCode != null) {
                    authonticateMobileNumber(countryCode + mobNumber);

                    AppSingletonClass.isOTPsent = true;
                } else {
                    Toast.makeText(context, "sorry this country does not exitst, try with different country.", Toast.LENGTH_SHORT).show();
                }


            } else {
                Log.d(TAG, "handleMobileNumberAuthontication: thank you failed to get");

            }
        } else {
            removeResponseMessage();
            HashMap<String, JsonElement> map = result.getParameters();
            String code = map.get("number").getAsString();
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifixationIdMain, code);
                if (!isUserRegisteredByNumber()) {
                    String message = "Verification successful";
                    signInWithPhoneAuthCredential(credential, message);
                    ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                    helperFunctions.insertResponseTextMessage(message);
                    AppSingletonClass.isOTPsent = false;
                }
            } catch (Exception e) {
                Log.e(TAG, "handleMobileNumberAuthontication: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    //  forming request to make weather APi request
    private void processWeatherData() {

        if (isUserRegisteredByNumber()) {
            HashMap<String, JsonElement> map = result.getParameters();
            String city, date, time_period, date_period;
            city = date = time_period = date_period = null;
            if (map.containsKey("geo_city")) {
                city = map.get("geo_city").getAsString();
            }
            if (map.containsKey("date")) {
                date = map.get("date").getAsString();
            }
            if (map.containsKey("date-period")) {
                date_period = map.get("date-period").getAsString();
            }
            if (map.containsKey("time-period")) {
                time_period = map.get("time-period").getAsString();
            }
            Log.d(TAG, "processWeatherData: city:" + city + "\tdate:" + date + "\tdate period:" + date_period + "\ttime period:" + time_period);
            weatherApidata(date, city);
        } else mobileRegistration();
    }

    private void getPriceOfFruitANdVegetable() {
        if (isUserRegisteredByNumber()) {
            HashMap<String, JsonElement> map = result.getParameters();
            String fruitName = null;

            if (map.containsKey("fruit")) {
                fruitName = map.get("fruit").getAsString();

            } else if (map.containsKey("vegetable")) {
                fruitName = map.get("vegetable").getAsString();
            }
            ChatmessageDataClasses.VegFruitPrice vegFruitPrice = getFruitPrice(fruitName);
            messages.add(vegFruitPrice);
            addMessagesToRecyclerview();
        } else {
            mobileRegistration();
        }
    }

    private boolean isUserRegisteredByNumber() {

        ChatBotDatabase database = new ChatBotDatabase(context);
        return database.isUserRegistered();

    }

    private void mobileRegistration() {

        String message = "Please tell me in which country you live ? so I can give you relevent information.";
        ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message);
        messages.add(textMessage);
        ChatMessagesHelperFunctions functions = new ChatMessagesHelperFunctions(context);
        functions.insertResponseTextMessage(message);
        addMessagesToRecyclerview();

    }

    private ChatmessageDataClasses.VegFruitPrice getFruitPrice(String name) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        List<Fruit> price = database.getPriceOfFruitOrVegetable(name);
        String title;
        String desc;
        ChatmessageDataClasses.VegFruitPrice vegFruitPrice;
        if (price.size() == 0) {
            desc = "as of now i dont know the price of " + name + ", click to know more.";
            title = "title ";
            vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(title, desc, "");
            String message = title + "," + desc + "," + "";
            ChatMessagesHelperFunctions functions = new ChatMessagesHelperFunctions(context);
            functions.insertPriceData(message);
        } else {
            StringBuilder buffer = new StringBuilder();
            for (Fruit fruit : price) {
                buffer.append(fruit.name).append(fruit.pieceOrKg).append("  ").append(fruit.price).append("\n");
            }
            title = "Price(\u20b9)  of " + name + " today in Chennai  is ";
            desc = buffer.toString();
            vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(title, desc, price.get(0).imageLink);
            String message = title + "," + desc + "," + price.get(0).imageLink;
            ChatMessagesHelperFunctions functions = new ChatMessagesHelperFunctions(context);
            functions.insertPriceData(message);
        }

        return vegFruitPrice;
    }

    private void handleVideoIntents() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("how to grow ");
        HashMap<String, JsonElement> params = result.getParameters();
        if (params.containsKey("vegetable")) {
            String vegname = params.get("vegetable").getAsString();
            buffer.append(vegname);
        }
        if (params.containsKey("fruit")) {
            String fruitName = params.get("fruit").getAsString();
            buffer.append(fruitName);
        }

        if (isUserRegisteredByNumber()) {
            loadyoutubeVideo(buffer.toString());
        } else {
            mobileRegistration();
        }
        Log.d(TAG, "handleVideoIntents: youtube query:" + buffer.toString());
    }

    private void getPayload() {
        List<String> selectableMenu = new ArrayList<>();
        List<ResponseMessage> responseMessages = result.getFulfillment().getMessages();
        for (ResponseMessage message : responseMessages) {
            if (message instanceof ResponseMessage.ResponsePayload) {

                try {
                    ResponseMessage.ResponsePayload payload = (ResponseMessage.ResponsePayload) message;
                    JsonObject jsonObject = payload.getPayload();
                    if (jsonObject.has("options")) {
                        JsonArray jsonElements = jsonObject.getAsJsonArray("options");
                        for (int i = 0; i < jsonElements.size(); i++) {
                            selectableMenu.add(jsonElements.get(i).getAsString());
                        }
                    } else if (jsonObject.has("images")) {
                        List<String> images = new ArrayList<>();
                        List<String> dataOfImages = new ArrayList<>();
                        List<String> diseaseNames = new ArrayList<>();
                        JsonArray jsonElements = jsonObject.getAsJsonArray("images");
                        Log.d(TAG, "getPayload: " + jsonElements.size());
                        for (int i = 0; i < jsonElements.size(); i++) {
                            JsonObject object = jsonElements.get(i).getAsJsonObject();
                            String imageLink = object.get("image_url").getAsString();
                            String data = object.get("desc").getAsString();
                            String nameOfDisease = object.get("disease_name").getAsString();
                            images.add(imageLink);
                            dataOfImages.add(data);
                            diseaseNames.add(nameOfDisease);
                        }
                        ChatmessageDataClasses.ResponseImages image = new ChatmessageDataClasses.ResponseImages(images, dataOfImages, diseaseNames);
                        messages.add(image);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "getPayload: " + e.toString());
                }
            }
        }
        if (selectableMenu.size() > 0) {
            ChatmessageDataClasses.OptionMenu optionMenu = new ChatmessageDataClasses.OptionMenu(selectableMenu);
            messages.add(optionMenu);
        }
    }

    private void weatherApidata(String date, final String city) {

        String url = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=461942f4e95c4a1d8fd114744181009&q=" + city + "&format=json&num_of_days=10&date=" + date;
        JSONObject object = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                performWeatherData(response, city);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void performWeatherData(JSONObject jsonObject, String city) {
        Log.d(TAG, "onResponse: " + jsonObject.toString());
        try {
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray weather = data.getJSONArray("weather");
            for (int i = 0; i < weather.length(); i++) {
                JSONObject oneDayDetails = weather.getJSONObject(i);
                String date = oneDayDetails.getString("date");
                String[] dates = date.split("-");
                int year = Integer.parseInt(dates[0]);
                int month = Integer.parseInt(dates[1]);
                int day = Integer.parseInt(dates[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.YEAR, year);
                String format = "dd MMM yyyy";
                SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                date = dateFormat.format(calendar.getTime());
                String maxTemperature = oneDayDetails.getString("maxtempC");
                List<List<String>> houlyDataLists = new ArrayList<>();
                String prec = null, hum = null, wind = null;
                String weatherIconUrl = null;
                JSONArray hourlyData = oneDayDetails.getJSONArray("hourly");
                for (int j = 0; j < hourlyData.length(); j++) {
                    JSONObject threeHourData = hourlyData.getJSONObject(j);

                    int time = Integer.parseInt(threeHourData.getString("time"));
                    int hour = time / 100;
                    int min = time % 100;
                    String timeThreeHour = hour + ":" + min;
                    prec = threeHourData.getString("precipMM");
                    hum = threeHourData.getString("humidity");
                    wind = threeHourData.getString("WindGustKmph");
                    String tempC = threeHourData.getString("tempC");
                    List<String> threeHOurlyStringList = new ArrayList<>();
                    threeHOurlyStringList.add(timeThreeHour);
                    threeHOurlyStringList.add(tempC);
                    JSONArray weatherIconUrlArray = threeHourData.getJSONArray("weatherIconUrl");
                    weatherIconUrl = weatherIconUrlArray.getJSONObject(0).getString("value");
                    houlyDataLists.add(threeHOurlyStringList);
                }
                Log.d(TAG, "performWeatherData: hourly size:" + houlyDataLists.size());
                Log.d(TAG, "performWeatherData: city:" + city);
                ChatmessageDataClasses.WeatherData weatherData = new ChatmessageDataClasses.WeatherData(date, city, weatherIconUrl, maxTemperature, prec, hum, wind, houlyDataLists);
                messages.add(weatherData);
                addMessagesToRecyclerview();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadyoutubeVideo(String title) {
        String url = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + title + "&type=video&key=AIzaSyDZj76GPBWBAP3m78M-kbYH6wMsuDB5rsw";
        JSONObject object = new JSONObject();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
                List<String> videoIDs = new ArrayList<>();
                List<String> imageLinks = new ArrayList<>();
                try {
                    JSONArray items = response.getJSONArray("items");
                    String urlMain = "", videoIDMain = "";
                    for (int i = 0; i < items.length(); i++) {

                        JSONObject singleItem = items.getJSONObject(i);
                        JSONObject id = singleItem.getJSONObject("id");
                        String videoId = id.getString("videoId");
                        JSONObject snippet = singleItem.getJSONObject("snippet");
                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                        JSONObject highThumbNail = thumbnails.getJSONObject("high");
                        String url = highThumbNail.getString("url");
                        videoIDs.add(videoId);
                        imageLinks.add(url);
                        if (i > 4) {
                            break;
                        }
                        if (i == 0) {
                            videoIDMain = videoId;
                            urlMain = url;
                        }
                    }
                    ChatmessageDataClasses.ResponseVideoMessage message = new ChatmessageDataClasses.ResponseVideoMessage(videoIDs, imageLinks);
                    messages.add(message);
                    ChatMessagesHelperFunctions functions = new ChatMessagesHelperFunctions(context);
                    functions.insertResponseYoutubeVideoIds(videoIDMain + "," + urlMain);
                    addMessagesToRecyclerview();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);

    }

    interface ProcessedResult {
        void result(List list);
    }

    private void translateTextToSelectedLanguage(String translatingText) {
        logMesage("trnaslating to given Language :" + translatingText);
        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAeN43km6hD8UHAUaFEZ8j9iCYFk8puvuE";
        Log.d(TAG, "translateText: " + translatingText);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("q", translatingText);
            jsonObject.put("target", this.selectedLanguage);
        } catch (Exception e) {
            logErrormeesage("cought " + e.toString() + "  while forming request body for google translate");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "response:" + response.toString());
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    logMesage("trnaslated  to given Language :" + translatedText);
                    translate(translatedText);
                } catch (JSONException e) {
                    logErrormeesage("cought " + e.toString() + "  while processing json sent by google translate");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrormeesage("error message sent by google transate is  " + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    private void logMesage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    private void logErrormeesage(String errorMessage) {
        AppSingletonClass.logErrorMessage(TAG, errorMessage);
    }


    private void addSensorData(String action) {
        GetSensorDummyData dummyData = new GetSensorDummyData();
        Object object = null;
        switch (action) {
            case "action.humidity":
                object = dummyData.insertHumidity();
                break;
            case "action.SoilPH":
                object = dummyData.insertSoilPh();
                break;
            case "action.soil.temperature":
                object = dummyData.insertSoilTemperature();
                break;
            case "action.light":
                object = dummyData.insertLight();
                break;
            case "action.soil.moisture":
                object = dummyData.insertSoilMoisture();
                break;
            default:
                break;
        }
        messages.add(object);
        addMessagesToRecyclerview();

    }


    private void formQuestionForQnaMaker() {

        HashMap<String, JsonElement> map = result.getParameters();
        if (map.containsKey("apple-disease-names")) {
            String diseaseName = map.get("apple-disease-names").getAsString();
            String formPriceSentence = "how do i cure " + diseaseName;
            requestToQnAmaker(formPriceSentence);

        }
    }

    //sending the request to qnamaker
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
                    translateTextToSelectedLanguageForQnaMaker(answer);


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

    //translating text to english for MS QnA
    private void translateTextToSelectedLanguageForQnaMaker(String translatingText) {
        logMesage("trnaslating to given Language :" + translatingText);
        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAeN43km6hD8UHAUaFEZ8j9iCYFk8puvuE";
        Log.d(TAG, "translateText: " + translatingText);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("q", translatingText);
            jsonObject.put("target", this.selectedLanguage);
        } catch (Exception e) {
            logErrormeesage("cought " + e.toString() + "  while forming request body for google translate");
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "response:" + response.toString());
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    logMesage("trnaslated  to given Language :" + translatedText);
                    ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(translatedText);
                    messages.add(message);
                    processedResult.result(messages);
                    ChatBotDatabase database = new ChatBotDatabase(context);
                    database.insertChats("responsetext", translatedText);
                } catch (JSONException e) {
                    logErrormeesage("cought " + e.toString() + "  while processing json sent by google translate");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrormeesage("error message sent by google transate is  " + error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    //authonticating User Mobile number
    private void authonticateMobileNumber(String mobileNumber) {

        Log.d(TAG, "authonticateMobileNumber: " + mobileNumber);

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                String message = "OTP received successfully";
                signInWithPhoneAuthCredential(credential, message);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "invalid mobile number", Toast.LENGTH_SHORT).show();
                    ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage("Invalid Mobile number, try again by saying country name.");
                    messages.add(textMessage);
                    addMessagesToRecyclerview();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(context, "quota for this number exhausted try again after some time", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                String message = "Please enter the OTP";
                helperFunctions.insertResponseTextMessage(message);
                ChatmessageDataClasses.ResponseTextMessage responseTextMessage = new ChatmessageDataClasses.ResponseTextMessage(message);
                messages.add(responseTextMessage);
                addMessagesToRecyclerview();
                //Toast.makeText(context, "please enter OTP from text message", Toast.LENGTH_SHORT).show();
                verifixationIdMain = verificationId;

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
                (Activity) context,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    //signing in the user after OTP
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String message) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            ChatBotDatabase database = new ChatBotDatabase(context);
                            Toast.makeText(context, " permission granted", Toast.LENGTH_SHORT).show();
                            database.insertMobileNumber(user.getPhoneNumber());
                            Log.d(TAG, "onComplete: " + user.getPhoneNumber());
                            ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message);
                            messages.add(textMessage);
                            new MyAsyncTask().execute(user.getPhoneNumber());

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(context, "invalid verification code", Toast.LENGTH_SHORT).show();
                                String responseMessage = "Invalid OTP ..please try again, by entering Country";
                                ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(responseMessage);
                                messages.add(textMessage);
                            }
                        }
                        addMessagesToRecyclerview();

                    }
                });
    }


    //adding items to main recyclerview in ChatActivity
    private void addMessagesToRecyclerview() {

        processedResult.result(messages);
    }


    //insert user Mobile number to AWS database
    class MyAsyncTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected final Integer doInBackground(String... stringArray) {
            try {
                String number = stringArray[0];
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(DatabaseCredentials.Url + DatabaseCredentials.database, DatabaseCredentials.UserName, DatabaseCredentials.Password);
                Statement stmt = connection.createStatement();
                String insetQuery = "insert into RegisteredMobileNumber values('" + number + "');";
                Log.d(TAG, "doInBackground: query:" + insetQuery);
                return stmt.executeUpdate(insetQuery);

            } catch (Exception e) {
                Log.e(TAG, "doInBackground: " + e.toString());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
        }
    }


}
