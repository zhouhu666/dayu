package com.example.p2p.common;

public class FileInfo {
    public FileInfo() {
    }

    public FileInfo(String fileName, String fileSize, String fileMd5) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileMd5 = fileMd5;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    private String fileName;
    private String fileSize;
    private String fileMd5;

    public String getFileUpdateTime() {
        return fileUpdateTime;
    }

    public void setFileUpdateTime(String fileUpdateTime) {
        this.fileUpdateTime = fileUpdateTime;
    }

    private String fileUpdateTime;
}
