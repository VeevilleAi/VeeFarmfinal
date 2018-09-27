package com.veeville.farm.helper;

/**
 * Created by user on 20-07-2017.
 */

public class InputImageClass {
    public String imagebytes;
    public boolean isUploadSuccess;

    public InputImageClass(String imagebytes, boolean isUploadSuccess) {
        this.imagebytes = imagebytes;
        this.isUploadSuccess = isUploadSuccess;
    }
}