package com.example.demo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ReverseGeocodingUtil {

    /**
     * 解析阿里的逆地理编码返回的JSON得到省市区
     *
     * @param str
     * @return
     */
    public static Map<String, String> getProvinceCityArea(String str) throws Exception {

        Map<String, String> map = new TreeMap<String, String>();
        JSONObject jsonObject = JSONObject.fromObject(str);
        JSONArray jsonArray = JSONArray.fromObject(jsonObject.getString("addrList"));
        JSONObject json = JSONObject.fromObject(jsonArray.get(0));
        String admName = json.getString("admName");
        if (StringUtil.isEmpty(admName)) {
            throw new Exception("解析错误，省市区为空！");
        }
        String arr[] = admName.split(",");
        if (arr.length != 3) {
            throw new Exception("解析错误，省市区为空！");
        }
        map.put("province", arr[0]);
        map.put("city", arr[1]);
        map.put("area", arr[2]);

        return map;
    }

    /**
     * 阿里的逆地理编码
     *
     * @param longitude 经度 大
     * @param latitude  纬度 小
     * @return
     * @throws IOException
     */
    public static String reverseGeocoding(String longitude, String latitude) throws Exception {

        // type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
        String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + latitude + "," + longitude + "&type=010";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        StringBuffer res = new StringBuffer();
        if (conn.getResponseCode() == 200) { // 200表示请求成功
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            char[] tmpbuf = new char[1024];
            int num = in.read(tmpbuf);
            while (num != -1) {
                res.append(tmpbuf, 0, num);
                num = in.read(tmpbuf);
            }
            in.close();
        } else {
            throw new Exception("请求失败");
        }
        return res.toString();
    }

    public static void main(String[] args) {

        try {
            String str = reverseGeocoding("121.1647550717", "31.0286865463"); // 31.0286865463,121.1647550717
            System.out.println(str);
            Map<String, String> map = getProvinceCityArea(str);
            System.out.println("省：" + map.get("province") + "\n市：" + map.get("city") + "\n区：" + map.get("area"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
