package com.veevillefarm.vfarm.activity;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatBotDatabase;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *steps to connect to SENSg
 * we need IP address of SENSg device
 * to get IP by SENSg we should farm hotstop with SSID - NSE_OPEN  andpassword - hotspot_nse and then start SENSg
 * after above steps check device connected to given NSE_OPEn network and one will be SENSg and get ip of that it wil be like 192.168.1.70
 * use that ip in the below code to Connect to SENSg and get data from it
 */
class SensGConnection {

    private Handler handler = new Handler();
    private List<String> humidityList = new ArrayList<>();
    private List<String> tempList = new ArrayList<>();
    private List<String> lightList = new ArrayList<>();
    private final String TAG = SensGConnection.class.getSimpleName();
    private String IP;
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

    //make request to SENSg for sensor data
    private void sendRequest() {
        String url = "http://" + IP + "/sensorStream";
        final String refer = "http://" + IP + "/index.html";
        logMessage("connecting to SENSg");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                logMessage("response string:success SENSg");
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
                    logErrorMessage("please connect to SENSg");
                } else if (error instanceof TimeoutError) {
                    //Toast.makeText(context, "please check the connection", Toast.LENGTH_SHORT).show();
                    logErrorMessage("please try again");
                }
                logErrorMessage(error.toString());
                if (error.networkResponse != null) {
                    String s = new String(error.networkResponse.data);
                    logErrorMessage(s);
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

    //process response came from SENSg and get data like temp,humidty which is shown below
    private void handleSensorData(String response) throws UnsupportedEncodingException {
        byte[] byteArray = response.getBytes("ISO-8859-1");

        if (byteArray.length < 5) {
            return;
        }
        int time, humidity, temperature, pressure, light, ir, noise, yaw, pitch, roll, magX, magY, magZ;
        time = ((byteArray[3] << 24 & 0xff) | (byteArray[2] << 16 & 0xff) | (byteArray[1] << 8) | (byteArray[0])) / 1000;
        logMessage("time:" + time);
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
        if (tempList.size() < 20) {
            tempList.add(temperature + "");

        } else {
            tempList.remove(humidityList.size() - 1);
            tempList.add(0, temperature + "");

        }
        index += 2;

        pressure = byteArray[index + 3] << 24 | byteArray[index + 2] << 16 | byteArray[index + 1] << 8 | byteArray[index];
        logMessage("Pressure:" + pressure);
        index += 4;
        light = byteArray[index + 3] << 24 | byteArray[index + 2] << 16 | byteArray[index + 1] << 8 | byteArray[index];
        if (lightList.size() < 20) {
            lightList.add(light + "");

        } else {
            lightList.remove(lightList.size() - 1);
            lightList.add(0, light + "");

        }
        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertLightValues(System.currentTimeMillis() / 1000, light);
        index += 4;

        ir = byteArray[index + 1] << 8 | byteArray[index];
        ir = ir / 100;
        logMessage("IR:" + ir);
        index += 2;
        noise = byteArray[index];
        logMessage("noise:" + noise);
        index++;

        int accX = byteArray[index + 1] << 8 | byteArray[index];
        if (accX >= 32768) {
            accX = -(65536 - accX);
        }
        accX = accX / 1000;
        index += 2;

        int accY = byteArray[index + 1] << 8 | byteArray[index];
        if (accY >= 32768) {
            accY = -(65536 - accY);
        }
        accY = accY / 1000;
        index += 2;

        int accZ = byteArray[index + 1] << 8 | byteArray[index];
        if (accZ >= 32768) {
            accZ = -(65536 - accZ);
        }
        accZ = accZ / 1000;
        logMessage("accX:" + accX + "\t accY:" + accY + "\taccZ:" + accZ);
        index += 2;

        yaw = byteArray[index + 1] << 8 | byteArray[index];
        if (yaw >= 32768) {
            yaw = -(65536 - yaw);
        }
        yaw = yaw / 10;
        logMessage("Yaw:" + yaw);
        index += 2;


        pitch = byteArray[index + 1] << 8 | byteArray[index];
        if (pitch >= 32768) {
            pitch = -(65536 - pitch);
        }
        pitch = pitch / 10;
        logMessage("Pitch:" + pitch);
        index += 2;

        roll = byteArray[index + 1] << 8 | byteArray[index];
        if (roll >= 32768) {
            roll = -(65536 - roll);
        }
        roll = roll / 10;
        logMessage("Roll:" + roll);
        index += 2;

        magX = byteArray[index + 1] << 8 | byteArray[index];
        if (magX >= 32768) {
            magX = -(65536 - magX);
        }

        index += 2;

        magY = byteArray[index + 1] << 8 | byteArray[index];
        if (magY >= 32768) {
            magY = -(65536 - magY);
        }
        index += 2;

        magZ = byteArray[index + 1] << 8 | byteArray[index];
        if (magZ >= 32768) {
            magZ = -(65536 - magZ);
        }
        logMessage("magX:" + magX + "\tmagY:" + magY + "\tmagZ:" + magZ);
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

    //when user request temperature
    ChatmessageDataClasses.SoilTemperature getTemperature() {

        List<ChatmessageDataClasses.SoilTemperature.TempValue> valuesList = new ArrayList<>();
        for (int i = 0; i < tempList.size(); i++) {
            ChatmessageDataClasses.SoilTemperature.TempValue values = new ChatmessageDataClasses.SoilTemperature.TempValue("", tempList.get(i) + "");
            valuesList.add(values);
        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.SoilTemperature("Today", "Bengaluru", valuesList, timestamp);
    }


    //when user wants light(lux) values
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
        return new ChatmessageDataClasses.Light("Today", "Bengaluru", valuesList, timestamp);
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

    //use this method to ebugg message
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }


    //use this methodto  debug  error message
    private void logErrorMessage(String logErrorMessage) {
        AppSingletonClass.logErrorMessage(TAG, logErrorMessage);
    }
}
