package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FLDQueries {

    public static ObservableList<String> getDivisionList(int countryId) throws SQLException {

        ObservableList<String> divisionList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String division = rs.getString("Division");
            divisionList.add(division);
        }
        return divisionList;
    }

//    public static int getDivisionId(String division) throws SQLException {
//        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setString(1, division);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            return rs.getInt("Division_ID");
//        }
//        return 0;
//    }

//    public static String getDivisionString(int divId) throws SQLException {
//        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
//        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
//        ps.setInt(1, divId);
//        ResultSet rs = ps.executeQuery();
//        while (rs.next()) {
//            return rs.getString("Division");
//        }
//        return null;
//    }

    public static ResultSet selectAllFLDs() throws SQLException {
        String sql = "SELECT * FROM client_schedule.first_level_divisions";
        return JDBC.connection.createStatement().executeQuery(sql);
    }
}
