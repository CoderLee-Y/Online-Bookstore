package com.Lee.bookstore_backend.utils.messageUtils;


import com.alibaba.fastjson.JSONObject;

public class returnMessage {
    private int status;
    private String message;
    private JSONObject data;

    public returnMessage(int status, String message, JSONObject data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    returnMessage(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public JSONObject getData() {
        return data;
    }

    @Override
    public String toString() {
        return "{" +
            "status: " + status +
            ", message: '" + message + '\'' +
            ", data: " + data +
            '}';
    }
}
