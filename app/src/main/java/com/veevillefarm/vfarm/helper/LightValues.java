package com.veevillefarm.vfarm.helper;

/**
 * Created by Prashant C on 19/11/18.
 * used to store light values used in LightActivity as a Hwlper class
 */
public class LightValues {
    public long timestamp, value;

    public LightValues(long timestamp, long value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
