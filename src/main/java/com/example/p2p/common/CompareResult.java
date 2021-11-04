package com.example.p2p.common;

import com.alibaba.fastjson.JSONObject;

public class CompareResult {
    private String hostIp;
    private String message;
    private String md5src;
    private String fileSize;
    private String fileUpdateTime;


    public CompareResult() {
    }
    public CompareResult(String hostIp, String message, String md5src, String fileSize, String fileUpdateTime) {
        this.hostIp = hostIp;
        this.message = message;
        this.md5src = md5src;
        this.fileSize = fileSize;
        this.fileUpdateTime = fileUpdateTime;
    }

    public String getHostIp() {
        return hostIp;
    }

    public String getMessage() {
        return message;
    }

    public String getMd5src() {
        return md5src;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMd5src(String md5src) {
        this.md5src = md5src;
    }

    public JSONObject toJsonObject(){
        return JSONObject.parseObject(JSONObject.toJSONString(this));
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }


    public String getFileUpdateTime() {
        return fileUpdateTime;
    }

    public void setFileUpdateTime(String fileUpdateTime) {
        this.fileUpdateTime = fileUpdateTime;
    }


}
