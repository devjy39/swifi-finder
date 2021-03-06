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
    private static final String AUTH_KEY = "auth key를 입력하세요.";
    private static final String SERVICE_NAME = "TbPublicWifiInfo";
    private static final String ENCODING_TYPE = "UTF-8";
    private static final String FILE_TYPE = "json";
    private static int total_cnt;
    private static final int MAX_TRANSFER_COUNT = 1000;

    private final WifiInfoService wifiInfoService = new WifiInfoService();

    public int openApiDataMigration() {
        int loadDataCount = 0;

        wifiInfoService.cleanDbTable();
        do {
            int endIdx = loadDataCount + MAX_TRANSFER_COUNT;
            if (total_cnt != 0) {
                endIdx = Math.min(total_cnt, endIdx);
            }

            loadDataCount += wifiInfoService.wifiInsert(indexDataMigration(loadDataCount + 1, endIdx));
            System.out.println("Cumulative number of loaded data: " + loadDataCount);
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
        } catch (Exception e) {
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
        URL url = new URL(new StringBuilder(REQUEST_URL)
                .append("/" + URLEncoder.encode(AUTH_KEY, ENCODING_TYPE))
                .append("/" + URLEncoder.encode(FILE_TYPE, ENCODING_TYPE))
                .append("/" + URLEncoder.encode(SERVICE_NAME, ENCODING_TYPE))
                .append("/" + URLEncoder.encode(String.valueOf(startIdx), ENCODING_TYPE))
                .append("/" + URLEncoder.encode(String.valueOf(endIdx), ENCODING_TYPE))
                .toString());

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
            wifi.setInstYear(safeParseInt(object.get("X_SWIFI_CNSTC_YEAR").getAsString()));
            wifi.setInOutDoor(object.get("X_SWIFI_INOUT_DOOR").getAsString());
            wifi.setRemars(object.get("X_SWIFI_REMARS3").getAsString());
            wifi.setLnt(safeParseDouble(object.get("LNT").getAsString()));
            wifi.setLat(safeParseDouble(object.get("LAT").getAsString()));
            wifi.setWorkDateTime(object.get("WORK_DTTM").getAsString());

            list.add(wifi);
        }

        return list;
    }

    private int safeParseInt(String str) {
        return str.isEmpty() ? 0 : Integer.parseInt(str);
    }

    private double safeParseDouble(String str) {
        return str.isEmpty() ? 0 : Double.parseDouble(str);
    }
}