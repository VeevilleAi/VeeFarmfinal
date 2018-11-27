package com.veeville.farm.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import java.util.Objects;
import java.util.Random;
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
    private final String TAG = ProcessBotResponse.class.getSimpleName();
    private ProcessedResult processedResult;
    private UpdateMessageForRegistration updateMessageForRegistration;

    ProcessBotResponse(Result result, Context context, ProcessedResult processedResult, String selectedLanguage, UpdateMessageForRegistration updateMessageForRegistration) {
        this.result = result;
        this.context = context;
        this.processedResult = processedResult;
        this.selectedLanguage = selectedLanguage;
        this.updateMessageForRegistration = updateMessageForRegistration;
    }


    //adding reply message from Dialogflow to recyclerview
    private void addSpeechToMessage(String speech) {
        long timestamp = System.currentTimeMillis();
        ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(speech, timestamp);
        ChatMessagesHelperFunctions chatMessagesHelperFunctions = new ChatMessagesHelperFunctions(context);
        chatMessagesHelperFunctions.insertResponseTextMessage(speech);
        messages.add(message);
    }

    //got the result from after translating to given language
    private void translate(String speech) {

        if (!result.isActionIncomplete()) {
            performAction(speech);
        } else {
            addSpeechToMessage(speech);
            addMessagesToRecyclerview();
        }

    }

    void getResultBack() {
        String speech = result.getFulfillment().getSpeech();
        translateTextToSelectedLanguage(speech);
    }

    private List<String> getFarmElements() {
        List<String> elements = new ArrayList<>();
        elements.add("SoilPh");
        elements.add("Soil Moisture");
        elements.add("Soil Temp");
        elements.add("Light");
        elements.add("Humidity");
        return elements;
    }

    private void addHealthCardReport() {
        List<ChatmessageDataClasses.HealthCard.SingleElementHealth> singleElementHealths = new ArrayList<>();
        List<String> elements = getFarmElements();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int value = random.nextInt(80);
            ChatmessageDataClasses.HealthCard.SingleElementHealth elementHealth = new ChatmessageDataClasses.HealthCard.SingleElementHealth(elements.get(i), value, value - 8, value + 23);
            singleElementHealths.add(elementHealth);
        }
        messages.add(new ChatmessageDataClasses.HealthCard(singleElementHealths));
    }

    // performing task based on action
    private void performAction(String speech) {
        String action = result.getAction();
        logMessage("Action:" + action);
        switch (action) {
            case "how.to.grow.vegetable":
                addSpeechToMessage(speech);
                getPayload();
                handleVideoIntents();
                break;
            case "how.to.grow.fruit":
                addSpeechToMessage(speech);
                getPayload();
                handleVideoIntents();
                break;
            case "price.fruit":
                addSpeechToMessage(speech);
                getPayload();
                getPriceOfFruitANdVegetable();
                break;
            case "price.vegetable":
                addSpeechToMessage(speech);
                getPayload();
                getPriceOfFruitANdVegetable();
                break;
            case "weather":
                processWeatherData();
                break;
            case "action.humidity":
                addSpeechToMessage(speech);
                getPayload();
                addSensorData(action);
                break;
            case "action.SoilPH":
                addSpeechToMessage(speech);
                addSensorData(action);
                break;
            case "action.soil.temperature":
                addSpeechToMessage(speech);
                addSensorData(action);
                break;
            case "action.light":
                addSpeechToMessage(speech);
                getPayload();
                addSensorData(action);
                break;
            case "action.soil.moisture":
                addSpeechToMessage(speech);
                getPayload();
                addSensorData(action);
                break;
            case "microsoft.qnamaker":
                addSpeechToMessage(speech);
                getPayload();
                formQuestionForQnaMaker();
                break;
            case "country":
                addSpeechToMessage(speech);
                getPayload();
                AppSingletonClass.isOTPsent = false;
                addMessagesToRecyclerview();
                updateMessageForRegistration.updateMessageForRegistrayion("Thanks for that!\n" + "Can you also give us your phone number?", false);
                updateMessageForRegistration.makeMessagesNormal(true);
                break;
            case "country-name.mobile-number":
                handleMobileNumberAuthontication(speech);
                break;
            case "input.welcome":
                addSpeechToMessage(speech);
                addMessagesToRecyclerview();
                break;
            case "healthcard":
                addSpeechToMessage(speech);
                addHealthCardReport();
                addMessagesToRecyclerview();
                break;
            default:
                addSpeechToMessage(speech);
                getPayload();
                addMessagesToRecyclerview();
        }
    }

    private void handleMobileNumberAuthontication(String speech) {

        if (!AppSingletonClass.isOTPsent) {
            addSpeechToMessage(speech);
            updateMessageForRegistration.updateMessageForRegistrayion("Excellent!\n" + "You will now receive an OTP, please share that too.", false);
            HashMap<String, JsonElement> map = result.getParameters();
            if (map.containsKey("country") && map.containsKey("number")) {
                String country = map.get("country").getAsString();
                String mobNumber = map.get("number").getAsString();
                logMessage("Aountry:" + country + "\tNumber:" + mobNumber);
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
                logMessage("Failed to get details try again");

            }
        } else {
            HashMap<String, JsonElement> map = result.getParameters();
            String code = map.get("number").getAsString();
            try {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifixationIdMain, code);
                String message = "Neat!\n" + "We have confirmed your number. No more topsy turvy for you anymore.";
                signInWithPhoneAuthCredential(credential, message);
                AppSingletonClass.isOTPsent = false;

            } catch (Exception e) {
                logErrormeesage(e.toString());
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
            logErrorMessage("processWeatherData: city:" + city + "\tdate:" + date + "\tdate period:" + date_period + "\ttime period:" + time_period);
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
        long timestamp = System.currentTimeMillis();
        String message = "Please tell me in which country you live ? so I can give you relevent information.";
        ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message, timestamp);
        messages.add(textMessage);
        ChatMessagesHelperFunctions functions = new ChatMessagesHelperFunctions(context);
        functions.insertResponseTextMessage(message);
        addMessagesToRecyclerview();

    }

    private ChatmessageDataClasses.VegFruitPrice getFruitPrice(String name) {
        long timestamp = System.currentTimeMillis();
        ChatBotDatabase database = new ChatBotDatabase(context);
        List<Fruit> price = database.getPriceOfFruitOrVegetable(name);
        String title;
        String desc;
        ChatmessageDataClasses.VegFruitPrice vegFruitPrice;
        if (price.size() == 0) {
            desc = "as of now i dont know the price of " + name + ", click to know more.";
            title = "title ";
            vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(title, desc, "", timestamp);
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
            vegFruitPrice = new ChatmessageDataClasses.VegFruitPrice(title, desc, price.get(0).imageLink, timestamp);
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
        logMessage("Youtube Query:" + buffer.toString());
    }

    private void getPayload() {
        long timestamp = System.currentTimeMillis();
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
                        logMessage("Payload size:" + jsonElements.size());
                        for (int i = 0; i < jsonElements.size(); i++) {
                            JsonObject object = jsonElements.get(i).getAsJsonObject();
                            String imageLink = object.get("image_url").getAsString();
                            String data = object.get("desc").getAsString();
                            String nameOfDisease = object.get("disease_name").getAsString();
                            images.add(imageLink);
                            dataOfImages.add(data);
                            diseaseNames.add(nameOfDisease);
                        }
                        ChatmessageDataClasses.ResponseImages image = new ChatmessageDataClasses.ResponseImages(images, dataOfImages, diseaseNames, timestamp);
                        messages.add(image);
                    }
                } catch (Exception e) {
                    logErrorMessage(e.toString());
                }
            }
        }
        if (selectableMenu.size() > 0) {
            ChatmessageDataClasses.OptionMenu optionMenu = new ChatmessageDataClasses.OptionMenu(selectableMenu, timestamp);
            messages.add(optionMenu);
        }
    }

    private void weatherApidata(String date, final String city) {

        String url = "http://api.worldweatheronline.com/premium/v1/weather.ashx?key=4098d598f45746aca4c94017181311&q=" + city + "&format=json&num_of_days=10&date=" + date;
        JSONObject object = new JSONObject();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                performWeatherData(response, city);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrorMessage(error.toString());
            }
        });
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void performWeatherData(JSONObject jsonObject, String city) {
        logMessage("JSON weather response:" + jsonObject.toString());
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
                long timestamp = System.currentTimeMillis();
                ChatmessageDataClasses.WeatherData weatherData = new ChatmessageDataClasses.WeatherData(date, city, weatherIconUrl, maxTemperature, prec, hum, wind, houlyDataLists, timestamp);
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
                logMessage("youtube response:" + response.toString());
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
                    long timestamp = System.currentTimeMillis();
                    ChatmessageDataClasses.ResponseVideoMessage message = new ChatmessageDataClasses.ResponseVideoMessage(videoIDs, imageLinks, timestamp);
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
                logErrorMessage(error.toString());
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

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("q", translatingText);
            jsonObject.put("target", this.selectedLanguage);
        } catch (Exception e) {
            logErrormeesage("cought " + e.toString() + "  while forming request body for google translate");
        }
        logMessage("translateTextToSelectedLanguage:" + jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
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
                logErrorMessage(error.toString());
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
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    logMesage("trnaslated  to given Language :" + translatedText);
                    long timestamp = System.currentTimeMillis();
                    ChatmessageDataClasses.ResponseTextMessage message = new ChatmessageDataClasses.ResponseTextMessage(translatedText, timestamp);
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

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                String message = "Neat!\n" + "We have confirmed your number. No more topsy turvy for you anymore.";

                for (int i = 0; i < messages.size(); i++) {
                    messages.remove(i);
                }
                signInWithPhoneAuthCredential(credential, message);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                logErrormeesage(e.toString());

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "invalid mobile number", Toast.LENGTH_SHORT).show();
                    long timestamp = System.currentTimeMillis();
                    String message = "Invalid Mobile number, try again by saying country name.";
                    ChatmessageDataClasses.ResponseTextMessage textMessage = new ChatmessageDataClasses.ResponseTextMessage(message, timestamp);
                    messages.add(textMessage);
                    addMessagesToRecyclerview();

                    ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                    helperFunctions.insertResponseTextMessage(message);
                    updateMessageForRegistration.makeMessagesNormal(false);
                    updateMessageForRegistration.updateMessageForRegistrayion("Awww!\n" + "We didn’t get the OTP. You’re gonna have to start all over again ", true);


                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(context, "quota for this number exhausted try again after some time", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                String message = "Excellent!\n" + "You will now receive an OTP, please share that too.";
                updateMessageForRegistration.updateMessageForRegistrayion(message, false);
                helperFunctions.insertResponseTextMessage(message);
                long timestamp = System.currentTimeMillis();
                ChatmessageDataClasses.ResponseTextMessage responseTextMessage = new ChatmessageDataClasses.ResponseTextMessage(message, timestamp);
                messages.add(responseTextMessage);
                addMessagesToRecyclerview();
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
                        long timestamp = System.currentTimeMillis();
                        if (task.isSuccessful()) {
                            FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                            ChatBotDatabase database = new ChatBotDatabase(context);
                            database.insertMobileNumber(user.getPhoneNumber());
                            new MyAsyncTask().execute(user.getPhoneNumber());
                            ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                            helperFunctions.insertResponseTextMessage(message);
                            updateMessageForRegistration.updateMessageForRegistrayion(message, false);
                            updateMessageForRegistration.makeMessagesNormal(true);
                            ChatmessageDataClasses.ResponseTextMessage responseTextMessage = new ChatmessageDataClasses.ResponseTextMessage(message, timestamp);
                            messages.add(responseTextMessage);
                            addMessagesToRecyclerview();

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(context, "invalid verification code", Toast.LENGTH_SHORT).show();
                                String responseMessage = "Awww!\n" + "We didn’t get the OTP. You’re gonna have to start all over again ";
                                ChatmessageDataClasses.ResponseTextMessage responseTextMessage = new ChatmessageDataClasses.ResponseTextMessage(responseMessage, timestamp);
                                ChatMessagesHelperFunctions helperFunctions = new ChatMessagesHelperFunctions(context);
                                helperFunctions.insertResponseTextMessage(responseMessage);
                                messages.add(responseTextMessage);
                                addMessagesToRecyclerview();
                            }
                        }
                    }
                });
    }


    //adding items to main recyclerview in ChatActivity
    private void addMessagesToRecyclerview() {

        processedResult.result(messages);
    }

    private void logMessage(String logMessage) {
        AppSingletonClass.logDebugMessage(TAG, logMessage);
    }

    interface UpdateMessageForRegistration {
        void updateMessageForRegistrayion(String message, boolean showCard);

        void makeMessagesNormal(boolean showNormal);
    }

    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
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
                return stmt.executeUpdate(insetQuery);

            } catch (Exception e) {
                logErrormeesage(e.toString());
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
        }
    }
}
