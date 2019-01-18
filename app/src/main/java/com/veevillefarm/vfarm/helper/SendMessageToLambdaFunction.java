package com.veevillefarm.vfarm.helper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prashant C on 04/01/19.
 */
public class SendMessageToLambdaFunction {

    private final String TAG = SendMessageToLambdaFunction.class.getSimpleName();
    public void sendFcmNotification(final FCMMessage message) throws JSONException {


        String url = "https://y11sxkxb01.execute-api.us-east-1.amazonaws.com/development/veefarmappooperations";

        final JSONObject dataObject = new JSONObject();
        dataObject.put("messageTitle", message.title);
        dataObject.put("messageBody", message.body);
        dataObject.put("fcmToken",message.fcmToken);
        dataObject.put("toAddress",message.to);
        dataObject.put("fromAddress",message.from);
        dataObject.put("fcmTokenOther",message.fcmTokenOfOther);
        dataObject.put("messageType", message.messageType);
        dataObject.put("imageId",message.imageId);
        logMessage(message.title+message.body+message.fcmToken+message.to+message.from+message.messageType);
        logMessage("other FCM:"+message.fcmTokenOfOther);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    logMessage(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logErrorMessage(error.toString());
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return dataObject.toString().getBytes();
            }
        };

        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
