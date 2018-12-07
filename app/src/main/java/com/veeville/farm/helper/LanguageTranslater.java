package com.veeville.farm.helper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant C on 29/11/18.
 */
public class LanguageTranslater {


    private final String TAG = LanguageTranslater.class.getSimpleName();

    public void translateText(List<String> translatingText, String targetLanguage, final LanguageTransResult result) {

        logMesage("translating input text to english :" + translatingText);
        String url = "https://translation.googleapis.com/language/translate/v2?key=AIzaSyAeN43km6hD8UHAUaFEZ8j9iCYFk8puvuE";

        JSONObject jsonObject = new JSONObject();
        StringBuffer buffer = new StringBuffer();
        try {
            for (int i = 0; i < translatingText.size(); i++) {
                buffer.append(translatingText.get(i) + "$");
            }
            if (translatingText.size() > 1)
                jsonObject.put("q", buffer.toString());
            else
                jsonObject.put("q", translatingText.get(0));
            jsonObject.put("target", targetLanguage);
        } catch (Exception e) {
            logErrormeesage("cought " + e.toString() + " while forming json body");
        }
        logMesage(jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logMesage("response from google translater :" + response.toString());
                try {
                    JSONObject resObject = response.getJSONObject("data");
                    JSONArray jsonArray = resObject.getJSONArray("translations");
                    List<String> translatedTextList = new ArrayList<>();
                    JSONObject translatedObject = jsonArray.getJSONObject(0);
                    String translatedText = translatedObject.getString("translatedText");
                    String[] texts = translatedText.split("\\$");
                    logMesage("Size:" + texts.length);
                    for (String temp : texts) {
                        translatedTextList.add(temp);
                    }
                    logMesage(translatedText);

                    result.languageTranResult(translatedTextList);

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

    private void logMesage(String message) {
        AppSingletonClass.logDebugMessage(TAG, message);
    }

    private void logErrormeesage(String errorMessage) {

    }
}
