package com.zerobase.swififinder.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zerobase.swififinder.dto.WifiInfo;
import com.zerobase.swififinder.service.WifiInfoService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ApiExplorer {
    private static final String REQUEST_URL = "http://openapi.seoul.go.kr:8088";
    private static final String AUTH_KEY = "인증 키";
    private static final String SERVICE_NAME = "TbPublicWifiInfo";
    private static final String ENCODING_TYPE = "UTF-8";
    private static final String FILE_TYPE = "json";
    private static int total_cnt;

    public int openApiDataMigration() {
        final int maxTransDataCnt = 1000;
        int loadDataCount = 0;

        do {
            int endIdx = loadDataCount + maxTransDataCnt;
            if (total_cnt != 0) {
                endIdx = Math.min(total_cnt, endIdx);
            }

            List<WifiInfo> wifiInfoList = indexDataMigration(loadDataCount + 1, endIdx);
            WifiInfoService wifiInfoService = new WifiInfoService();
            if (wifiInfoService.wifiInsert(wifiInfoList)) {
                System.out.println("sql error");
                break;
            }
            loadDataCount += maxTransDataCnt;
        } while (loadDataCount < total_cnt);

        return total_cnt;
    }

    public List<WifiInfo> indexDataMigration(int startIdx, int endIdx) {
        List<WifiInfo> list = null;
        HttpURLConnection connection;
        BufferedReader rd;
        try {
            connection = connectUrl(startIdx, endIdx);

            if (connection.getResponseCode() >= 200 && connection.getResponseCode() <= 300) { //정상
                rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING_TYPE));
            } else {
                rd = new BufferedReader(new InputStreamReader(connection.getErrorStream(), ENCODING_TYPE));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("api connection error");
            return null;
        }

        try {
            String line;
            while ((line = rd.readLine()) != null) {
                if (isFailapiRequest(line)) break;

                JsonObject apiJsonData = JsonParser
                        .parseString(line).getAsJsonObject()
                        .get(SERVICE_NAME).getAsJsonObject();
                printMetaData(apiJsonData);

                list = JsonArrayToList(apiJsonData.get("row").getAsJsonArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
            try {
                rd.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private void printMetaData(JsonObject apiJsonData) {
        total_cnt = apiJsonData.get("list_total_count").getAsInt();
        for (String key : apiJsonData.keySet()) {
            if (key.equals("row")) {
                break;
            }
            System.out.println(key + " : " + apiJsonData.get(key));
        }
    }

    private boolean isFailapiRequest(String line) {
        JsonElement result = JsonParser
                .parseString(line).getAsJsonObject()
                .get("RESULT");

        if (result != null) {
            JsonObject resultObj = result.getAsJsonObject();
            for (String key : resultObj.keySet()) {
                System.out.println(key + " : " + resultObj.get(key));
            }
            return true;
        }

        return false;
    }

    private HttpURLConnection connectUrl(int startIdx, int endIdx) throws IOException {
        StringBuilder urlBuilder = new StringBuilder(REQUEST_URL);
        urlBuilder.append("/" + URLEncoder.encode(AUTH_KEY, ENCODING_TYPE));
        urlBuilder.append("/" + URLEncoder.encode(FILE_TYPE, ENCODING_TYPE));
        urlBuilder.append("/" + URLEncoder.encode(SERVICE_NAME, ENCODING_TYPE));
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(startIdx), ENCODING_TYPE));
        urlBuilder.append("/" + URLEncoder.encode(String.valueOf(endIdx), ENCODING_TYPE));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        return conn;
    }

    private List<WifiInfo> JsonArrayToList(JsonArray jsonArray) {
        List<WifiInfo> list = new ArrayList<>();
        if (jsonArray == null) {
            return null;
        }

        for (JsonElement element : jsonArray) {
            JsonObject object = element.getAsJsonObject();
            WifiInfo wifi = new WifiInfo();
            wifi.setId(object.get("X_SWIFI_MGR_NO").getAsString());
            wifi.setWrdofc(object.get("X_SWIFI_WRDOFC").getAsString());
            wifi.setName(object.get("X_SWIFI_MAIN_NM").getAsString());
            wifi.setAdress1(object.get("X_SWIFI_ADRES1").getAsString());
            wifi.setAdress2(object.get("X_SWIFI_ADRES2").getAsString());
            wifi.setInstFloor(object.get("X_SWIFI_INSTL_FLOOR").getAsString());
            wifi.setInstType(object.get("X_SWIFI_INSTL_TY").getAsString());
            wifi.setInstMby(object.get("X_SWIFI_INSTL_MBY").getAsString());
            wifi.setSvcSe(object.get("X_SWIFI_SVC_SE").getAsString());
            wifi.setCmcwr(object.get("X_SWIFI_CMCWR").getAsString());
            wifi.setInstYear(object.get("X_SWIFI_CNSTC_YEAR").getAsInt());
            wifi.setInOutDoor(object.get("X_SWIFI_INOUT_DOOR").getAsString());
            wifi.setRemars(object.get("X_SWIFI_REMARS3").getAsString());
            wifi.setLnt(object.get("LNT").getAsDouble());
            wifi.setLat(object.get("LAT").getAsDouble());
            wifi.setWorkDateTime(object.get("WORK_DTTM").getAsString());

            list.add(wifi);
        }

        System.out.println(list.size() + "개 data load");

        return list;
    }
}