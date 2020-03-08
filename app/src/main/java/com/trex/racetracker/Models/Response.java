package com.trex.racetracker.Models;

public class Response {
    private int responseCode;
    private String message;

    public Response() {
        this.responseCode = 0;
        this.message = null;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setResponseCode(int myResponseCode) {
        if (myResponseCode != 0) {
            this.responseCode = myResponseCode;
        } else {
            this.responseCode = 0;
        }
    }
    public void setMessage(String myMessage) {
        if (myMessage != null) {
            this.message = myMessage;
        } else {
            this.message = "";
        }
    }

}
