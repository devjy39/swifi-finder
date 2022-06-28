package com.zerobase.swififinder.repository;

import com.zerobase.swififinder.dto.WifiInfo;
import com.zerobase.swififinder.util.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WifiInfoRepository {
    private final DbConnector dbConnector = new DbConnector();

    public void cleanTable() {
        Connection connection = dbConnector.ConnectSqlite();
        PreparedStatement preparedStatement = null;
        try {
            String sql = " delete from SWIFI ";

            preparedStatement = connection.prepareStatement(sql);

            System.out.println("clean raw data :"+preparedStatement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }
    }

    public int saveAll(List<WifiInfo> list) {
        int insertDataCount = 0;

        Connection connection = dbConnector.ConnectSqlite();
        PreparedStatement preparedStatement = null;

        try {
            for (WifiInfo wifiInfo : list) {
                String sql = " insert into SWIFI values (?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?) ";

                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, wifiInfo.getId());
                preparedStatement.setString(2, wifiInfo.getWrdofc());
                preparedStatement.setString(3, wifiInfo.getName());
                preparedStatement.setString(4, wifiInfo.getAdress1());
                preparedStatement.setString(5, wifiInfo.getAdress2());
                preparedStatement.setString(6, wifiInfo.getInstFloor());
                preparedStatement.setString(7, wifiInfo.getInstType());
                preparedStatement.setString(8, wifiInfo.getInstMby());
                preparedStatement.setString(9, wifiInfo.getSvcSe());
                preparedStatement.setString(10, wifiInfo.getCmcwr());
                preparedStatement.setString(11, String.valueOf(wifiInfo.getInstYear()));
                preparedStatement.setString(12, wifiInfo.getInOutDoor());
                preparedStatement.setString(13, wifiInfo.getRemars());
                preparedStatement.setString(14, String.valueOf(wifiInfo.getLnt()));
                preparedStatement.setString(15, String.valueOf(wifiInfo.getLat()));
                preparedStatement.setString(16, wifiInfo.getWorkDateTime());

                if (preparedStatement.executeUpdate() > 0) {
                    insertDataCount++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }

        return insertDataCount;
    }

    public List<WifiInfo> findByOrderByDistance(String lat, String lnt, int limitCnt) {
        List<WifiInfo> list = new ArrayList<>();

        Connection connection = dbConnector.ConnectSqlite();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String sql = "select round(degrees(acos(sin(radians( ? )) * sin(radians(s.LAT)) + " +
                    "           (cos(radians( ? )) * cos(radians(s.LAT)) * " +
                    "                cos(radians( ? - s.LNT))))) * 60 * 1.1515 * 1.609344,4) as DISTANCE,\n" +
                    "        s.* " +
                    "from SWIFI as s " +
                    "order by DISTANCE " +
                    "limit ?; ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, lat);
            preparedStatement.setString(2, lat);
            preparedStatement.setString(3, lnt);
            preparedStatement.setString(4, String.valueOf(limitCnt));

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                WifiInfo wifi = new WifiInfo();
                wifi.setDistance(resultSet.getDouble("DISTANCE"));
                wifi.setId(resultSet.getString("SWIFI_MGR_NO"));
                wifi.setWrdofc(resultSet.getString("SWIFI_WRDOFC"));
                wifi.setName(resultSet.getString("SWIFI_MAIN_NM"));
                wifi.setAdress1(resultSet.getString("SWIFI_ADRES1"));
                wifi.setAdress2(resultSet.getString("SWIFI_ADRES2"));
                wifi.setInstFloor(resultSet.getString("SWIFI_INSTL_FLOOR"));
                wifi.setInstType(resultSet.getString("SWIFI_INSTL_TY"));
                wifi.setInstMby(resultSet.getString("SWIFI_INSTL_MBY"));
                wifi.setSvcSe(resultSet.getString("SWIFI_SVC_SE"));
                wifi.setCmcwr(resultSet.getString("SWIFI_CMCWR"));
                wifi.setInstYear(Integer.parseInt(resultSet.getString("SWIFI_CNSTC_YEAR")));
                wifi.setInOutDoor(resultSet.getString("SWIFI_INOUT_DOOR"));
                wifi.setRemars(resultSet.getString("SWIFI_REMARS3"));
                wifi.setLat(Double.parseDouble(resultSet.getString("LAT")));
                wifi.setLnt(Double.parseDouble(resultSet.getString("LNT")));
                wifi.setWorkDateTime(resultSet.getString("WORK_DTTM"));

                list.add(wifi);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, resultSet);
        }
        return list;
    }
}
