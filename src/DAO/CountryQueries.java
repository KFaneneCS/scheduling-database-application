package DAO;

import utility.AlertPopups;
import utility.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryQueries {


    public static ResultSet selectAllCountries() throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries";
        return JDBC.connection.createStatement().executeQuery(sql);
    }



    // possibly delete everything below this
    public static int convertToCountryId(String country) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("Country_ID");
        }
        AlertPopups.generateErrorMessage(CustomerQueries.GENERAL_ERROR_MESSAGE);
        return -1;
    }

    public static int convertToCountryId(int divId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getInt("Country_ID");
        }
        AlertPopups.generateErrorMessage(CustomerQueries.GENERAL_ERROR_MESSAGE);
        return -1;
    }

    public static String countryIdToString(int countryId) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            return rs.getString("Country");
        }
        AlertPopups.generateErrorMessage(CustomerQueries.GENERAL_ERROR_MESSAGE);
        return null;
    }

}
