package com.veeville.farm.farmer.FarmerHelperClasses;

import java.util.List;

/**
 * Created by Prashant C on 26/10/18.
 */
public class SoilTestResult {
    public List<SingleSoilTestResult> soilTests;

    public SoilTestResult(List<SingleSoilTestResult> soilTests) {
        this.soilTests = soilTests;
    }

    public static class SingleSoilTestResult {
        public long timestamp;
        public List<SingleSoilTestResultSingleElement> singleSoilTestResultSingleElements;

        public SingleSoilTestResult(long timestamp, List<SingleSoilTestResultSingleElement> singleSoilTestResultSingleElements) {
            this.timestamp = timestamp;
            this.singleSoilTestResultSingleElements = singleSoilTestResultSingleElements;
        }

        public static class SingleSoilTestResultSingleElement {
            public String element, description;
            public float result, rangeMax, rangeMin;

            public SingleSoilTestResultSingleElement(String element, String description, float result, float rangeMin, float rangeMax) {
                this.element = element;
                this.description = description;
                this.result = result;
                this.rangeMin = rangeMin;
                this.rangeMax = rangeMax;

            }
        }

    }

    public static class SoilTestGraph {

    }
}
