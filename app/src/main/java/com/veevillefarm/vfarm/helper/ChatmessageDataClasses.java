package com.veevillefarm.vfarm.helper;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Prashant C on 18/07/18.
 * these helper classes used in Chat Message data classes like InputTextMessage,ResponseTextMessage, etc.
 */
public class ChatmessageDataClasses {
    public static class InputTextMessage {
        public String inputTextMessage;
        public long timestamp;

        public InputTextMessage(String inputTextMessage, long timestamp) {
            this.inputTextMessage = inputTextMessage;
            this.timestamp = timestamp;
        }
    }

    public static class InputImageMessage {
        public long timestamp;
        public String imageLink;

        public InputImageMessage(String imageLink, long timestamp) {
            this.imageLink = imageLink;
            this.timestamp = timestamp;
        }
    }

    public static class InputBitMapImage{
        long imgId;
        String awsImageId;
        public boolean isDownloaded;
        public String filePath;
        public byte[] byteImge;
        public InputBitMapImage(long imgId,String awsImageId,boolean isDownloaded,String filePath,byte[] byteImage){
            this.awsImageId = awsImageId;
            this.imgId = imgId;
            this.isDownloaded = isDownloaded;
            this.filePath = filePath;
            this.byteImge = byteImage;
        }
    }

    public static class ResponseBitMapImage{

        public String awsImageId;
        public long imgId;
        public Bitmap bitmap;
        public boolean isDownloaded;
        public String filePath;
        public ResponseBitMapImage(long imgId,String awsImageId,Bitmap thumbnail,boolean isDownloaded,String filePath){
            this.bitmap = thumbnail;
            this.awsImageId = awsImageId;
            this.imgId = imgId;
            this.isDownloaded = isDownloaded;
            this.filePath = filePath;
        }
    }
    public static class VegFruitPrice {
        public String title, description, imageLink;
        public long timestamp;

        public VegFruitPrice(String title, String description, String imageLink, long timestamp) {
            this.title = title;
            this.description = description;
            this.imageLink = imageLink;
            this.timestamp = timestamp;
        }
    }

    public static class DateInMessage {

        public String date;

        public DateInMessage(String date) {
            this.date = date;
        }

    }

    public static class ResponseTextMessage {
        public String responseTextMessage;
        public long timestamp;

        public ResponseTextMessage(String inputTextMessage, long timestamp) {
            this.responseTextMessage = inputTextMessage;
            this.timestamp = timestamp;
        }
    }


    public static class ResponseVideoMessage {
        public List<String> thumbnail;
        public List<String> videoIds;
        public long timestamp;

        public ResponseVideoMessage(List<String> videoIds, List<String> thumbnail, long timestamp) {
            this.videoIds = videoIds;
            this.thumbnail = thumbnail;
            this.timestamp = timestamp;
        }
    }

    public static class OptionMenu {
        public List<String> menuItems;
        public long timestamp;

        public OptionMenu(List<String> menuItems, long timestamp) {
            this.menuItems = menuItems;
            this.timestamp = timestamp;
        }
    }

    public static class ResponseImages {
        public List<String> imageLinks, dataOfImages, diseaseNames;
        public long timestamp;

        public ResponseImages(List<String> imageLinks, List<String> dataOfImages, List<String> diseaseNames, long timestamp) {
            this.imageLinks = imageLinks;
            this.dataOfImages = dataOfImages;
            this.diseaseNames = diseaseNames;
            this.timestamp = timestamp;
        }
    }

    public static class VisualQnA {
        public List elements;
        public long timestamp;

        public VisualQnA(List elements, long timestamp) {
            this.elements = elements;
            this.timestamp = timestamp;
        }
    }

    public static class Humidity {
        public String place, date;
        public long timestamp;
        public List<HumidityDataValues> humidityDataValues;

        public Humidity(String place, String date, List<HumidityDataValues> humidityDataValues) {
            this.date = date;
            this.place = place;
            this.humidityDataValues = humidityDataValues;
            this.timestamp = timestamp;
        }

        public static class HumidityDataValues {
            public String hour, hAbsulute, hRelative;

            public HumidityDataValues(String hour, String hAbsulute, String hRelative) {
                this.hour = hour;
                this.hAbsulute = hAbsulute;
                this.hRelative = hRelative;
            }
        }
    }

    public static class SoilPH {
        public String date, place, value;
        public long timestamp;

        public SoilPH(String date, String place, String value) {
            this.date = date;
            this.place = place;
            this.value = value;
            this.timestamp = timestamp;
        }
    }

    public static class SoilTemperature {
        public String date, place;
        public List<TempValue> soilMoistureValues;
        public long timestamp;

        public SoilTemperature(String date, String place, List<TempValue> soilMoistureValues, long timestamp) {
            this.date = date;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
            this.timestamp = timestamp;
        }


        public static class TempValue {
            public String date, value1;

            public TempValue(String date, String value1) {
                this.date = date;
                this.value1 = value1;
            }
        }
    }

    public static class SoilMoisture {
        public String month, place;
        public List<SoilMoistureValues> soilMoistureValues;
        public long timestamp;

        public SoilMoisture(String month, String place, List<SoilMoistureValues> soilMoistureValues, long timestamp) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
            this.timestamp = timestamp;
        }


        public static class SoilMoistureValues {
            public String date, value1, value2;

            public SoilMoistureValues(String date, String value1, String value2) {
                this.date = date;
                this.value1 = value1;
                this.value2 = value2;
            }
        }
    }

    public static class WeatherData {

        public String date, place, imgLink, temp, precipitation, humidity, wind;
        public List<List<String>> tempHourly;
        public long timestamp;

        public WeatherData(String date, String place, String imgLink, String temp, String precipitation, String humidity, String wind, List<List<String>> tempHourly, long timestamp) {
            this.date = date;
            this.place = place;
            this.imgLink = imgLink;
            this.temp = temp;
            this.precipitation = precipitation;
            this.humidity = humidity;
            this.wind = wind;
            this.tempHourly = tempHourly;
            this.timestamp = timestamp;
        }

    }

    public static class Light {
        public String month, place;
        public List<LightValues> soilMoistureValues;
        public long timestamp;

        public Light(String month, String place, List<LightValues> soilMoistureValues, long timestamp) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
            this.timestamp = timestamp;
        }


        public static class LightValues {
            public String date, value1;

            public LightValues(String date, String value1) {
                this.date = date;
                this.value1 = value1;

            }
        }
    }

    public static class HealthCard {
        public List<SingleElementHealth> healthList;

        public HealthCard(List<SingleElementHealth> healthList) {
            this.healthList = healthList;
        }

        public static class SingleElementHealth {
            public String title;
            public float average, min, max;

            public SingleElementHealth(String title, float average, float min, float max) {
                this.average = average;
                this.min = min;
                this.max = max;
                this.title = title;
            }
        }
    }
}
