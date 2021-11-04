package com.example.p2p.bean;

import com.example.p2p.service.FileService;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public boolean md5Compare(String mdrSrc, String md5dest){
        if(mdrSrc.isEmpty() || md5dest.isEmpty()){
            System.out.println("mds is empty!");
            return false;
        }
        //8b53508d454130c212d55c0d39ae0ea1
        else if(mdrSrc.equalsIgnoreCase(md5dest)) {
            System.out.println("P2P文件分发正确！" + mdrSrc);
            return true;
        } else{
            System.out.println("P2P文件分发不正确！");
            return false;
        }
    }

    @Override
    public String getFileSize(String path) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        File file = new File(path);
        if(!file.exists() || !file.isFile()){
            System.out.println("文件不存在");
            return "文件不存在";
        }
        long size = file.length();

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            System.out.println("size: "+String.format("%d B", size));
        return String.format("%d B", size);
    }

    @Override
    public String getFileName(String path) {
        File file = new File(path);
        if(!file.exists() || !file.isFile()) {
            System.out.println("文件不存在");
            return "文件不存在";
        }
        System.out.println("md5: "+file.getName());
        return file.getName();
    }

    @Override
    public String getFileMd5(String path) throws IOException {
        File file = new File(path);
        if(!file.exists() || !file.isFile()) {
            System.out.println("文件不存在");
            return "文件不存在";
        }
        FileInputStream fis = new FileInputStream(path);
        String md5 = DigestUtils.md5DigestAsHex(fis);
        System.out.println("md5: "+md5);
        fis.close();
        return md5;
    }

    @Override
    public String getFileUpdateTime(String path){
        File file = new File(path);
        if(!file.exists() || !file.isFile()) {
            System.out.println("文件不存在");
            return "文件不存在";
//            throw new RuntimeException("文件不存在");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(file.lastModified());
        String time = sdf.format(cal.getTime());
        System.out.println(time);

        return time;
    }
}
