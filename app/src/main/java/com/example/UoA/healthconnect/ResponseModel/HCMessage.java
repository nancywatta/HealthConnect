package com.example.UoA.healthconnect.ResponseModel;

/**
 * Created by Nancy on 9/13/14.
 */
public class HCMessage {
    private String status;
    private HCMessageHeader header;
    private Object response;
    private String error;

    public HCMessage(){
        this.header = new HCMessageHeader();
    }

    public String getStatus() {
        return status;
    }

    public HCMessageHeader getHeader() {
        return header;
    }

    public Object getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
