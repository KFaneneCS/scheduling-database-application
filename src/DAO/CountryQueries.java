package DAO;

import utility.JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryQueries {

    public static ResultSet selectAllCountries() throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries";
        return JDBC.connection.createStatement().executeQuery(sql);
    }

}
