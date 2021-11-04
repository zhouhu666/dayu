package com.example.p2p.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.p2p.bean.FileServiceImpl;
import com.example.p2p.common.CompareResult;
import com.example.p2p.common.DeleteResult;
import com.example.p2p.common.FileInfo;
import com.example.p2p.common.Response;
import io.swagger.annotations.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Api(tags = "文件分发控制类")
@RestController
@RequestMapping("/file")
public class FileController {
    @ApiOperation(value = "获取文件信息", notes = "根据path获取文件信息")
    @GetMapping("/info")
    @ApiImplicitParam(name = "path", value = "文件路径", required = true, dataType = "String", paramType = "query")
    public Response getFileInfo(@RequestParam String path) throws IOException {
        FileInfo fi = new FileInfo();
        FileServiceImpl fsi = new FileServiceImpl();
        Response res = new Response();
//        String path = "D:\\桌面\\DDOS攻击介绍.pptx";
        fi.setFileName(fsi.getFileName(path));
        fi.setFileSize(fsi.getFileSize(path));
        fi.setFileMd5(fsi.getFileMd5(path));
        fi.setFileUpdateTime(fsi.getFileUpdateTime(path));
        JSONObject jsonO = new JSONObject();
        jsonO.put("fileName", fi.getFileName());
        jsonO.put("fileSize", fi.getFileSize());
        jsonO.put("fileMd5", fi.getFileMd5());
        jsonO.put("fileUpdateTime", fi.getFileUpdateTime());
        res.setStatus(200);
        res.setMessage("Success");
        res.setData(jsonO);

        return res;
    }

    @ApiOperation(value = "对比文件md5", notes = "根据path对比文件md5")
    @GetMapping("/compare")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",
                    value = "文件路径",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
            @ApiImplicitParam(name = "md5dest",
                    value = "目标文件md5",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
    })
    public Response fileCompare(@RequestParam String path, @RequestParam String md5dest) throws IOException {
        String message = "";
        int status = 0;
        Response res = new Response();
        FileServiceImpl fsi = new FileServiceImpl();
        String md5src = fsi.getFileMd5(path);
        if (fsi.md5Compare(md5src, md5dest)) {
            status = 200;
            message = "success,文件md5值是一致";

        } else {
            status = 404;
            message = "error,文件md5值不一致";
        }

        JSONObject jsonO = new JSONObject();
        jsonO.put("md5src", md5src);
        jsonO.put("fileSize", fsi.getFileSize(path));
        jsonO.put("fileUpdateTime", fsi.getFileUpdateTime(path));

        res.setStatus(status);
        res.setMessage(message);
        res.setData(jsonO);

        return res;
    }

    @ApiOperation(value = "根据ip对比文件md5", notes = "根据ip获取主机上的文件做md5对比")
    @PostMapping ("/compare/ips")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",
                    value = "文件路径",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
            @ApiImplicitParam(name = "md5dest",
                    value = "目标文件md5",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
    })

    public Response hostFileCompare(@RequestParam String[] ips, @RequestParam String path, @RequestParam String md5dest) throws URISyntaxException, IOException {
        Response res = new Response();
        JSONObject jsonObject = new JSONObject();
        List<JSONObject> listOk = new ArrayList<>();
        List<JSONObject> listFail = new ArrayList<>();
        for (int i = 0; i < ips.length; i++) {
            System.out.println("ip: "+ips[i]);
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder = new URIBuilder();
            uriBuilder.setScheme("http");
            uriBuilder.setHost(ips[i]+":8083");
            uriBuilder.setPath("/file/compare");
            uriBuilder.setParameter("path", path);
            uriBuilder.setParameter("md5dest", md5dest);
//
            System.out.println("uri: "+uriBuilder.build());

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            String cookie1 = "jms_csrftoken=TpPUUC8zPcHMiRATXyUEgTTcqS63GBGScYR4sA07VYX0XKFd2qK1VkYA532ehVFp; CI-SESSION=20b1a931-c7d2-4178-9565-706507a057c8; ponlineCurrentVerion=%E6%96%87%E4%BB%B6%E6%9B%B4%E6%96%B0%E6%97%B6%E9%97%B4Tue%20Mar%2002%202021%2002:37:15%20GMT+0000%20(Coordinated%20Universal%20Time); paasZoneCodeCookie=BJHT; env=prod; uid=80277350; token=aee7fa83c44a1d95e380bcbc9e735eb4; cloud_portal_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJjc3JmVG9rZW4iOiJjZTkzZmNjNjFlNzM0YjQ0OWVjM2U2OWI2NTc3ZTZjOCIsInNzb1Rva2VuIjoiZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmhjSEFpT2lKdmNIQnZMV05zYjNWa0xYQnZjblJoYkNJc0luTjFZaUk2SWxjNU1EQTFORGMzSWl3aWIyNWpaU0k2Wm1Gc2MyVXNJbWx6Y3lJNkltOXdjRzh0WTJ4dmRXUXRjRzl5ZEdGc1Z6a3dNRFUwTnpjd0lpd2lkSGx3SWpvaU1DSXNJbWxoZENJNk1UWXhORGcwTlRZeE4zMC4wVDgyaTFQYVJjd1V0U2JxdlQwblBLNUlzdUJabmlkQUVmUllQZWJuOUxNIiwidXNlciI6eyJ1c2VyX2lkIjoiVzkwMDU0NzciLCJ1c2VyX25hbWUiOiLmtoLlrrbpkasiLCJ1c2VyX2VtYWlsIjoidi10dWppYXhpbkBvcHBvLmNvbSJ9LCJleHAiOjE2MTU3MTM4NTF9.n5I1qHvpeS3VAu_y_aJwIMY5g8LFgX83Nix6tpTY2DnVHZBNack9zAT8Z2P8xZevOrfh2afmYH1T044u8eaV2mlH7NFPEjXLCgQFDFFZV4OuwfBC3R2QpZWOQt-qXvGxSJSBuER3Qpsay2h7gUQdP8rjO8IUPxq6Y8_6Y_SZoKs; cloud_portal_csrftoken=ce93fcc61e734b449ec3e69b6577e6c8";
            httpGet.addHeader("Cookie", cookie1);
            httpGet.addHeader("Content-Type", "application/x-www-form-urlencoded");

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                String bodyString = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("bodyString: "+bodyString);
                JSONObject jsonObject1 = JSONObject.parseObject(bodyString);
                if(jsonObject1.getInteger("status") == 200){
                    listOk.add(new CompareResult(ips[i], jsonObject1.getString("message"), jsonObject1.getJSONObject("data").getString("md5src"),
                            jsonObject1.getJSONObject("data").getString("fileSize"),jsonObject1.getJSONObject("data").getString("fileUpdateTime")).toJsonObject());

                }else{
                    listFail.add(new CompareResult(ips[i], jsonObject1.getString("message"), jsonObject1.getJSONObject("data").getString("md5src"),
                            jsonObject1.getJSONObject("data").getString("fileSize"),jsonObject1.getJSONObject("data").getString("fileUpdateTime")).toJsonObject());
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            jsonObject.put("OK", listOk);
            jsonObject.put("Fail", listFail);
        }

        res.setStatus(200);
        res.setMessage("Success");
        res.setData(jsonObject);
        return res;
    }

    @ApiOperation(value = "删除单个文件", notes = "根据path删除文件")
    @DeleteMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",
                    value = "文件路径",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
    })
    public Response deleteFile(@RequestParam String path) throws IOException {
        Response res = new Response();
        String message = "";
        int status = 0;
        try {
            File file = new File(path);
            System.gc();
            if(file.delete()){
                status = 200;
                message = file.getName() + "文件已被删除！";
            }else{
                status = 400;
                message = file.getName() + "文件删除失败！";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        res.setStatus(status);
        res.setMessage(message);

        return res;
    }

    @ApiOperation(value = "根据ip批量删除文件", notes = "根据ip批量删除文件")
    @DeleteMapping ("/delete/ips")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path",
                    value = "文件路径",
                    required = true,
                    dataType = "String",
                    paramType = "query"),
    })

    public Response patchDeleteFile(@RequestParam String[] ips,  @RequestParam String path) throws URISyntaxException, IOException {
        Response res = new Response();
        String message = "";
        int status = 0;
        JSONObject jsonObject = new JSONObject();
        List<DeleteResult> listOk = new ArrayList<>();
        List<DeleteResult> listFail = new ArrayList<>();

        for (int i = 0; ips.length > i; i++) {
            System.out.println("ip: "+ips[i]);
            CloseableHttpClient httpClient = HttpClients.createDefault();

            URIBuilder uriBuilder1 = new URIBuilder();
            uriBuilder1.setScheme("http");
            uriBuilder1.setHost(ips[i]+":8083");
            uriBuilder1.setPath("/file/delete");
            uriBuilder1.setParameter("path", path);
//
            System.out.println("uri: "+uriBuilder1.build());
            HttpDelete httpDelete = new HttpDelete(uriBuilder1.build());
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpDelete);
                String bodyString = EntityUtils.toString(response.getEntity(), "UTF-8");
                System.out.println("bodyString: "+bodyString);
                JSONObject jsonObject1 = JSONObject.parseObject(bodyString);
                if(jsonObject1.getInteger("status") == 200){
                    listOk.add(new DeleteResult(ips[i], jsonObject1.getString("message")));

                }else{
                    listFail.add(new DeleteResult(ips[i], jsonObject1.getString("message")));
                }
            } catch (ClientProtocolException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            jsonObject.put("OK", listOk);
            jsonObject.put("Fail", listFail);
        }

        res.setStatus(200);
        res.setMessage("Success");
        res.setData(jsonObject);
        return res;
    }
}
