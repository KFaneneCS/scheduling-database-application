package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactQueries {

    public static ResultSet selectAllContacts() throws SQLException {
        String sql = "SELECT * FROM client_schedule.contacts";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

    public static ObservableList<String> selectAllContacts(String columnName) throws SQLException {

        ObservableList<String> dataList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM client_schedule.contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String data = rs.getString(columnName);
            dataList.add(data);
        }
        return dataList;
    }


}

