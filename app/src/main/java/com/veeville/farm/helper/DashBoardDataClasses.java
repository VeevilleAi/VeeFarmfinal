package com.veeville.farm.helper;

import java.util.List;

/**
 * Created by Prashant C on 04/07/18.
 */
public class DashBoardDataClasses {

    public static class HumidityData {
        public String date, place;
        public List<HumidityDataValues> humidityDataValues;

        public HumidityData(String date, String place, List<HumidityDataValues> humidityDataValues) {
            this.date = date;
            this.place = place;
            this.humidityDataValues = humidityDataValues;
        }


        public static class HumidityToday {

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

    public static class SoilMoistureData {
        public String month, place;
        public List<SoilMoistureValues> soilMoistureValues;

        public SoilMoistureData(String month, String place, List<SoilMoistureValues> soilMoistureValues) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
        }

        public static class SoilMoistureGraphMonthData {

        }

        public static class SoilMoistureGraphyearData {

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

    public static class SoilPH {
        public String month, place, value;

        public SoilPH(String month, String place, String value) {
            this.month = month;
            this.place = place;
            this.value = value;
        }
    }

    public static class LightData {
        public String month, place;
        public List<LightValues> soilMoistureValues;

        public LightData(String month, String place, List<LightValues> soilMoistureValues) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
        }

        public static class LightFirstMonthGraphData {
            public LightFirstMonthGraphData() {

            }
        }

        public static class LightGraphYearData {
            public LightGraphYearData() {

            }
        }


        public static class LightValues {
            public String date, value1;

            public LightValues(String date, String value1) {
                this.date = date;
                this.value1 = value1;
            }
        }
    }

    public static class TemperatureData {
        public String month, place;
        public List<TempValue> soilMoistureValues;

        public TemperatureData(String month, String place, List<TempValue> soilMoistureValues) {
            this.month = month;
            this.place = place;
            this.soilMoistureValues = soilMoistureValues;
        }

        public static class SoilTempMonthGraphData {
            public SoilTempMonthGraphData() {

            }
        }

        public static class SoilTempyearGraphData {
            public SoilTempyearGraphData() {

            }

        }
        public static class TempValue {
            public String date, value1;

            public TempValue(String date, String value1) {
                this.date = date;
                this.value1 = value1;
            }
        }
    }


    public static class WorkFlowData {
        public String title, date;
        public List<String> subTitle, description;

        public WorkFlowData(String title, String date, List<String> subTitle, List<String> description) {
            this.title = title;
            this.date = date;
            this.subTitle = subTitle;
            this.description = description;
        }
    }

    public static class SensorGraph {
        public String selectedValue;
        public int selectedPosition;

        public SensorGraph(String selectedValue, int selectedPosition) {
            this.selectedValue = selectedValue;
            this.selectedPosition = selectedPosition;
        }
    }
}
