package com.veeville.farm.activity;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.veeville.farm.helper.AppSingletonClass;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensGConnection {

    private Handler handler = new Handler();
    private List<String> humidityList = new ArrayList<>();
    private List<String> tempList = new ArrayList<>();
    private List<String> lightList = new ArrayList<>();
    private String IP, TAG = "SensGConnection";
    private Context context;

    SensGConnection(String Ip, Context context) {
        this.IP = Ip;
        this.context = context;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                sendRequest();
                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(runnable, 2000);
    }


    void sendRequest() {
        String url = "http://" + IP + "/sensorStream";
        final String refer = "http://" + IP + "/index.html";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: response string:success SENSg");
                try {
                    handleSensorData(response);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(context, "not yet connected to SENSg", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(context, "please check the connection", Toast.LENGTH_SHORT).show();
                }
                Log.e(TAG, "onErrorResponse: " + error.toString());
                if (error.networkResponse != null) {
                    String s = new String(error.networkResponse.data);
                    Log.e(TAG, "onErrorResponse: actual error:" + s);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> map = new HashMap<>();
                map.put("referer", refer);
                return map;
            }

        };
        AppSingletonClass.getInstance().addToRequestQueue(request);
    }

    private void handleSensorData(String response) throws UnsupportedEncodingException {
        byte[] byteArray = response.getBytes("ISO-8859-1");

        if (byteArray.length < 5) {
            return;
        }
        int time, humidity, temperature, pressure, light, ir, noise, yaw, pitch, roll, magX, magY, magZ;
        time = ((byteArray[3] << 24 & 0xff) | (byteArray[2] << 16 & 0xff) | (byteArray[1] << 8) | (byteArray[0])) / 1000;
        Log.d(TAG, "handleSensorData: " + time);
        int index = 4;
        humidity = byteArray[index] / 2;
        if (humidityList.size() < 20) {
            humidityList.add(humidity + "");
        } else {
            humidityList.remove(humidityList.size() - 1);
            humidityList.add(0, humidity + "");
        }
        index++;

        temperature = byteArray[index + 1] << 8 | byteArray[index];
        temperature = temperature / 100;
        Log.d(TAG, "handleSensorData: temp data:" + temperature);
        if (tempList.size() < 20) {
            tempList.add(temperature + "");

        } else {
            tempList.remove(humidityList.size() - 1);
            tempList.add(0, temperature + "");

        }
        index += 2;

        pressure = byteArray[index + 3] << 24 | byteArray[index + 2] << 16 | byteArray[index + 1] << 8 | byteArray[index];
        index += 4;
        Log.d(TAG, "handleSensorData: " + pressure);
        light = byteArray[index + 3] << 24 | byteArray[index + 2] << 16 | byteArray[index + 1] << 8 | byteArray[index];
        if (lightList.size() < 20) {
            lightList.add(light + "");

        } else {
            lightList.remove(lightList.size() - 1);
            lightList.add(0, light + "");

        }
        index += 4;

        ir = byteArray[index + 1] << 8 | byteArray[index];
        ir = ir / 100;
        index += 2;
        Log.d(TAG, "handleSensorData: " + ir);
        noise = byteArray[index];
        index++;
        Log.d(TAG, "handleSensorData: " + noise);

        int accX = byteArray[index + 1] << 8 | byteArray[index];
        if (accX >= 32768) {
            accX = -(65536 - accX);
        }
        accX = accX / 1000;
        index += 2;
        Log.d(TAG, "handleSensorData: " + accX);

        int accY = byteArray[index + 1] << 8 | byteArray[index];
        if (accY >= 32768) {
            accY = -(65536 - accY);
        }
        accY = accY / 1000;
        index += 2;
        Log.d(TAG, "handleSensorData: " + accY);

        int accZ = byteArray[index + 1] << 8 | byteArray[index];
        if (accZ >= 32768) {
            accZ = -(65536 - accZ);
        }
        accZ = accZ / 1000;
        Log.d(TAG, "handleSensorData: " + accZ);
        index += 2;

        yaw = byteArray[index + 1] << 8 | byteArray[index];
        if (yaw >= 32768) {
            yaw = -(65536 - yaw);
        }
        yaw = yaw / 10;
        Log.d(TAG, "handleSensorData: " + yaw);
        index += 2;


        pitch = byteArray[index + 1] << 8 | byteArray[index];
        if (pitch >= 32768) {
            pitch = -(65536 - pitch);
        }
        pitch = pitch / 10;
        Log.d(TAG, "handleSensorData: " + pitch);
        index += 2;

        roll = byteArray[index + 1] << 8 | byteArray[index];
        if (roll >= 32768) {
            roll = -(65536 - roll);
        }
        roll = roll / 10;
        Log.d(TAG, "handleSensorData: " + roll);
        index += 2;

        magX = byteArray[index + 1] << 8 | byteArray[index];
        if (magX >= 32768) {
            magX = -(65536 - magX);
            Log.d(TAG, "handleSensorData: " + magX);
        }

        index += 2;

        magY = byteArray[index + 1] << 8 | byteArray[index];
        if (magY >= 32768) {
            magY = -(65536 - magY);
            Log.d(TAG, "handleSensorData: " + magY);
        }
        index += 2;

        magZ = byteArray[index + 1] << 8 | byteArray[index];
        if (magZ >= 32768) {
            magZ = -(65536 - magZ);
            Log.d(TAG, "handleSensorData: " + magZ);
        }
    }

    ChatmessageDataClasses.Humidity getHumidity() {

        List<ChatmessageDataClasses.Humidity.HumidityDataValues> valuesList = new ArrayList<>();
        int hour = 1;
        hour = hour * humidityList.size() + 1;
        for (int i = 0; i < humidityList.size(); i++) {
            ChatmessageDataClasses.Humidity.HumidityDataValues values = new ChatmessageDataClasses.Humidity.HumidityDataValues(getHour(hour), humidityList.get(i) + "", "N");
            valuesList.add(values);
            hour--;
        }
        return new ChatmessageDataClasses.Humidity("Bengaluru", "Today", valuesList);
    }

    ChatmessageDataClasses.SoilTemperature getTemperature() {

        List<ChatmessageDataClasses.SoilTemperature.TempValue> valuesList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            ChatmessageDataClasses.SoilTemperature.TempValue values = new ChatmessageDataClasses.SoilTemperature.TempValue("", tempList.get(i) + "");
            valuesList.add(values);
        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.SoilTemperature("Today", "Bengaluru", valuesList,timestamp);
    }

    ChatmessageDataClasses.Light getLight() {

        int mitures = 1;
        mitures = mitures * tempList.size() * 10;
        List<ChatmessageDataClasses.Light.LightValues> valuesList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            ChatmessageDataClasses.Light.LightValues values = new ChatmessageDataClasses.Light.LightValues(gettimeForTime(-mitures), lightList.get(i) + "");
            valuesList.add(values);
            mitures = mitures - 10;

        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.Light("Today", "Bengaluru", valuesList,timestamp);
    }

    private String gettimeForTime(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        int min = calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour + ":" + min;
    }

    private String getHour(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -hours);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return hour + "";
    }
}
