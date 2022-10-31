package com.example.CLOOK.dao;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.BufferedReader;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.example.CLOOK.domain.AirVO;
import com.example.CLOOK.domain.GeocodingVO;
import com.example.CLOOK.domain.WeatherVO;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public interface AirRepsitory {

    public static List<AirVO> getAir(String stationName)
            throws IOException, ParseException {

        String apiUrl = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";
        // 홈페이지에서 받은 키
        String serviceKey = "lsreK53XwFXG2rEI3GpisRYQCjg97dt7uTl0HEZnBtYQvqdxXub024qirOptZW3z%2FEJyGQIDVoSWWrzXnUMBxQ%3D%3D";
        String returnType = "JSON"; // 타입 xml, json 등등 ..
        String numOfRows = "100";
        String pageNo = "1";
        String dataTerm = "DAILY"; // 조회하고싶은 날짜
        String ver ="1.3";

        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("returnType", "UTF-8") + "=" + URLEncoder.encode(returnType, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "="
                + URLEncoder.encode(numOfRows, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("stationName", "UTF-8") + "="
                + URLEncoder.encode(stationName, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataTerm", "UTF-8") + "="
                + URLEncoder.encode(dataTerm, "UTF-8"));          
        urlBuilder.append("&" + URLEncoder.encode("ver", "UTF-8") + "=" + URLEncoder.encode(ver, "UTF-8"));

        // urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" +
        // URLEncoder.encode("UTF-8")); //경도
        // urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" +
        // URLEncoder.encode(ny, "UTF-8")); //위도

        URL url = new URL(urlBuilder.toString());
        // 어떻게 넘어가는지 확인하고 싶으면 아래 출력분 주석 해제
        // System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        /*
         * StringBuilder sb = new StringBuilder();
         * String line;
         * while ((line = rd.readLine()) != null) {
         * sb.append(line);
         * }
         * rd.close();
         * conn.disconnect();
         * String result = sb.toString();
         * 
         * return result;
         */

        List<AirVO> listairVO = new ArrayList<AirVO>();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(rd.readLine());
        JSONObject response = (JSONObject) object.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONArray items = (JSONArray) body.get("items");
        System.out.println(items);
        //JSONArray item = (JSONArray) items.get("item");

        // String status = (String) response.get("status");
        for (int i = 0; i < items.size(); i++) {
            AirVO airVO = new AirVO();
            object = (JSONObject) items.get(i);
            String dataTime = (String) object.get("dataTime");
            String pm10Value24 = (String) object.get("pm10Value24");
            String pm25Value24 = (String) object.get("pm25Value24");

            
            airVO.setDataTime(dataTime);
            airVO.setPm10Value24(pm10Value24);
            airVO.setPm25Value24(pm25Value24);

            listairVO.add(airVO);
        }


        return listairVO;

    }

}