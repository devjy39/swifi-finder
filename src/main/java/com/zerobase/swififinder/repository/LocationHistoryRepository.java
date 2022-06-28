package com.zerobase.swififinder.repository;

import com.zerobase.swififinder.dto.LocationHistory;
import com.zerobase.swififinder.util.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationHistoryRepository {
    private final DbConnector dbConnector = new DbConnector();

    public int save(String lat, String lnt) {
        Connection connection = dbConnector.ConnectSqlite();
        PreparedStatement preparedStatement = null;
        int affectedRows = 0;

        try {

            String sql = " INSERT INTO LOCATION_HISTORY(LAT, LNT, VIEW_DATATIME) values (?, ?, datetime()) ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, String.valueOf(lat));
            preparedStatement.setString(2, String.valueOf(lnt));

            affectedRows = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }

        return affectedRows;
    }

    public List<LocationHistory> findAll() {
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

        return list;
    }

    public int deleteById(String id) {
        Connection connection = new DbConnector().ConnectSqlite();
        PreparedStatement preparedStatement = null;
        int affectedRows = 0;

        try {
            String sql = " delete from LOCATION_HISTORY where ID = ? ";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);

            affectedRows = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbConnector.closeConnect(connection, preparedStatement, null);
        }

        return affectedRows;
    }
}
