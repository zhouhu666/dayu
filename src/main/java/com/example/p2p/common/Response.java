package com.example.p2p.common;

public class Response {

    public Response() {

    }

    private String message;
    private int status;
    private Object data;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Response(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
