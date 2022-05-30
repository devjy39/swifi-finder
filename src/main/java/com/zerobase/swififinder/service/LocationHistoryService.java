package com.zerobase.swififinder.service;

import com.zerobase.swififinder.dto.LocationHistory;
import com.zerobase.swififinder.util.DbConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationHistoryService {
    private static final DbConnector dbConnector = new DbConnector();

    public void historyInsert(String lat, String lnt) {
        Connection connection = dbConnector.ConnectSqlite();
        PreparedStatement preparedStatement = null;

        try {

            String sql = " INSERT INTO LOCATION_HISTORY(LAT, LNT, VIEW_DATATIME) values (?, ?, datetime()) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(lat));
            preparedStatement.setString(2, String.valueOf(lnt));

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println(" insert success");
            } else {
                System.out.println(" insert fail");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }
    }

    public List<LocationHistory> historySelect() {
        List<LocationHistory> list = new ArrayList<>();

        Connection connection = new DbConnector().ConnectSqlite();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            String sql = " select * from 'LOCATION_HISTORY' ";
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LocationHistory history = new LocationHistory();
                history.setId(resultSet.getInt("ID"));
                history.setLat(resultSet.getDouble("LAT"));
                history.setLnt(resultSet.getDouble("LNT"));
                history.setDateTime(resultSet.getString("VIEW_DATATIME"));

                list.add(history);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, resultSet);
        }
        System.out.println(list.size()+"개 history 조회");

        return list;
    }

    public void historyDelete(String id) {
        Connection connection = new DbConnector().ConnectSqlite();
        PreparedStatement preparedStatement = null;

        try {
            String sql = " delete from LOCATION_HISTORY where ID = ? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                System.out.println(" delete success");
            } else {
                System.out.println(" delete fail");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }
    }

}
