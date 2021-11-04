package com.example.p2p.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import io.swagger.models.auth.In;

import java.lang.reflect.Array;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
//        //获取指定时间的时间戳，除以1000说明得到的是秒级别的时间戳（10位）
//        long time = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse("2021-03-13 20:00:00", new ParsePosition(0)).getTime();
//
//        //获取时间戳
//        long now1 = System.currentTimeMillis();
//        long now2 = new Date().getTime();
//
//        System.out.println("获取指定时间的时间戳:" + time);
//        System.out.println("当前时间戳:" +now1);
//        System.out.println("当前时间戳:" +now2);

//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date=new Date();
//        int a = 3;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        System.out.println(sdf.format(date));
//        System.out.println(calendar.getInstance().getTimeInMillis());
//        calendar.add(Calendar.DAY_OF_MONTH, -a);
//        date = calendar.getTime();
//        System.out.println(sdf.format(date));
//        String a = "rdss";
//        List<String> testDomain = Arrays.asList("rds", "ums");
////        for(TestEnum b : TestEnum.values()){
////            if(b.name().equals(a)){
////                System.out.print("OK");
////            }else{
////                System.out.print("Failed");
////            }
////        }
//        if(testDomain.contains(a)){
//            System.out.print("OK");
//        }else{
//                System.out.print("Failed");
//            }

        String aa = "{\n" +
                "    \"total\": 2,\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"requestCount\": 12,\n" +
                "            \"address\": \"GET /approval/v1/template/detail\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"requestCount\": 365,\n" +
                "            \"address\": \"POST /approval/v1/order/add/third\"\n" +
                "        },\n"  +
                "    ]\n" +
                "}";

        String bb = "{\n" +
                "    \"total\": 5,\n" +
                "    \"list\": [\n" +
                "        {\n" +
                "            \"requestCount\": 1,\n" +
                "            \"address\": \"GET /approval/v1/template/detail\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"requestCount\": 2,\n" +
                "            \"address\": \"POST /approval/v1/order/add/third\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"requestCount\": 3,\n" +
                "            \"address\": \"POST /approval/v1/order/list\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"requestCount\": 4,\n" +
                "            \"address\": \"POST /approval/v1/order/process\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"requestCount\": 5,\n" +
                "            \"address\": \"GET /approval/v1/template/systemNames-test\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JSONObject jsonObject1 = JSONObject.parseObject(aa);
        JSONObject jsonObject2 = JSONObject.parseObject(bb);
        InterfaceInfo interfaceInfo1 = JSON.parseObject(JSON.toJSONString(jsonObject1), InterfaceInfo.class);
        InterfaceInfo interfaceInfo2 = JSON.parseObject(JSON.toJSONString(jsonObject2), InterfaceInfo.class);
//        System.out.print("total: "+ JSON.parseObject(JSON.toJSONString(jsonObject)).getInteger("total"));
//        System.out.print("requestCount: "+ interfaceInfo1.getList().get(4).getRequestCount());

//        JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(jsonObject1.getJSONArray("list")));
//        Map<Integer, Integer> temp = new HashMap<>();
//        for(int i=0;i < jsonArray.size();i++){
//            JSONObject nn = jsonArray.getJSONObject(i);
//            temp.put(i, nn.getInteger("requestCount"));
//            nn.remove("requestCount");
//        }
//        System.out.print("address: "+ JSON.toJSONString(jsonArray));
//
//        for(int i=0;i < jsonArray.size();i++){
//            JSONObject nn = jsonArray.getJSONObject(i);
////            temp.put(i, nn.getInteger("requestCount"));
////            nn.remove("requestCount");
//
//            nn.put("",temp.get(i));
//        }

        List<ChainList> compare = new ArrayList<>();

        for (ChainList c1 : interfaceInfo1.getList()) {
            boolean b = false;
            for (ChainList c2 : interfaceInfo2.getList()) {
                if (c1.getAddress().equals(c2.getAddress())) {
                    ChainList c = new ChainList();
                    c.setAddress(c1.getAddress());
                    c.setRequestCount(c1.getRequestCount() + c2.getRequestCount());
//                    c.setRequestCount(c1.getRequestCount());
                    compare.add(c);
                    b = true;
                }
            }
            if (!b) {
                compare.add(c1);
            }
        }

//        for(int i=0; i < interfaceInfo1.getList().size();i++){
//            System.out.print("\ni="+i);
//            String address1 = interfaceInfo1.getList().get(i).getAddress();
//            int total1 = interfaceInfo1.getList().get(i).getRequestCount();
//            System.out.print("\naddress1: " + address1);
//            for(int j=0; j < interfaceInfo2.getList().size(); j++){
//                System.out.print("\nj="+j);
//                String address2 = interfaceInfo2.getList().get(j).getAddress();
//                int total2 = interfaceInfo1.getList().get(i).getRequestCount();
//                System.out.print("\naddress2: " + address2);
//                if(address1.equalsIgnoreCase(address2)){
//                    System.out.print("\naddreass 相同");
//                    System.out.print("\n11111: "+ JSON.toJSONString(interfaceInfo1.getList().get(i)));
//                    int total = total1+total2;
//                    interfaceInfo1.getList().get(i).setRequestCount(total);
//                    break;
//                }
//                compare.add(interfaceInfo1.getList().get(i));
//
//            }
//        }

//        Arrays.sort(interfaceInfo1.getList().toArray(), Collections.reverseOrder());
//        for(ChainList list:interfaceInfo1.getList())
//        interfaceInfo1.getList().sort(((o1, o2) -> o2.getRequestCount() - o1.getRequestCount()));
        System.out.print("\ncompare: "+ JSON.toJSONString(compare));


//        Sets.SetView difference = Sets.difference(Sets.newHashSet(jsonObject1.getJSONArray("address")), Sets.newHashSet(jsonObject2.getJSONArray("address")));
//        List<ChainList> mm = new ArrayList<>();
//        mm.addAll(difference);
//
//        System.out.print("difference: "+ JSON.toJSONString(mm));
    }
}
