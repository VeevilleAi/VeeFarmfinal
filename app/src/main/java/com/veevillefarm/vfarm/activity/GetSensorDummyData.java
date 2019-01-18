package com.veevillefarm.vfarm.activity;

import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Prashant C on 25/09/18.
 * getting randomly generated data to show in app
 */
public class GetSensorDummyData {


    private final String TAG =  GetSensorDummyData.class.getSimpleName();
    //generating light values randomly
    public ChatmessageDataClasses.Light insertLight() {
        List<ChatmessageDataClasses.Light.LightValues> lightValues = new ArrayList<>();
        for (int j = 5; j < 20; j++) {
            Random r = new Random();
            int i1 = r.nextInt(1075 - 1000) + 1000;
            String value;
            if (j < 10) {
                value = "0" + j;
            } else {
                value = j + "";
            }
            ChatmessageDataClasses.Light.LightValues values = new ChatmessageDataClasses.Light.LightValues(value, i1 + "");
            lightValues.add(values);
        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.Light("Today", "beengaluru", lightValues, timestamp);
    }

    //generating Humidity values randomly
    public ChatmessageDataClasses.Humidity insertHumidity() {

        List<ChatmessageDataClasses.Humidity.HumidityDataValues> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            String value;
            if (i < 10) {
                value = "0" + i;
            } else {
                value = i + "";
            }
            Random r = new Random();
            int i1 = r.nextInt(75 - 70) + 70;
            int i2 = i1 + 2;
            ChatmessageDataClasses.Humidity.HumidityDataValues val0 = new ChatmessageDataClasses.Humidity.HumidityDataValues(value, i1 + "", i2 + "");
            values.add(val0);
        }
        return new ChatmessageDataClasses.Humidity("Bengaluru", "Today", values);

    }

    public ChatmessageDataClasses.SoilPH insertSoilPh() {
        return new ChatmessageDataClasses.SoilPH("Today", "Bengaluru", "6.5");
    }

    //generating Soil temperature randomly
    public ChatmessageDataClasses.SoilTemperature insertSoilTemperature() {

        List<ChatmessageDataClasses.SoilTemperature.TempValue> soilMoistureValues2 = new ArrayList<>();
        for (int j = 5; j < 20; j++) {
            Random r = new Random();
            int i1 = r.nextInt(27 - 23) + 23;
            String value;
            if (j < 10) {
                value = "0" + j;
            } else {
                value = j + "";
            }
            ChatmessageDataClasses.SoilTemperature.TempValue values = new ChatmessageDataClasses.SoilTemperature.TempValue(value, "" + i1);
            soilMoistureValues2.add(values);
        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.SoilTemperature("Today", "Bengaluru", soilMoistureValues2, timestamp);
    }

    //generating soil moisture values randomly
    public ChatmessageDataClasses.SoilMoisture insertSoilMoisture() {

        List<ChatmessageDataClasses.SoilMoisture.SoilMoistureValues> soilMoistureValues2 = new ArrayList<>();
        for (int j = 1; j < 31; j++) {
            String value;
            if (j < 10) {
                value = "0" + j;
            } else {
                value = j + "";
            }
            Random r = new Random();
            int i1 = r.nextInt(47 - 44) + 44;
            int i2 = i1 + 20;
            ChatmessageDataClasses.SoilMoisture.SoilMoistureValues values = new ChatmessageDataClasses.SoilMoisture.SoilMoistureValues(value, "" + i1, "" + i2);
            soilMoistureValues2.add(values);
        }
        long timestamp = System.currentTimeMillis();
        return new ChatmessageDataClasses.SoilMoisture("July", "Bengaluru", soilMoistureValues2, timestamp);
    }
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }

    private void logErrorMessage(String errorMessage){
        AppSingletonClass.logErrorMessage(TAG,errorMessage);
    }
}
