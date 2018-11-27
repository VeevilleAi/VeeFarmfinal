package com.veeville.farm.helper;

/**
 * Created by user on 20-07-2017.
 */

public class InputImageClass {
    public String imagebytes;
    public boolean isUploadSuccess;
    public long timestamp;

    public InputImageClass(String imagebytes, boolean isUploadSuccess, long timestamp) {
        this.imagebytes = imagebytes;
        this.isUploadSuccess = isUploadSuccess;
        this.timestamp = timestamp;
    }
}