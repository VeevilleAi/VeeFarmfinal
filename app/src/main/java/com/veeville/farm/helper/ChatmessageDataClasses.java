package com.veeville.farm.helper;

import java.util.List;

/**
 * Created by Prashant C on 18/07/18.
 */
public class ChatmessageDataClasses {
    public static class InputTextMessage {
        public String inputTextMessage;

        public InputTextMessage(String inputTextMessage) {
            this.inputTextMessage = inputTextMessage;
        }
    }

    public static class InputImageMessage {
        public String imageLink;

        public InputImageMessage(String imageLink) {
            this.imageLink = imageLink;
        }
    }

    public static class VegFruitPrice {
        public String title, description, imageLink;

        public VegFruitPrice(String title, String description, String imageLink) {
            this.title = title;
            this.description = description;
            this.imageLink = imageLink;
        }
    }

    public static class ResponseTextMessage {
        public String responseTextMessage;

        public ResponseTextMessage(String inputTextMessage) {
            this.responseTextMessage = inputTextMessage;
        }
    }


    public static class ResponseVideoMessage {
        public List<String> thumbnail;
        public List<String> videoIds;

        public ResponseVideoMessage(List<String> videoIds, List<String> thumbnail) {
            this.videoIds = videoIds;
            this.thumbnail = thumbnail;
        }
    }

    public static class OptionMenu {
        public List<String> menuItems;

        public OptionMenu(List<String> menuItems) {
            this.menuItems = menuItems;
        }
    }

    public static class ResponseImages {
        public List<String> imageLinks, dataOfImages, diseaseNames;

        public ResponseImages(List<String> imageLinks, List<String> dataOfImages, List<String> diseaseNames) {
            this.imageLinks = imageLinks;
            this.dataOfImages = dataOfImages;
            this.diseaseNames = diseaseNames;
        }
    }

    public static class VisualQnA {
        public List elements;

        public VisualQnA(List elements) {
            this.elements = elements;
        }
    }

    public static class Humidity {
        public String place, date;
        public List<HumidityDataValues> humidityDataValues;

        public Humidity(String place, String date, List<HumidityDataValues> humidityDataValues) {
            this.date = date;
            this.place = place;
            this.humidityDataValues = humidityDataValues;
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

        public SoilPH(String date, String place, String value) {
            this.date = date;
            this.place = place;
            this.value = value;
        }
    }

    public static class SoilTemperature {
        public String date, place;
        public List<TempValue> soilMoistureValues;

        public SoilTemperature(String date, String place, List<TempValue> soilMoistureValues) {
            this.date = date;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
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

        public SoilMoisture(String month, String place, List<SoilMoistureValues> soilMoistureValues) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
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

        public WeatherData(String date, String place, String imgLink, String temp, String precipitation, String humidity, String wind, List<List<String>> tempHourly) {
            this.date = date;
            this.place = place;
            this.imgLink = imgLink;
            this.temp = temp;
            this.precipitation = precipitation;
            this.humidity = humidity;
            this.wind = wind;
            this.tempHourly = tempHourly;
        }

    }

    public static class Light {
        public String month, place;
        public List<LightValues> soilMoistureValues;

        public Light(String month, String place, List<LightValues> soilMoistureValues) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
        }


        public static class LightValues {
            public String date, value1;

            public LightValues(String date, String value1) {
                this.date = date;
                this.value1 = value1;
            }
        }
    }
}
