package com.example.p2p.common;

public class DeleteResult {
    public DeleteResult() {
    }

    public DeleteResult(String hostIp, String message) {
        this.hostIp = hostIp;
        this.message = message;
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getMessage() {
        return message;
    }

    private String hostIp;

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
}
