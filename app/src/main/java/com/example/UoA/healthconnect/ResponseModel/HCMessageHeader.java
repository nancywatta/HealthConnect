package com.example.UoA.healthconnect.ResponseModel;

/**
 * Created by Nancy on 9/13/14.
 */
public class HCMessageHeader {
    private final String version = Constants.REST_API_VERSION;
    private long timestamp;

    public HCMessageHeader(){
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getVersion() {
        return version;
    }
}
