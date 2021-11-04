package com.example.p2p.service;

import java.io.IOException;

public interface FileService {
    /**
     * md5比较
     * @param md5src，md5Src
     * @return
     */
    public boolean md5Compare(String md5src, String md5dest);

    public String getFileSize(String path);

    public String getFileName(String path);

    public String getFileMd5(String path) throws IOException;

    public String getFileUpdateTime(String path);
}
